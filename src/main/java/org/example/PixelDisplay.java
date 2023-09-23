package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PixelDisplay extends JPanel {
    private short[][] pixelArray;
    private int zoomLevel = 1;
    private int xOffset = 0;
    private int yOffset = 0;
    private int prevDragX, prevDragY;
    private Ant ant = new Ant();

    public PixelDisplay(short[][] pixelArray) {
        this.pixelArray = pixelArray;
        setPreferredSize(new Dimension(pixelArray[0].length, pixelArray.length));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevDragX = e.getX();
                prevDragY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - prevDragX;
                int dy = e.getY() - prevDragY;
                xOffset += dx;
                yOffset += dy;
                //repaint();
                prevDragX = e.getX();
                prevDragY = e.getY();
            }
        });

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            zoomLevel -= notches;
            zoomLevel = Math.max(1, zoomLevel);
            //repaint();
        });

        new Thread(() -> {
            long lastime = System.nanoTime();
            double ns = 1000000000 / 500;
            double delta = 0;


            while (true) {
                long now = System.nanoTime();
                delta += (now - lastime) / ns;
                lastime = now;

                if (delta >= 1) {
                    this.pixelArray = ant.getGrid();
                    delta--;
                }
            }
        }).start();

        new Thread(() -> {while (true) repaint();}).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int pixelSize = 10;
        int height = pixelArray.length;
        int width = pixelArray[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor;
                int colorValue = pixelArray[y][x];
                if (colorValue == 0) {
                    pixelColor = new Color(0, 0, 0);
                } else if (colorValue == 1) {
                    pixelColor = new Color(255, 255, 255);
                } else if (colorValue == 2) {
                    pixelColor = new Color(255, 0, 0);
                } else {
                    pixelColor = new Color(0, 0, 0);
                }

                g.setColor(pixelColor);

                int scaledX = x * pixelSize * zoomLevel + xOffset;
                int scaledY = y * pixelSize * zoomLevel + yOffset;
                g.fillRect(scaledX, scaledY, pixelSize * zoomLevel, pixelSize * zoomLevel);
            }
        }
    }

    public static void main(String[] args) {
        short[][] initialPixelData = new short[100][100];
        JFrame frame = new JFrame("Pixel Display");
        PixelDisplay pixelDisplay = new PixelDisplay(initialPixelData);
        frame.add(pixelDisplay);
        frame.pack();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
