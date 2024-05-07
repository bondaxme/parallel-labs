package Task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public static final int BALL_COUNT = 1000;
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
        JButton buttonExperiment = new JButton("Experiment");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 1; i <= BALL_COUNT; i++) {
                    Color color = (i % 2 != 0) ? Color.RED : Color.BLUE;
                    Ball b = new Ball(canvas, color);
                    canvas.add(b);

                    BallThread thread = new BallThread(b);
                    if (i % 2 != 0) {
                        thread.setPriority(Thread.MAX_PRIORITY);
                    } else {
                        thread.setPriority(Thread.MIN_PRIORITY);
                    }
                    thread.start();
                    System.out.println("Thread name = " +
                            thread.getName());
                }
            }
        });

        buttonExperiment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i <= BALL_COUNT; i++) {
                    Color color = (i == 1) ? Color.RED : Color.BLUE;
                    Ball b = new Ball(canvas, color, 0, 0);
                    canvas.add(b);

                    BallThread thread = new BallThread(b);
                    if (i == 1) {
                        thread.setPriority(Thread.MAX_PRIORITY);
                    } else {
                        thread.setPriority(Thread.MIN_PRIORITY);
                    }
                    thread.start();
                    System.out.println("Thread name = " +
                            thread.getName());
                }
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
        buttonPanel.add(buttonExperiment);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}