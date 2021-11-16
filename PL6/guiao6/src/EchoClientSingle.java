import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class EchoClientSingle {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        try {
            Socket socket = new Socket("localhost", 12345);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            SenderReceiverClient sr = new SenderReceiverClient(out, in);

            while (run)
                run = sr.send();


            socket.shutdownOutput();
            double mean = sr.receive();
            System.out.println("Server response: " + mean);
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
