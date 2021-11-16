import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Arithmetic {
    private double state;
    private int actions;
    private Lock l = new ReentrantLock();

    public Arithmetic() {
        this.state = 0;
        this.actions = 0;
    }

    public double add(int quantity) {
        l.lock();
        this.state += quantity;
        this.actions++;
        try {
            return this.state;
        } finally {
            l.unlock();
        }
    }

    public double divide(int quantity) {
        l.lock();
        this.state /= quantity;
        this.actions++;
        try {
            return this.state;
        } finally {
            l.unlock();
        }
    }

    public double multiply(int quantity) {
        l.lock();
        this.state *= quantity;
        this.actions++;
        try {
            return this.state;
        } finally {
            l.unlock();
        }
    }

    public double mean() {
        l.lock();
        try {
            return (this.state/this.actions);
        } finally {
            l.unlock();
        }
    }
}
