import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Bank {

    public class Account {
        private int balance;
        private Lock l = new ReentrantLock();
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

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    private ReadWriteLock l = new ReentrantReadWriteLock();


    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        // wl = write lock
        Lock wl = this.l.writeLock();
        wl.lock();
        int id = nextId;
        nextId += 1;
        map.put(id, c);
        wl.unlock();
        return id;
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        // rl = read lock
        Lock rl = this.l.readLock();

        rl.lock();
        //2PL
        Account c = map.remove(id);
        try {
            if (c == null)
                return 0;
            c.l.lock();
        } finally {
            rl.unlock();
        }

        try {
            return c.balance();
        } finally {
            c.l.unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Lock rl = this.l.readLock();

        rl.lock();
        Account c = map.get(id);
        try {
            if (c == null)
                return 0;
            c.l.lock();
        } finally {
            rl.unlock();
        }

        try {
            return c.balance();
        } finally {
            c.l.unlock();
        }

    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Lock rl = this.l.readLock();

        rl.lock();
        Account c = map.get(id);
        try {
            if (c == null)
                return false;
            c.l.lock();
        } finally {
            rl.unlock();
        }

        try{
            return c.deposit(value);
        } finally {
            c.l.unlock();
        }

    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Lock rl = this.l.readLock();

        rl.lock();
        Account c = map.get(id);

        try {
            if (c == null)
                return false;
            c.l.lock();
        } finally {
           rl.unlock();
        }

        try {
            return c.withdraw(value);
        } finally {
            c.l.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        Lock rl = this.l.readLock();

        rl.lock();
        cfrom = map.get(from);
        cto = map.get(to);
        try {
            if (cfrom == null || cto ==  null)
                return false;
            cfrom.l.lock();
            cto.l.lock();
        } finally {
            rl.unlock();
        }

        try {
            return cfrom.withdraw(value) && cto.deposit(value);
        } finally {
            cto.l.unlock();
            cfrom.l.unlock();
        }

    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        List<Account> tmp = new ArrayList<>();
        Lock rl = this.l.readLock();

        rl.lock();
        try {
            for (int i : ids) {
                Account c = map.get(i);
                if (c == null)
                    return 0;
                tmp.add(c);
                c.l.lock();
            }
        } finally {
            rl.unlock();
        }


        for (Account c : tmp) {
            total += c.balance();
            c.l.unlock();
        }
        //este total vai ser equivalente a um snapshot antigo, pode não ser
        // o snapshot realista no final da execução do método.
        // este total vai ser o total aquando da execução do método
        // se quisesse que fosse igual durante toda a execução do método era
        // preciso dar só unlock no final da soma

        return total;

  }

}
