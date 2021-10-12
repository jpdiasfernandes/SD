package src;

public class GuiaoEx2 {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        int N = 100000;
        Thread[] ths = new Thread[N];

        for (int i = 0; i < N; i++) {
            ths[i] = new Thread(new Deposit(bank));
        }

        for (int i = 0; i < N; i++)
            ths[i].start();

        for(int i = 0; i < N; i++)
            ths[i].join();

        System.out.println(bank.balance());
    }
}
