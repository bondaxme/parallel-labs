package Task6;

public class Counter extends CounterBase {
    private int count;

    public Counter() {
        this.count = 0;
    }

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int getValue() {
        return count;
    }
}
