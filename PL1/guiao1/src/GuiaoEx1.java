
import java.lang.Thread;

public class GuiaoEx1 {
    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        Thread[] ths = new Thread[N];
        for (int i = 0; i < N; i++)
            ths[i] = new Thread(new Increment());

        for (int i = 0; i < N; i++)
            ths[i].start();

        for (int i = 0; i < N; i++)
            ths[i].join();

        System.out.println("fim!");
    }
}