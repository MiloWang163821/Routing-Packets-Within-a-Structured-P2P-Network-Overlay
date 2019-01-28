package cs455.overlay.wireformats;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.routing.RoutingTable;

public class RegistrySendsNodeManifes implements Event {
	private byte type;
	private int NR;
	private int thisNode;
	private ArrayList<RoutingTable> routingList;
	private HashMap<Integer, MessagingNode> registryTable;

	public RegistrySendsNodeManifes(int NR, int thisNode, ArrayList<RoutingTable> routingList,
			HashMap<Integer, MessagingNode> registryTable) {
		this.NR = NR;
		this.thisNode = thisNode;
		this.routingList = routingList;
		this.registryTable = registryTable;
	}

	public int getThisNode() {
		return thisNode;
	}

	public ArrayList<RoutingTable> getRoutingList() {
		return routingList;
	}

	public HashMap<Integer, MessagingNode> getRegistryTable() {
		return registryTable;
	}

	public byte getType() {
		return '6' - 48;
	}

	public byte[] dataToBytes() throws IOException {
		System.out.println("Ready to pack data........");

		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

		type = getType();
		dout.write(type);
		dout.writeInt(NR);

		for (int i = 0; i < NR; i++) {
			int toNodeID = routingList.get(i).getToNodeId();
			byte length = (byte) registryTable.get(toNodeID).getNodeAddress().length;
			byte[] toNodeaddress = registryTable.get(toNodeID).getNodeAddress();
			int toNodePort = registryTable.get(toNodeID).getNodePort();
			dout.writeInt(toNodeID);
			dout.write(length);
			dout.write(toNodeaddress);
			dout.writeInt(toNodePort);
		}
		int nodesNumber = registryTable.size();
		dout.writeInt(nodesNumber);
		for (Map.Entry<Integer, MessagingNode> entry : registryTable.entrySet()) {
			int nodesID = entry.getKey();
			dout.writeInt(nodesID);
		}
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		baOutputStream.close();
		dout.close();
		return marshalledBytes;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// HashMap<Integer, MessagingNode> registryTable = new HashMap<Integer,
		// MessagingNode>();
		// ArrayList<Integer> idList = new ArrayList<Integer>();
		//
		// MessagingNode mn1 = new MessagingNode("11", 1001);
		// MessagingNode mn2 = new MessagingNode("22", 1002);
		// MessagingNode mn3 = new MessagingNode("33", 1003);
		// MessagingNode mn6 = new MessagingNode("66", 1006);
		// MessagingNode mn7 = new MessagingNode("77", 1007);
		// MessagingNode mn8 = new MessagingNode("88", 1008);
		// MessagingNode mn9 = new MessagingNode("99", 1009);
		// MessagingNode mn10 = new MessagingNode("1010", 1010);
		// MessagingNode mn4 = new MessagingNode("44", 1004);
		// MessagingNode mn5 = new MessagingNode("55", 1005);
		//
		// registryTable.put(1345, mn1);
		// registryTable.put(2673, mn2);
		// registryTable.put(3990, mn3);
		// registryTable.put(7454, mn7);
		// registryTable.put(3428, mn8);
		// registryTable.put(1931, mn9);
		// registryTable.put(3410, mn10);
		// registryTable.put(5624, mn4);
		// registryTable.put(5123, mn5);
		// registryTable.put(2236, mn6);
		//
		// for (Map.Entry<Integer, MessagingNode> entry : registryTable.entrySet()) {
		// idList.add(entry.getKey());
		// }
		// Collections.sort(idList);
		//
		// int NR = 4;
		//
		// HashMap<Integer, ArrayList<RoutingTable>> routingtable = new HashMap<Integer,
		// ArrayList<RoutingTable>>();
		// int thisNode = 0;
		// int d = 0;
		// int toNode;
		// for (int i = 0; i < idList.size(); i++) {
		// ArrayList<RoutingTable> rt = new ArrayList<RoutingTable>();
		// thisNode = idList.get(i);
		// for (int n = 1; n <= NR; n++) {
		// d = (int) Math.pow(2, n - 1);
		// if (i + d < idList.size()) {
		// toNode = idList.get(i + d);
		// } else {
		// toNode = idList.get(i + d - idList.size());
		// }
		// RoutingTable rtable = new RoutingTable(d, toNode);
		// rt.add(rtable);
		// }
		// routingtable.put(thisNode, rt);
		// }
		//
		// System.out.println(idList);
		//
		// // int key = 1345;
		// // for(int i = 0; i < routingtable.get(key).size(); i++) {
		// // System.out.println(routingtable.get(key).get(i).getDistance() + " " +
		// // routingtable.get(key).get(i).getToNode());
		// // }
		//
		// for (Map.Entry<Integer, ArrayList<RoutingTable>> entry :
		// routingtable.entrySet()) {
		// System.out.println(entry.getKey() + ": ");
		// for (int i = 0; i < entry.getValue().size(); i++) {
		// System.out.println(entry.getValue().get(i).getDistance() + " \t" +
		// entry.getValue().get(i).getToNode());
		// }
		// System.out.println();
		// }

	}

}
