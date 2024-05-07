package Task2;

import java.util.Random;

public class Producer implements Runnable {
    private Drop drop;
    private int size;

    public Producer(Drop drop, int size) {
        this.drop = drop;
        this.size = size;
    }

    public void run() {
        Random random = new Random();
        int[] importantInfo = new int[size];
        for (int i = 0; i < importantInfo.length; i++) {
            importantInfo[i] = random.nextInt(100) + 1;
        }

        for (int i = 0;
             i < importantInfo.length;
             i++) {
            drop.put(importantInfo[i]);
            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {}
        }
        drop.put(0);
    }
}
