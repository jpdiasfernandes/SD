import java.util.Random;

class Propose implements Runnable {
    private Agreement a;
    private Random rnd;
    private int maxNumber;

    public Propose(Agreement a) {
        this.a = a;
        this.rnd = new Random();
        this.maxNumber = 1000;
    }

    public void run() {
        int n = rnd.nextInt(maxNumber);
        try {
            System.out.println(a.propose(n));
        } catch (InterruptedException e) {

        }

    }
}
public class TestAgreement {
    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        int T = 25;
        Agreement a = new Agreement(N);
        Thread[] threads = new Thread[T];

        for (int i = 0; i < T; i++)
            threads[i] = new Thread(new Propose(a));

        for (int i = 0; i < T; i++)
            threads[i].start();


        for (int i = 0; i < T; i++)
            threads[i].join();
    }

}
