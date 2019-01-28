package cs455.overlay.wireformats;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class OverlayNodeSendsRegistration implements Event {

	private byte type;
	private byte lenAddress;
	private byte[] address;
	private int port;

	public OverlayNodeSendsRegistration(byte lenAddress, byte[] address, int port) {
		this.address = address;
		this.lenAddress = lenAddress;
		this.port = port;
	}

	public byte getType() {
		byte type = '2'- 48;
		return type;
	}

	public byte[] getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public byte[] dataToBytes() throws IOException {
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		type = getType();
		dout.write(type);
		dout.write(lenAddress);
		dout.write(address);
		dout.writeInt(port);
		dout.flush();
		
		marshalledBytes = baOutputStream.toByteArray();
		baOutputStream.close();
		dout.close();
		
		return marshalledBytes;
	}

}
