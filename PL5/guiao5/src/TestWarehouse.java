import java.util.*;

class Suplier implements Runnable {
    private String item;
    private Warehouse h;
    private final int quantity = 5;
    public Suplier(Warehouse h, String item) {
        this.h = h;
        this.item = item;
    }

    public void run() {
        h.supply(item, quantity);
        System.out.println("Supplied " + quantity + ".");
    }
}

class Consumer implements Runnable {
    private Set<String> option;
    private Warehouse h;
    public Consumer(Warehouse h, Set<String> item) {
        this.h = h;
        this.option = item;
    }

    public void run() {
        try {
            h.consumeNoStarve(option);
            System.out.println("Consumed " + option.size() + ".");
        } catch (InterruptedException e) {

        }
    }
}


public class TestWarehouse {
    public static void main(String[] args) throws InterruptedException {
        final int C = 15;
        final int S = 10;
        Random rnd = new Random();
        Thread consumers[] = new Thread[C];
        Thread supliers[] = new Thread[S];

        Warehouse h = new Warehouse();

        List<Set<String>> items = new ArrayList<>();
        Set<String> op1 = new HashSet<>();
        Set<String> op2 = new HashSet<>();
        Set<String> op3 = new HashSet<>();

        List<String> suplierItems = new ArrayList<>();

        op1.add("bicicleta");
        op1.add("rolo");
        op1.add("televisão");
        op2.add("impressora");
        op2.add("cadeira");
        op3.add("papel");

        suplierItems.add("bicicleta");
        suplierItems.add("rolo");
        suplierItems.add("televisão");
        suplierItems.add("impressora");
        suplierItems.add("cadeira");
        suplierItems.add("papel");

        items.add(op1); items.add(op2); items.add(op3);

        int i;
        // fazer um maximo depois, estou a assumir que os consumer vão ser mais
        for (i = 0; i < C; i++) {
            if (i < S) {
                String supplying = suplierItems.get(i % suplierItems.size());
                supliers[i] = new Thread(new Suplier(h, supplying));
            }
            if (i < C) {
                Set<String> option = items.get(i % items.size());
                consumers[i] = new Thread(new Consumer(h, option));
            }
        }

        for (i = 0; i < C; i++) {
            if (i < S) supliers[i].start();
            if (i < C) consumers[i].start();
        }

        for (i = 0; i < C; i++) {
            if (i < S) supliers[i].join();
            if (i < C) consumers[i].join();
        }

    }
}
