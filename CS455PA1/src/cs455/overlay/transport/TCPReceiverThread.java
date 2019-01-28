package cs455.overlay.transport;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import cs455.overlay.node.Node;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;

public class TCPReceiverThread implements Runnable {

	private Socket socket;
	private DataInputStream din;
	private Node node;

	public TCPReceiverThread(Socket socket, Node node) throws IOException {
		this.socket = socket;
		this.node = node;
		din = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		int dataLength;
		try {
			dataLength = din.readInt();
			byte[] dataIReceived = new byte[dataLength];
			din.readFully(dataIReceived, 0, dataLength);
			System.out.println("Received Data");			
			ByteArrayInputStream baInputStream = new ByteArrayInputStream(dataIReceived);
			DataInputStream din2 = new DataInputStream(new BufferedInputStream(baInputStream));
			Event event = EventFactory.toMessage(din2);
			node.onEvent(event);
			System.out.println("End onEvent");
			
			
		} catch (SocketException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

}
