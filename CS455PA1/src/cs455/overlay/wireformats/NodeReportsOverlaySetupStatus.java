package cs455.overlay.wireformats;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cs455.overlay.routing.RoutingTable;

// CS455PA1
// Author: Milo Wang
// Date: Feb 15, 2018
// Class: CS160
// Email: wangxujun0926@126.com
/**
 * 
 */

/**
 * @author milowang
 *
 */
public class NodeReportsOverlaySetupStatus implements Event{
	
	private HashMap<Integer, ArrayList<RoutingTable>> routingTable;
	private int[] nodesIdList;
	
	public NodeReportsOverlaySetupStatus(HashMap<Integer, ArrayList<RoutingTable>> routingTable, int[] nodesIdList) {
		this.routingTable = routingTable;
		this.nodesIdList = nodesIdList;
	}
	public HashMap<Integer, ArrayList<RoutingTable>> geRroutingTable() {
		return routingTable;
	}
	public int[] getNodesIdList() {
		return nodesIdList;
	}
	
	
	public NodeReportsOverlaySetupStatus(int status, byte infoLen, byte[] info) {

	}

	public byte getType() {
		return 0;
	}

	public byte[] dataToBytes() throws IOException {

		return null;
	}
}
