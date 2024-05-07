package Task2;

public class BallThread extends Thread {
    private final Ball b;
    private final Sink sink;
    private final BallCanvas canvas;



    public BallThread(Ball ball, Sink sink, BallCanvas canvas){
        b = ball;
        this.sink = sink;
        this.canvas = canvas;
    }

    @Override
    public void run(){
        try{
            while (true) {
                b.move();
                if(!canvas.containsBall(b)){
                    sink.incrementBallsInSink();
                    canvas.updateSinkCounter();
                    break;
                }
                System.out.println("Thread name = "
                        + Thread.currentThread().getName());
                Thread.sleep(5);
            }
        } catch(InterruptedException ex){
            System.out.println("Thread was interrupted");
        }
    }

}