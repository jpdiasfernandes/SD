import java.util.Random;

import static java.lang.Thread.sleep;

class TaskAndWait implements Runnable {
    private Barrier b;

    public TaskAndWait(Barrier barrier) {
        b = barrier;
    }
    public void run() {
        final int maxSec = 15;
        Random rnd = new Random();

        try {
            int time = rnd.nextInt(maxSec) * 1000;
            sleep(time);
            System.out.println("Estou pronto");
            b.await();
            System.out.println("Estou livre");
        } catch (InterruptedException e) {

        }
    }
}
public class TestBarrier {
    public static void main(String[] args) throws InterruptedException{
        final int N = 10;
        final int T = 5;
        Barrier b = new Barrier(N);

        Thread[] threads = new Thread[T];
        for (int i = 0; i < T; i++) {
            threads[i] = new Thread(new TaskAndWait(b));
        }

        for (int i = 0; i < T; i++) {
            threads[i].start();
        }

        for (int i = 0; i < T; i++)
            threads[i].join();


    }
}
