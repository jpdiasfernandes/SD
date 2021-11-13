import java.io.*;
import java.net.Socket;
import java.util.Random;

class LaunchClient implements Runnable {
    private final int TID;
    public LaunchClient(int TID) {
        this.TID = TID;
    }
    public void run() {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            Random rnd = new Random();
            //final int bound = rnd.nextInt(10);
            final int bound = 20;
            for (int i = 0; i < bound; i++) {
                out.println(10);
                out.flush();
                String response = in.readLine();
                System.out.println(i + "th response to (" + TID + ") :" + response);
            }
            socket.shutdownOutput();

            float mean = Float.parseFloat(in.readLine());
            System.out.println("--> Mean response to (" + TID + ") : " + mean);
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {

        }
    }
}
public class TestServerMult {
    // O objetivo deste teste é apenas para testar se a concorrência ao Sum está
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
