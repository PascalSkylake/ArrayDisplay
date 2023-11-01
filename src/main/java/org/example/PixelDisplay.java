package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

public class PixelDisplay extends JPanel {
    private Color[][] pixelArray;
    private int zoomLevel = 1;
    private int xOffset = 0;
    private int yOffset = 0;
    private int prevDragX, prevDragY;
    private static Random random;
    int counter = 0;
    private float speedFactor = 1f;
    private float currentPosition = 0f;
    private float sections = 2f;

    public PixelDisplay(Color[][] pixelArrayIn) {
        pixelArray = pixelArrayIn;


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
            double ns = 1000000000 / 100;
            double delta = 0;


            while (true) {
                long now = System.nanoTime();
                delta += (now - lastime) / ns;
                lastime = now;

                if (delta >= 1) {
                    update();
                    counter++;
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
                Color pixelColor = pixelArray[y][x];

                /*
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
                 */


                g.setColor(pixelColor);

                int scaledX = x * pixelSize * zoomLevel + xOffset;
                int scaledY = y * pixelSize * zoomLevel + yOffset;
                g.fillRect(scaledX, scaledY, pixelSize * zoomLevel, pixelSize * zoomLevel);
            }
        }
    }

    public static void main(String[] args) {
        random = new Random();
        Color[][] initialPixelData = new Color[1][100];
        for (int i = 0; i < initialPixelData.length; i++) {
            for (int j = 0; j < initialPixelData[i].length; j++) {
                initialPixelData[i][j] = Color.getHSBColor(random.nextFloat(), 1f, 1f);
            }
        }

        JFrame frame = new JFrame("Pixel Display");
        PixelDisplay pixelDisplay = new PixelDisplay(initialPixelData);
        frame.add(pixelDisplay);
        frame.pack();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        pixelDisplay.getHSB(new Color(100, 50, 215));

        Scanner kb = new Scanner(System.in);
        while (true) {
            float next = kb.nextFloat();
            pixelDisplay.speedFactor = next;
        }
    }

    public void setPixelHSB(int i, float h, float s, float b) {
        pixelArray[0][i] = Color.getHSBColor(h, s, b);
    }

    public void update2() {
        currentPosition += speedFactor;

        for (int i = 0; i < pixelArray[0].length; i++) {
            float adjustedPosition = (i + currentPosition) % pixelArray[0].length;

            if (adjustedPosition < 0) {
                adjustedPosition =+ pixelArray[0].length;
            }

            float hue = ((adjustedPosition / pixelArray[0].length) / (1f / sections)) % 1;


            setPixelHSB(i, hue, 1f, 1f);
        }
    }

    public void update() {
        //speedFactor = 3 * (float) Math.sin((Math.PI / 500) * (counter % 100)) + 2;
        Color[] temp = pixelArray[0];
        Color[] next = new Color[pixelArray[0].length];
        if (next[0] == null) {
            for (int i = 0; i < next.length; i++) {
                next[i] = Color.getHSBColor(random.nextFloat(), 1f, 1f);
            }
        }

        for (int i = 0; i < pixelArray[0].length; i++) {
            float[] thsb = getHSB(temp[i]);
            float[] nhsb = getHSB(next[i]);

            if (Math.abs(thsb[0] - nhsb[0]) < 0.01 && Math.abs(thsb[1] - nhsb[1]) < 0.01 && Math.abs(thsb[2] - nhsb[2]) < 0.01) {
                next[i] = Color.getHSBColor(random.nextFloat(), 1f, 1f);
            } else if (Math.abs(Math.abs(thsb[0] - nhsb[0])) >= 0.01) {
                if (nhsb[0] - thsb[0] <= 0) {
                    thsb[0] = thsb[0] - (nhsb[0] - thsb[0]) / (1 / (speedFactor / 50));
                } else {
                    thsb[0] = thsb[0] + (nhsb[0] - thsb[0]) / (1 / (speedFactor / 50));
                }

            }

            temp[i] = Color.getHSBColor(thsb[0], 1f, 1f);
        }

    }

    public void update3() {
        for (int i = 0; i < pixelArray[0].length; i++) {
            //setPixelHSB(i, Math.sin());
        }
    }

    private float[] getHSB(Color c) {
        float[] rgb = new float[3];
        rgb = c.getRGBColorComponents(rgb);

        rgb[0] /= 255f;
        rgb[1] /= 255f;
        rgb[2] /= 255f;

        float cmax = Math.max(rgb[0], Math.max(rgb[1], rgb[2])); // maximum of r, g, b
        float cmin = Math.min(rgb[0], Math.min(rgb[1], rgb[2])); // minimum of r, g, b
        float diff = cmax - cmin; // diff of cmax and cmin.
        float h = -1, s = -1;

        // if cmax and cmax are equal then h = 0
        if (cmax == cmin) {
            h = 0;
        }

        // if cmax equal r then compute h
        else if (cmax == rgb[0]) {
            h = (60 * ((rgb[1] - rgb[2]) / diff) + 360) % 360;
        }

        // if cmax equal g then compute h
        else if (cmax == rgb[1]) {
            h = (60 * ((rgb[2] - rgb[0]) / diff) + 120) % 360;
        }

        // if cmax equal b then compute h
        else if (cmax == rgb[2]) {
            h = (60 * ((rgb[0] - rgb[1]) / diff) + 240) % 360;
        }

        // if cmax equal zero
        if (cmax == 0) {
            s = 0;
        } else {
            s = (diff / cmax) * 100;
        }

        h /= 360;
        s /= 100;
        float b = cmax * 255;


        return new float[] {h, s, b};
    }
}
