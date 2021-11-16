import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SenderReceiverServer {
    private OutputStream out;
    private InputStream in;
    //probably not the best solution
    private byte [] bReceiver = new byte[5];

    public SenderReceiverServer(OutputStream out, InputStream in) {
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

    public void send(double state) throws IOException {
        ByteBuffer sender = ByteBuffer.allocate(8);
        sender.clear();
        sender.putDouble(state);
        out.write(sender.array());
        out.flush();
    }

    public boolean read() throws IOException {
        return (in.read(this.bReceiver)) != -1;
    }
    //Probably not the best solution to the problem
    public double receive(Arithmetic a) throws IOException {
        ByteBuffer receiver = ByteBuffer.allocate(5);
        receiver.clear();
        receiver.put(this.bReceiver);
        receiver.position(0);
        int opcode = (int) receiver.get(0);
        receiver.position(1);
        int quantity = receiver.getInt();

        switch(opcode) {
            case 1:
                return a.add(quantity);
            case 2:
                return a.divide(quantity);
            case 3:
                return a.multiply(quantity);
            default:
                throw new IOException();
        }
    }
}