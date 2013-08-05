package udphole;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Hole {

    private InetAddress outAddress;
    private int outPort;
    private DatagramSocket socket;

    public Hole() throws SocketException {
        this.socket = new DatagramSocket();
    }

    public InetAddress getOutAddress() {
        return outAddress;
    }

    public int getOutPort() {
        return outPort;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
