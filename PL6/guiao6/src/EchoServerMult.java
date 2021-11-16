import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ServerResponse implements Runnable {

    private Socket socket;
    private SenderReceiverServer srs;
    private Arithmetic a;

    public ServerResponse(Socket socket, Arithmetic a) throws IOException{
        this.socket = socket;
        this.a = a;
        this.srs = new SenderReceiverServer(socket.getOutputStream(), socket.getInputStream());
    }

    public void run() {
        try {
            boolean run = true;
            while (srs.read()) {
                double state = srs.receive(a);
                srs.send(state);
            }

            socket.shutdownInput();
            double mean = a.mean();
            srs.send(mean);

            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {

        }
    }
}
public class EchoServerMult {
    Lock l = new ReentrantLock();

    public static void main(String[] args) {
        Arithmetic a = new Arithmetic();
        try {
            ServerSocket ss = new ServerSocket(12345);


            while (true) {
                Socket socket = ss.accept();
                Thread connection = new Thread(new ServerResponse(socket,a));
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
