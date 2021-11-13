import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerSingle {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);


            while (true) {
                int n = 0;
                int sum = 0;

                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                while ((line = in.readLine()) != null) {

                    Integer x = Integer.parseInt(line);
                    sum += x;
                    n++;
                    out.println(sum);
                    out.flush();
                }

                socket.shutdownInput();
                double mean = sum / n;
                out.println(mean);
                out.flush();

                socket.shutdownOutput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
