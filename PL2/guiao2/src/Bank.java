package src;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

class Bank {

  private static class Account {
    Lock accLock = new ReentrantLock();
    private int balance;
    Account(int balance) { this.balance = balance; }
    int balance() { return balance; }
    boolean deposit(int value) {
      balance += value;
      return true;
    }
    boolean withdraw(int value) {
      if (value > balance)
        return false;
      balance -= value;
      return true;
    }
  }

  // src.Bank slots and vector of accounts
  private Lock lock = new ReentrantLock();
  private int slots;
  private Account[] av;

  public Bank(int n)
  {
    slots=n;
    av=new Account[slots];
    for (int i=0; i<slots; i++) {
      av[i]=new Account(0);
    }

  }

  // Account balance
  public int balance(int id) {
      if (id < 0 || id >= slots)
      return 0;

      Account ac = getAccount(id);
      try {
        ac.accLock.lock();
        return ac.balance();
      } finally {
        ac.accLock.unlock();
      }

  }

  // Deposit

  boolean deposit(int id, int value) {
    if (id < 0 || id >= slots)
      return false;

    Account ac = getAccount(id);
    try {
        ac.accLock.lock();
       return ac.deposit(value);
    } finally {
        ac.accLock.unlock();
    }
  }

  // Withdraw; fails if no such account or insufficient balance
  public boolean withdraw(int id, int value) {

    if (id < 0 || id >= slots)
      return false;
    Account ac = getAccount(id);
    try {
      ac.accLock.lock();
      return ac.withdraw(value);
    } finally {
      ac.accLock.unlock();
    }
  }

  // Transfer; fails if no such accounts or insufficient funds
  public boolean transfer(int from, int to, int value) {
    if (!validAccount(from) || !validAccount(to)) return false;

      this.lock.lock();

      Account f = getAccount(from);
      Account t = getAccount(to);

      f.accLock.lock();
      t.accLock.lock();

      this.lock.unlock();



      boolean with = withdraw(from, value);
      f.accLock.unlock();
      boolean dep = deposit(to, value);
      t.accLock.unlock();

      return dep && with;
  }

  public int totalBalance() {
    int r = 0;

      for (int i = 0; i < slots; i++)
        getAccount(i).accLock.lock();

      for (int i = 0; i < slots; i++) {
        r += this.balance(i);
        getAccount(i).accLock.unlock();
      }

      return r;
  }

  private boolean validAccount(int id) {
    return !(id < 0 || id >= slots);
  }

  private Account getAccount(int id) {
      // Não sei se se deve bloquear o banco
    return av[id];
  }

}
