package Task6;



public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        runThreads(counter);

        LockCounter lockCounter = new LockCounter();
        runThreads(lockCounter);

        SyncBlockCounter syncBlockCounter = new SyncBlockCounter();
        runThreads(syncBlockCounter);

        SyncMethodCounter syncMethodCounter = new SyncMethodCounter();
        runThreads(syncMethodCounter);
    }

    private static void runThreads(CounterBase counter) {
        IncrementThread incrementThread = new IncrementThread(counter);
        DecrementThread decrementThread = new DecrementThread(counter);

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter.getClass().getSimpleName() + " value: " + counter.getValue());
    }

}
