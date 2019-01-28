package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cs455.overlay.routing.RoutingTable;
import cs455.overlay.transport.TCPConnect;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.Protocol;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireformats.RegistrySendsNodeManifes;

import java.util.Scanner;

public class Registry implements Node, Runnable {

	private static HashMap<Integer, MessagingNode> registryTable = new HashMap<Integer, MessagingNode>();
	private static HashMap<Integer, TCPSender> registrySendToNode = new HashMap<Integer, TCPSender>();
	private static HashMap<Integer, ArrayList<RoutingTable>> routingtable = new HashMap<Integer, ArrayList<RoutingTable>>();

	private int registry_port;

	public Registry(int registry_port) {
		this.registry_port = registry_port;
	}

	public void setRegistrySendToNode(HashMap<Integer, TCPSender> registrySendToNode) {
		this.registrySendToNode = registrySendToNode;
	}
	//
	// public HashMap<Integer, TCPSender> getRegistrySendToNode() {
	// return registrySendToNode;
	// }
	//
	// public void setRegistryTable(HashMap<Integer, MessagingNode> registryTable) {
	// this.registryTable = registryTable;
	// }
	//
	// public HashMap<Integer, MessagingNode> getRegistryTable() {
	// return registryTable;
	// }
	//
	// public void setRoutingTable(HashMap<Integer, ArrayList<RoutingTable>>
	// routingtable) {
	// this.routingtable = routingtable;
	// }
	//
	// public static HashMap<Integer, ArrayList<RoutingTable>> getRoutingTable() {
	// return routingtable;
	// }

	public String addressByteToString(byte[] byte_Address) throws IOException {
		String str_Address = "";
		str_Address = InetAddress.getByAddress(byte_Address).toString();
		str_Address = (String) str_Address.subSequence(1, str_Address.length());
		return str_Address;
	}

	public int setIdentifier(HashMap<Integer, MessagingNode> registryTable) {
		int id = (int) (Math.random() * 128);
		if (!registryTable.containsKey(id)) {
			return id;
		}
		return setIdentifier(registryTable);
	}

	public synchronized void onEvent(Event event) throws IOException {

		System.out.println("Here is Registry onEvent, type: " + (int) event.getType());
		int port = 0;
		String address = "";
		byte[] dataToSend = null;
		
		switch ((int) event.getType()) {
		case Protocol.OVERLAY_NODE_SENDS_REGISTRATION:
			OverlayNodeSendsRegistration ONSR = (OverlayNodeSendsRegistration) event;
			port = ONSR.getPort();
			address = addressByteToString(ONSR.getAddress());
			MessagingNode MN = new MessagingNode(ONSR.getAddress(), ONSR.getPort());
			boolean tableCheck = true;
			for (Map.Entry<Integer, MessagingNode> entry : registryTable.entrySet()) {
				if ((ONSR.getPort() == entry.getValue().getNodePort())
						&& (Arrays.equals(ONSR.getAddress(), entry.getValue().getNodeAddress()))) {
					tableCheck = false;
				}
			}
			if (tableCheck) {
				int id = setIdentifier(registryTable);
				registryTable.put(id, MN);

				String information = "Registration request successful. The number of messaging nodes currently constituting the overlay is ("
						+ registryTable.size() + ")";
				System.out.println(information);
				RegistryReportsRegistrationStatus RRRS = new RegistryReportsRegistrationStatus(id,
						(byte) information.getBytes().length, information.getBytes());
				dataToSend = RRRS.dataToBytes();

				TCPSender sender = new TCPSender(new Socket(address, port));
				registrySendToNode.put(id, sender);
				setRegistrySendToNode(registrySendToNode);
				sender.sendData(dataToSend);
			} else {
				String information = "Registration failed";
				System.out.println(information);
				RegistryReportsRegistrationStatus RRRS = new RegistryReportsRegistrationStatus(-1,
						(byte) information.getBytes().length, information.getBytes());
				dataToSend = RRRS.dataToBytes();
				TCPSender sender = new TCPSender(new Socket(address, port));
				System.out.println("Sending Registry Reports Registration Status...");
				sender.sendData(dataToSend);
				System.out.println("Send Success");
			}
			break;
		case Protocol.REGISTRY_SENDS_NODE_MANIFEST:

			break;
		default:
			break;

		}
	}

	public void readCommandline(String str) throws IOException {
		
		String[] command = str.split("\\s+");
		System.out.println(Arrays.toString(command));
		switch (command[0]) {
		case "setup-overlay":
			int NR = Integer.parseInt(command[1]);
			List idList = new ArrayList(registryTable.keySet());
			Collections.sort(idList);
			System.out.println(idList);
			for (Entry<Integer, TCPSender> entry : registrySendToNode.entrySet()) {
				System.out.print(entry.getKey() + " ");
			}
			System.out.println();
			routingtable = new HashMap<Integer, ArrayList<RoutingTable>>();
			int thisNode = 0, d = 0, toNode = 0;
			for (int i = 0; i < idList.size(); i++) {
				ArrayList<RoutingTable> routingList = new ArrayList<RoutingTable>();
				thisNode = (int) idList.get(i);
				for (int n = 1; n <= NR; n++) {
					d = (int) Math.pow(2, n - 1);
					if (i + d < idList.size()) {
						toNode = (int) idList.get(i + d);
					} else {
						toNode = (int) idList.get(i + d - idList.size());
					}
					RoutingTable rtable = new RoutingTable(d, toNode);
					routingList.add(rtable);
				}
				routingtable.put(thisNode, routingList);
			}
			for(int i = 0; i < idList.size(); i++) {		
				RegistrySendsNodeManifes RSNM = new RegistrySendsNodeManifes(NR, (int)idList.get(i),routingtable.get((int)idList.get(i)), registryTable);
				int nodeIdISendTo = (int)idList.get(i);
				System.out.println("thisnodetosend " + nodeIdISendTo);

				byte[] dataToSend = RSNM.dataToBytes();
				System.out.println("Sending Node Manifes.....");
				System.out.println(Arrays.toString(dataToSend));
				registrySendToNode.get(nodeIdISendTo).sendData(dataToSend);
				System.out.println("Send success!!!");
			}
			break;
		case "list-messaging-nodes":
			System.out.println("hostname\t\tport-number\t\tnodeID");
			for (Map.Entry<Integer, MessagingNode> entry : registryTable.entrySet()) {
				System.out.println(Arrays.toString(entry.getValue().getNodeAddress()) + "\t\t" + entry.getValue().getNodePort() + "\t\t"
						+ entry.getKey());
			}
			break;
		case "list-routing-tables":
			System.out.println("Here is the list-routing-tables");
			for (Map.Entry<Integer, ArrayList<RoutingTable>> entry : routingtable.entrySet()) {
				System.out.println(entry.getKey() + ": ");
				for (int i = 0; i < entry.getValue().size(); i++) {
					System.out.println(
							entry.getValue().get(i).getDistance() + " \t" + entry.getValue().get(i).getToNodeId());
				}
				System.out.println();
			}
			break;

		}

	}

	public void run() {

		ServerSocket serversocket;
		try {
			serversocket = new ServerSocket(registry_port);

			Thread t_connect = new Thread(new TCPConnect(serversocket, this));
			t_connect.start();

			Scanner scanner = new Scanner(System.in);
			if (scanner.hasNext()) {
				do {
					String command = scanner.nextLine();
					readCommandline(command);
				} while (scanner.hasNextLine());

			}
			scanner.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {

		int registry_port = Integer.parseInt(args[0]);
		Registry R = new Registry(registry_port);
		R.run();

		System.out.println("saffdsfwafwafew");

	}


}
