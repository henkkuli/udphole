package udphole;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Hole {

    private String pool;
    private InetAddress outAddress;
    private int outPort;
    private DatagramSocket socket;

    public Hole(InetAddress serverAddr, int serverPort, String pool) throws SocketException, IOException {
        this.socket = new DatagramSocket();
        this.pool = pool;

        DatagramPacket poolPacket = new DatagramPacket(pool.getBytes(), pool.getBytes().length, serverAddr, serverPort);
        socket.send(poolPacket);
        poolPacket.setData(new byte[1024]);
        socket.receive(poolPacket);
        if (!new String(poolPacket.getData()).equals(pool)) {
            throw new IOException("Server didn't receive pool");
        }

        DatagramPacket okPacket = new DatagramPacket("ok".getBytes(), 2, serverAddr, serverPort);
        socket.send(okPacket);
        okPacket.setData(new byte[6]);
        socket.receive(okPacket);

        this.outAddress = InetAddress.getByAddress(new byte[]{okPacket.getData()[0], okPacket.getData()[1], okPacket.getData()[2], okPacket.getData()[3]});
        this.outPort = (int) ByteBuffer.wrap(okPacket.getData(), 4, 2).getShort() & 0xffff;
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
