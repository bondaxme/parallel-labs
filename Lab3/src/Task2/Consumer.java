package Task2;

import java.util.Random;

public class Consumer implements Runnable {
    private Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        int count = 0;
        for (int message = drop.take();
             message != 0;
             message = drop.take()) {

            System.out.format("MESSAGE RECEIVED: %s%n", message);
            count++;
            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {}
        }
        System.out.format("Total messages received: %s%n", count);
    }
}
