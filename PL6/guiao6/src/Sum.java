import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sum {
    private int sum;
    private int n;
    private Lock l = new ReentrantLock();

    public Sum() {
        this.sum = 0;
        this.n = 0;
    }

    public int add(int quantity) {
        l.lock();
        this.sum += quantity;
        this.n++;
        try {
            return this.sum;
        } finally {
            l.unlock();
        }
    }

    public double mean() {
        l.lock();
        try {
            return (this.sum / this.n);
        } finally {
            l.unlock();
        }
    }
}
