package cs455.overlay.transport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import cs455.overlay.node.Node;

public class TCPConnect implements Runnable {
	private ServerSocket serversocket;
	private Node node;

	public TCPConnect(ServerSocket serversocket, Node node) {
		this.serversocket = serversocket;
		this.node = node;
	}

	public void run() {
		try {
			while (true) {
				System.out.println("---------waiting connect-------");				
				Socket socket = serversocket.accept();
				System.out.println("connected");
				
				TCPServerThread serverthread = new TCPServerThread(socket, node);
				serverthread.run();
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
