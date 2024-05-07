package Task6;

class SyncBlockCounter extends CounterBase {
    private int value = 0;

    public void increment() {
        synchronized(this) {
            value++;
        }
    }

    public void decrement() {
        synchronized(this) {
            value--;
        }
    }

    public int getValue() {
        synchronized(this) {
            return value;
        }
    }
}

