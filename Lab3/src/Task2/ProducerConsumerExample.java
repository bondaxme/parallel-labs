package Task2;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        int size = 100;
        Drop drop = new Drop();
        (new Thread(new Producer(drop, size))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
