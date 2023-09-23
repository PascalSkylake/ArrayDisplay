package org.example;

import java.awt.*;

public class Ant {
    private short[][] grid;
    private Point antPose;
    private int antDirection = 1;
    private short antPoseValue = 0;

    public Ant() {
        grid = new short[300][300];
        grid[50][50] = 2;
        antPose = new Point(50, 50);
    }

    public short[][] getGrid() {
        operate2();
        return this.grid;
    }

    public void operate2() {
        if (antPoseValue == 0) {
            antDirection -= 1;
            if (Math.abs(antDirection) % 4 == 0) {
                antPoseValue = grid[antPose.x - 1][antPose.y];
                grid[antPose.x][antPose.y] = 1;
                antPose.setLocation(antPose.x - 1, antPose.y);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 1) {
                antPoseValue = grid[antPose.x][antPose.y + 1];
                grid[antPose.x][antPose.y] = 1;
                antPose.setLocation(antPose.x, antPose.y + 1);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 2) {
                antPoseValue = grid[antPose.x + 1][antPose.y];
                grid[antPose.x][antPose.y] = 1;
                antPose.setLocation(antPose.x + 1, antPose.y);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 3) {
                antPoseValue = grid[antPose.x][antPose.y - 1];
                grid[antPose.x][antPose.y] = 1;
                antPose.setLocation(antPose.x, antPose.y - 1);
                grid[antPose.x][antPose.y] = 2;
            }
        } else {
            antDirection += 1;
            if (Math.abs(antDirection) % 4 == 0) {
                antPoseValue = grid[antPose.x - 1][antPose.y];
                grid[antPose.x][antPose.y] = 0;
                antPose.setLocation(antPose.x - 1, antPose.y);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 1) {
                antPoseValue = grid[antPose.x][antPose.y + 1];
                grid[antPose.x][antPose.y] = 0;
                antPose.setLocation(antPose.x, antPose.y + 1);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 2) {
                antPoseValue = grid[antPose.x + 1][antPose.y];
                grid[antPose.x][antPose.y] = 0;
                antPose.setLocation(antPose.x + 1, antPose.y);
                grid[antPose.x][antPose.y] = 2;
            } else if (Math.abs(antDirection) % 4 == 3) {
                antPoseValue = grid[antPose.x][antPose.y - 1];
                grid[antPose.x][antPose.y] = 0;
                antPose.setLocation(antPose.x, antPose.y - 1);
                grid[antPose.x][antPose.y] = 2;
            }
        }
    }
    public void operate() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 2 && antPoseValue == 0) {
                    antDirection -= 1;
                    if (Math.abs(antDirection) % 4 == 0) {
                        antPoseValue = grid[i - 1][j];
                        grid[i][j] = 1;
                        grid[i - 1][j] = 2;
                    } else if (Math.abs(antDirection) % 4 == 1) {
                        antPoseValue = grid[i][j + 1];
                        grid[i][j] = 1;
                        grid[i][j + 1] = 2;
                    } else if (Math.abs(antDirection) % 4 == 2) {
                        antPoseValue = grid[i + 1][j];
                        grid[i][j] = 1;
                        grid[i + 1][j] = 2;
                    } else if (Math.abs(antDirection) % 4 == 3) {
                        antPoseValue = grid[i][j - 1];
                        grid[i][j] = 1;
                        grid[i][j - 1] = 2;
                    }
                } else if (grid[i][j] == 2 && antPoseValue == 1) {
                    antDirection += 1;
                    if (Math.abs(antDirection) % 4 == 0) {
                        antPoseValue = grid[i - 1][j];
                        grid[i][j] = 0;
                        grid[i - 1][j] = 2;
                    } else if (Math.abs(antDirection) % 4 == 1) {
                        antPoseValue = grid[i][j + 1];
                        grid[i][j] = 0;
                        grid[i][j + 1] = 2;
                    } else if (Math.abs(antDirection) % 4 == 2) {
                        antPoseValue = grid[i + 1][j];
                        grid[i][j] = 0;
                        grid[i + 1][j] = 2;
                    } else if (Math.abs(antDirection) % 4 == 3) {
                        antPoseValue = grid[i][j - 1];
                        grid[i][j] = 0;
                        grid[i][j - 1] = 2;
                    }
                }
            }
        }
    }
}
