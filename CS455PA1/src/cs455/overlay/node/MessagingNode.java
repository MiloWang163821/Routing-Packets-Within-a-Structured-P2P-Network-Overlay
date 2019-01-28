package cs455.overlay.node;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import cs455.overlay.transport.TCPSender;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.NodeReportsOverlaySetupStatus;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.Protocol;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;

public class MessagingNode implements Node, Runnable {

	private byte[] nodeAddress;
	private int nodePort;
	private HashMap<Integer, TCPSender> nodeSendToRegistry = new HashMap<Integer, TCPSender>();

	private ServerSocket serverSocket;

	public MessagingNode(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public MessagingNode(byte[] nodeAddress, int nodePort) {
		this.nodeAddress = nodeAddress;
		this.nodePort = nodePort;
	}

	public byte[] getNodeAddress() {
		return nodeAddress;
	}

	public int getNodePort() {
		return nodePort;
	}

	public void setNodeSendToRegistry(HashMap<Integer, TCPSender> nodeSendToRegistry) {
		this.nodeSendToRegistry = nodeSendToRegistry;
	}

	public HashMap<Integer, TCPSender> getNodeSendToRegistry(HashMap<Integer, TCPSender> nodeSendToRegistry) {
		return nodeSendToRegistry;
	}

	public synchronized void onEvent(Event event) throws IOException {
		switch ((int) event.getType()) {
		case Protocol.REGISTRY_REPORTS_REGISTRATION_STATUS:
			RegistryReportsRegistrationStatus RRRS = (RegistryReportsRegistrationStatus) event;
			System.out.println("Status: " + RRRS.getStatus());
			String information = new String(RRRS.getInfoStr(), "UTF-8");
			System.out.println(information);
			break;
		case Protocol.REGISTRY_SENDS_NODE_MANIFEST:
			NodeReportsOverlaySetupStatus NPOSS = (NodeReportsOverlaySetupStatus) event;

			System.out.println(NPOSS.getNodesIdList());
			break;

		}

	}

	public void readCommandline(String str) {
		String[] command = str.split("\\s+");

		if (command[0].equals("print-counters-and-diagnostics")) {
			System.out.println("Here is print-counters-and-diagnostics");
		} else if (command[0].equals("exit-overlay")) {
			System.out.println("Let's exit overlay");

		}

	}

	public void run() {
		
		while (true) {
			System.out.println("waiting for the feedback");
			Socket socket;
			try {
				socket = serverSocket.accept();
				System.out.println("Connected");
				TCPServerThread serverThread = new TCPServerThread(socket, this);
				serverThread.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		
		
		
		
		String registry_host = args[0];
		int registry_port = Integer.parseInt(args[1]);

		ServerSocket server = new ServerSocket(0);
		Socket client = new Socket(registry_host, registry_port);

		MessagingNode MN = new MessagingNode(client.getLocalAddress().getAddress(), server.getLocalPort());
		OverlayNodeSendsRegistration ONSR = new OverlayNodeSendsRegistration((byte) MN.getNodeAddress().length,
				MN.getNodeAddress(), MN.getNodePort());
		byte[] dataToSend = ONSR.dataToBytes();

		TCPSender sender = new TCPSender(client);

		HashMap<Integer, TCPSender> nodeSendToRegistry = new HashMap<Integer, TCPSender>();
		nodeSendToRegistry.put(MN.getNodePort(), sender);
		MN.setNodeSendToRegistry(nodeSendToRegistry);
		sender.sendData(dataToSend);

		MessagingNode mn = new MessagingNode(server);
		mn.run();


	}
}
