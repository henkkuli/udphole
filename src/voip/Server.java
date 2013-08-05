package voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Server {

    private DatagramSocket socket;
    private SourceDataLine line;
    private Thread thread;

    public Server(DatagramSocket socket) {
        this.socket = socket;
        this.openAudioLine();
        this.thread = new Thread(new Runner());
    }

    private void openAudioLine() {
        try {
            AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                throw new UnsupportedOperationException("Audio playing is not supported");
            }

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException ex) {
            throw new UnsupportedOperationException("Audio playing is not supported", ex);
        }
    }

    public void start() {
        this.thread.start();
    }

    private class Runner implements Runnable {

        @Override
        public void run() {
            byte buffer[] = new byte[1024];
            DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
            while (true) {
                try {
                    socket.receive(dataPacket);
                    line.write(buffer, 0, buffer.length);
                    System.out.println("Received");
                } catch (IOException ex) {
                }
            }
        }
    }
}
