import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {
  private Map<String, Product> map =  new HashMap<String, Product>();
  Lock l = new ReentrantLock();



  private Product get(String item) {
    l.lock();
    Product p = map.get(item);
    if (p == null) {
      p = new Product(l.newCondition());
      map.put(item, p);
    }

    try {
      return p;
    } finally {
      l.unlock();
    }
  }

  public void supply(String item, int quantity) {
    l.lock();
    Product p = get(item);
    p.quantity += quantity;
    p.c.signalAll();
    l.unlock();
  }

  public void consumeGreedy(Set<String> items) throws InterruptedException {
    l.lock();

    for (String s : items) {
      Product p = get(s);
      while(p.quantity == 0)
        p.c.await();
      p.quantity--;
    }

    l.unlock();
  }

  public void consumeCoop(Set<String> items) throws InterruptedException {
      l.lock();

      int i = 0;
      List<String> aux = new ArrayList<>(items);
      while (anyEmpty(items)) {
        for (String s : items) {
          Product p = get(s);
          while (p.quantity == 0) p.c.await();
        }
      }
      // Outra forma de fazer
      //for (i = 0; i < aux.size(); i++) {
      //      Product p = get(aux.get(i));
      //      while (p.quantity == 0){
      //        i = 0;
      //        p.c.await();
      //      }
      //}

      for(String s : items) {
        Product p = get(s);
        p.quantity--;
      }
      l.unlock();
  }

  public void consumeNoStarve(Set<String> items) throws InterruptedException {

    l.lock();
    int tries = 0;
    while (anyEmpty(items) && tries < 5) {
      for (String s : items) {
        Product p = get(s);
        if (p.quantity == 0) {
          p.c.await();
          // tem o problema de se houver um spurious wake,conta como
          // uma tentativa. Mas para o tipo de problema que se pretende
          // resolver não é necessário esse nível de exigência.
          tries++;
        }
      }
    }
      if (tries == 5) consumeGreedy(items);
      else {
        for (String s : items) {
          Product p = get(s);
          p.quantity--;
        }
      }

      l.unlock();
  }

  private boolean anyEmpty(Set<String> items) {
    l.lock();
    try {
      for (String s : items) {
        if (get(s).quantity == 0) return true;
      }
      return false;
    } finally {
      l.unlock();
    }

  }
}





