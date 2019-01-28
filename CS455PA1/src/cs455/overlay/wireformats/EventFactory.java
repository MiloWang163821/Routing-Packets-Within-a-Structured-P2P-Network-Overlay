package cs455.overlay.wireformats;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cs455.overlay.routing.RoutingTable;

// CS455PA1
// Author: Milo Wang
// Date: Feb 12, 2018
// Class: CS160
// Email: wangxujun0926@126.com
/**
 * 
 */

/**
 * @author milowang
 *
 */
public class EventFactory {

	public static Event toMessage(DataInputStream din) throws IOException {
		int type = (int)din.readByte();
		switch (type) {
		case 2:
			System.out.println("Message type is 2. Going to onEvent 2");
			byte addressLen = din.readByte();
			byte[] address = new byte[(int)addressLen];
			din.read(address);
			int port = din.readInt();
			return new OverlayNodeSendsRegistration(addressLen, address, port);
		case 3:
			System.out.println("Message type is 3. Going to onEvent 3");
			int status = din.readInt();
			byte infoLen = din.readByte();
			byte[] info = new byte[(int)infoLen];
			din.read(info);
			return new RegistryReportsRegistrationStatus(status, infoLen, info);
		case 4:
			System.out.println("Message type is 4. Going to onEvent 4");

		case 5:
			System.out.println("Message type is 5. Going to onEvent 5");

		case 6:
			System.out.println("Message type is 6. Going to onEvent 6");
			ArrayList<RoutingTable> routingList = new ArrayList<RoutingTable>();
			HashMap<Integer, ArrayList<RoutingTable>> routingTable = new HashMap<Integer, ArrayList<RoutingTable>>();
			
			int NR = din.readInt();
			for(int i = 0; i < NR; i++) {
				int toNodeId = din.readInt();
				byte length = din.readByte();
				byte[] toNodeAddress = new byte[(int)length];
				din.read(toNodeAddress);
				int toNodePort = din.readInt();
				RoutingTable rt = new RoutingTable(toNodeAddress, toNodePort);
				routingList.add(toNodeId, rt);
			}
			int nodesNumber = din.readInt();
			int[] nodesIdList = new int[nodesNumber];
			for(int i = 0; i < nodesNumber; i++) {
				nodesIdList[i] = din.readInt();
			}
			return new NodeReportsOverlaySetupStatus(routingTable, nodesIdList);

		case 7:
			System.out.println("Message type is 7. Going to onEvent 7");

		case 8:
			System.out.println("Message type is 8. Going to onEvent 8");

		case 9:
			System.out.println("Message type is 9. Going to onEvent 9");

		case 10:
			System.out.println("Message type is 10. Going to onEvent 10");

		case 11:
			System.out.println("Message type is 11. Going to onEvent 11");

		case 12:
			System.out.println("Message type is 12. Going to onEvent 12");


		}

		return null;

	}

}

/*
				byte length = din.readByte();
				byte[] toNodeAddress = new byte[(int)length];
				din.read(toNodeAddress);
				int toNodePort = din.readInt();
//				int toNodeID = routingList.get(NR).getToNode();
//				byte length = (byte)registryTable.get(toNodeID).getNodeAddress().length;
//				byte[] toNodeaddress = registryTable.get(toNodeID).getNodeAddress();
//				int toNodePort = registryTable.get(toNodeID).getNodePort();
//				dout.writeInt(toNodeID);
//				dout.write(length);
//				dout.write(toNodeaddress);
//				dout.writeInt(toNodePort);

			int nodesNumber = registryTable.size();
			dout.write((byte)nodesNumber);
			for (Map.Entry<Integer, MessagingNode> entry : registryTable.entrySet()) {
				int nodesID = entry.getKey();
				dout.writeInt(nodesID);
			}
*/
