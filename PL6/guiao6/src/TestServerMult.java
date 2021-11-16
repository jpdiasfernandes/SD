import java.io.*;
import java.net.Socket;
import java.util.Random;

class LaunchClient implements Runnable {
    private final int TID;
    private SenderReceiverClient src;
    public LaunchClient(int TID) {
        this.TID = TID;
    }
    public void run() {
        try {
            Socket socket = new Socket("localhost", 12345);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            src = new SenderReceiverClient(out, in);

            Random rnd = new Random();
            //final int bound = rnd.nextInt(10);
            final int bound = 20;
            for (int i = 0; i < bound; i++) {
                //byte op = (byte) (rnd.nextInt(2) + 1);
                byte op = 1;
                src.sendGeneric(op, 10);
                System.out.println("Total after (" + TID + ") sent: " + src.receive());
                System.out.flush();
            }
            socket.shutdownOutput();

            double mean = src.receive();
            System.out.println("--> Mean response to (" + TID + ") : " + mean);
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {

        }
    }
}
public class TestServerMult {
    // O objetivo deste teste é apenas para testar se a concorrência ao Arithmetic está
    // a ser bem feita.
    public static void main(String[] args) throws InterruptedException {
        final int T = 30;
        Thread t[] = new Thread[T];

        int i;
        for (i = 0; i < T; i++)
            t[i] = new Thread(new LaunchClient(i));

        for (i = 0; i < T; i++)
            t[i].start();

        for (i = 0; i < T; i++)
            t[i].join();

    }
}
