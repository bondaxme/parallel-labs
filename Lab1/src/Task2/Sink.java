package Task2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

class Sink {
    private int ballsInSink = 0;
    private final Component canvas;
    private static final int XSIZE = 50;
    private static final int YSIZE = 50;
    private final int width = 125;
    private final int height = 125;

    public Sink(Component canvas) {
        this.canvas = canvas;
    }

    public void incrementBallsInSink() {
        ballsInSink++;
    }

    public int getBallsInSink() {
        return ballsInSink;
    }


    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(width, height, XSIZE, YSIZE));
        g2.setColor(Color.WHITE);
    }

    public boolean checkCollision(int x, int y) {
        return (x >= width && x <= width + XSIZE &&
                y >= height && y <= height + YSIZE);
    }
}