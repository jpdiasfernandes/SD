
public class Deposit implements Runnable {
    private Bank bank;

    public Deposit(Bank bank) {
        this.bank = bank;
    }

    public void run() {
        this.bank.deposit(100);
    }
}
