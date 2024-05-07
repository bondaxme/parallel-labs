package Task4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public static final int BALL_COUNT = 100;
    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = "
                + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new Thread(() -> {

                    Ball[] balls = new Ball[BALL_COUNT];
                    for (int i = 0; i < BALL_COUNT; i++) {
                        Ball b = new Ball(canvas, new Color((int)(Math.random() * 0x1000000)));
                        canvas.add(b);
                        balls[i] = b;
                    }

                    for (Ball ball : balls) {
                        BallThread ballThread = new BallThread(ball);
                        ballThread.start();
                        try {
                            ballThread.join();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                }).start();
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}