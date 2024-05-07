package Task5;

class SymbolPrinter implements Runnable {
    private final Object lock;
    private final char symbol;

    public SymbolPrinter(Object lock, char symbol) {
        this.lock = lock;
        this.symbol = symbol;
    }

    @Override
    public void run() {
        synchronized (lock) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    System.out.print(symbol);
                    lock.notify();
                    try {
                        if (j < 99) lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println();
            }
        }
    }
}