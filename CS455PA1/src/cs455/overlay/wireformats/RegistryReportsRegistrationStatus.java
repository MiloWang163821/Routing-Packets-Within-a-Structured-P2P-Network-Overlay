package cs455.overlay.wireformats;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistryReportsRegistrationStatus implements Event {

	// byte: Message type (REGISTRY_REPORTS_REGISTRATION_STATUS)
	// int: Success status; Assigned ID if successful, -1 in case of a failure
	// byte: Length of following "Information string" field
	// byte[^^]: Information string; ASCII charset

	private byte type;
	private int status;
	private byte infoLen;
	private byte[] info;

	public RegistryReportsRegistrationStatus(int status, byte infoLen, byte[] info) {
		this.status = status;
		this.infoLen = infoLen;
		this.info = info;
	}

	public int getStatus() {
		return status;
	}

	public byte[] getInfoStr() {
		return info;
	}

	public byte getType() {
		return '3'-48;
	}

	public byte[] dataToBytes() throws IOException {
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

		type = getType();
		dout.write(type);
		dout.writeInt(status);
		dout.write(infoLen);
		dout.write(info);
		dout.flush();

		marshalledBytes = baOutputStream.toByteArray();

		baOutputStream.close();
		dout.close();

		return marshalledBytes;
	}

}
