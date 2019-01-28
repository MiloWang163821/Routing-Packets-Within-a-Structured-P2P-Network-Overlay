package cs455.overlay.routing;
// CS455PA1
// Author: Milo Wang
// Date: Feb 14, 2018
// Class: CS160
// Email: wangxujun0926@126.com
/**
 * 
 */

/**
 * @author milowang
 *
 */
public class RoutingTable {
	
	private int distance;
	private int toNodeId;
	private byte[] toNodeAddress;
	private int toNodePort;
	
	public RoutingTable(int distance, int toNodeId) {
		this.distance = distance;
		this.toNodeId = toNodeId;
	}
	
	public RoutingTable(byte[] toNodeAddress, int toNodePort) {
		this.toNodeAddress = toNodeAddress;
		this.toNodePort = toNodePort;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public int getToNodeId() {
		return toNodeId;
	}

	
}
