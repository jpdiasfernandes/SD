import java.awt.image.BaseMultiResolutionImage;
import java.util.Random;

class Closer implements Runnable  {

    private Bank b;
    final int accs;
    public Closer(Bank b, int accs) {
        this.b = b;
        this.accs = accs;
    }

    public void run() {
        final int closes = 50;
        Random rand = new Random();
        for (int i = 0; i < closes; i++) {
            System.out.println(b.balance(i));
            b.closeAccount(i);
        }
    }
}

class Transfer implements Runnable {
    private Bank b;
    final int accs;
    public Transfer(Bank b,int accs) {
        this.b = b;
        this.accs = accs;
    }

    public void run() {
        final int moves=1000000;
        int from, to;
        Random rand = new Random();

        for (int i = 0; i < moves; i++) {
            from = rand.nextInt(100);
            while ((to=rand.nextInt(100)) == from);
            b.transfer(from, to, 10);
        }
    }
}

public class Test {
    public static void main(String[] args) throws InterruptedException {
            Bank b = new Bank();
            final int accs = 10000;
            for (int i = 0; i < accs; i++)
                b.createAccount(10);

        int ids[] = new int[accs];
        for (int i =0; i < accs; i++)
            ids[i] = i;


        System.out.println(b.totalBalance(ids));
        //Thread do cliente 1
            Thread c1 = new Thread(new Transfer(b, accs));
            Thread c2 = new Thread(new Transfer(b, accs));
            Thread c3 = new Thread(new Closer(b, accs));

            c1.start();
            c2.start();
            c3.start();
            c1.join();
            c2.join();
            c3.join();

            int id[] = new int[accs - 100];
            for (int i = 0; i < accs - 100; i++)
                id[i] = i + 100;

            System.out.println(b.totalBalance(id));


    }



}

