package voip;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import udphole.Hole;

public class VoIP {

    public VoIP(String serverName, int serverPort, String pool) {
        try {
            Hole hole = new Hole(InetAddress.getByName(serverName), serverPort, pool);

            Server server = new Server(hole.getSocket());
            Client client = new Client(hole.getSocket(), hole.getOutAddress(), hole.getOutPort());
            server.start();
            client.start();
        } catch (IOException ex) {
            Logger.getLogger(VoIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        if (args.length < 3) {
            System.exit(-1);
        }
        new VoIP(args[0], Integer.parseInt(args[1]), args[2]);
    }
}
