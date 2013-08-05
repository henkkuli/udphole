package voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Client {

    private DatagramSocket socket;
    private TargetDataLine line;
    private Thread thread;
    private InetAddress addr;
    private int port;

    public Client(DatagramSocket socket, InetAddress addr, int port) {
        this.socket = socket;
        this.addr = addr;
        this.port = port;
        this.openAudioLine();
        this.thread = new Thread(new Runner());
    }

    private void openAudioLine() {
        try {
            AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                throw new UnsupportedOperationException("Audio recording is not supported");
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException ex) {
            throw new UnsupportedOperationException("Audio recording is not supported", ex);
        }
    }

    public void start() {
        this.thread.start();
    }

    private class Runner implements Runnable {

        @Override
        public void run() {
            byte buffer[] = new byte[1024];
            DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length, addr, port);
            while (true) {
                try {
                    line.read(buffer, 0, buffer.length);
                    socket.send(dataPacket);
                } catch (IOException ex) {
                }
            }
        }
    }
}
