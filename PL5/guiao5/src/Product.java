import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Product {
    int quantity = 0;
    Condition c;

    public Product(Condition c) {
        this.c = c;
    }


}
