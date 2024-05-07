package Task6;

class IncrementThread extends Thread {
    private final CounterBase counter;

    public IncrementThread(CounterBase counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 100000; i++) {
            counter.increment();
        }
    }
}
