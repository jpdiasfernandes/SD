import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier2 {
    private int N;
    private int n;
    private boolean aberto;
    private final Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    public Barrier2(int N) {
        this.N = N;
        this.n = N;
        this.aberto = false;
    }

    public void await() throws InterruptedException {
        l.lock();
        while(aberto)
            c.await();

        if (this.n-- == 1) {
            aberto = true;
            c.signalAll();
        }

        while (!aberto)
            c.await();

        if(++this.n == this.N) {
            aberto = false;
            c.signalAll();
        }

        l.unlock();
    }
}
