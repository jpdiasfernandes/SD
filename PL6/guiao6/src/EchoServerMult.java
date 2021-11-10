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
    private Sum s;

    public ServerResponse(Socket socket, Sum s) {
        this.socket = socket;
        this.s = s;

    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    Integer x = Integer.parseInt(line);
                    int sum = s.add(x);
                    out.println("" + sum);
                    out.flush();
                } catch (NumberFormatException e) {
                    out.println("Error in parsing");
                    out.flush();
                }

            }

            socket.shutdownInput();
            double mean = s.mean();
            out.println("" + mean);
            out.flush();

            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {

        }
    }
}
public class EchoServerMult {
    Lock l = new ReentrantLock();

    public static void main(String[] args) {
        Sum s = new Sum();
        try {
            ServerSocket ss = new ServerSocket(12345);


            while (true) {
                Socket socket = ss.accept();
                Thread connection = new Thread(new ServerResponse(socket,s));
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
