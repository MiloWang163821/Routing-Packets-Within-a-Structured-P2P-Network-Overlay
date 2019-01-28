package cs455.overlay.transport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.Node;

public class TCPServerThread implements Runnable {

	private Socket socket;
	private Node node;

	public TCPServerThread() {

	}
	
	public TCPServerThread(Socket socket, Node node) {
		this.socket = socket;
		this.node = node;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {

				TCPReceiverThread t_receiver = new TCPReceiverThread(socket, node);
				t_receiver.run();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
