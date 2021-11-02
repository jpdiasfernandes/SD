import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Agreement {
    private Barrier b;
    private Lock l = new ReentrantLock();
    private Condition c;
    private int N;
    private int n;
    private int max;
    private boolean aberta;

    public Agreement(int N) {
        this.b = new Barrier(N);
        this.c = l.newCondition();
        this.N = N;
        this.n = N;
        this.max = Integer.MIN_VALUE;
        this.aberta = false;
    }

    public int propose(int choice) throws InterruptedException {
        l.lock();
        while (aberta) {
            c.await();
        }
        if (choice > max) max = choice;
        if (this.n-- == 1)
            aberta = true;

        l.unlock();
        b.await();
        l.lock();

        try {
            return max;
        } finally {
            if (++this.n == this.N) {
                aberta = false;
                c.signalAll();
                max = Integer.MIN_VALUE;
            }
            l.unlock();
        }

    }
}
