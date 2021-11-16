import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SenderReceiverClient {
    private OutputStream out;
    private InputStream in;
    private final Scanner sc = new Scanner(System.in);

    public SenderReceiverClient(OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
    }


    //      Client sender protocol
    //        1 byte     4 bytes
    //       -----------------------
    //      | Opcode |  Quantity   |
    //       -----------------------

    //      Server sender protocol
    //          8 bytes
    //      ---------------
    //      |  Quantity   |
    //      ---------------

    public boolean send() {
        ByteBuffer sender = ByteBuffer.allocate(5);
        boolean r = true;
        System.out.println("Selecione a operação: \n" +
                "1- Soma " +
                "2- Divisão " +
                "3- Multiplicação "+
                "(-1)- Sair");
        int option = -1;
        try {
            option = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Inisra um inteiro");
        }

        if (option > 0 && option < 4) {
            byte opcode = (byte) option;
            System.out.println("Valor operando:");
            try {
                int quantity = sc.nextInt();
                sender.put(opcode);
                sender.putInt(quantity);
                out.write(sender.array());
                out.flush();

                double response = receive();
                System.out.println("Estado atual: " + response);
            } catch (InputMismatchException | IOException e) {
                System.out.println("Insira um Inteiro");
            }
        } else if (option == -1) {
            r = false;
        } else {
            System.out.println("Insira um valor válido");
        }

        // Só queremos que devolva falso se o cliente explicitamente
        // Quiser parar o programa.
        return r;
    }

    public void sendGeneric(byte opcode, int quantity) throws IOException {
        ByteBuffer sender = ByteBuffer.allocate(5);
        sender.put(opcode);
        sender.putInt(quantity);
        out.write(sender.array());
        out.flush();
    }

    public double receive() throws IOException {
        ByteBuffer receiver = ByteBuffer.allocate(8);
        byte [] b = new byte[8];
        in.read(b);
        receiver.clear();
        receiver.put(b);
        receiver.position(0);
        double r = receiver.getDouble();
        return r;
    }
    
}
