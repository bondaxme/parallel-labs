package Task6;

class DecrementThread extends Thread {
    private final CounterBase counter;

    public DecrementThread(CounterBase counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 100000; i++) {
            counter.decrement();
        }
    }
}
