package cs455.overlay.wireformats;
import java.io.IOException;

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
public interface Event {

	public byte getType();

	public byte[] dataToBytes() throws IOException;

}
