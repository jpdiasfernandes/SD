import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private int N;
    private int current;
    private int state;
    private final Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    public Barrier(int N) {
        this.N = N;
        this.current = N;
        this.state = 0;
    }

    public void await() throws InterruptedException {
        l.lock();
        int position = current--;
        int myState = state;

        if (position == 1) {
            current = N;
            state++;
            c.signalAll();
        }
        else {
            while(myState == state) {
                c.await();
            }
        }

        l.unlock();
    }
}
