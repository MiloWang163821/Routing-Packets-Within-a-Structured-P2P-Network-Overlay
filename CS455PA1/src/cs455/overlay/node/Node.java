package cs455.overlay.node;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import cs455.overlay.wireformats.Event;

public interface Node {
	public void onEvent(Event event) throws IOException;
	//public void readCommandline(String command);
}
