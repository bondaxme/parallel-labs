package Task6;

class SyncMethodCounter extends CounterBase {
    private int value = 0;

    public synchronized void increment() {
        value++;
    }

    public synchronized void decrement() {
        value--;
    }

    public synchronized int getValue() {
        return value;
    }
}

