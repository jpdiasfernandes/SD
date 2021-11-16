import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class EchoClientSingle {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            SenderReceiverClient sr = new SenderReceiverClient(socket.getOutputStream(), socket.getInputStream());

            String userInput;
            while (run) {
                run = sr.send();
            }


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
