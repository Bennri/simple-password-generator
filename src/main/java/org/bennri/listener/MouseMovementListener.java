package org.bennri.listener;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MouseMovementListener {
    private int n;
    private List<Point> coords;

    public MouseMovementListener(int n) {
        this.n = n;
        this.coords = new LinkedList<>();
    }

    public void collectPoints() {

        for(int i = 0; i < n; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Point x = MouseInfo.getPointerInfo().getLocation();
            coords.add(x);
        }
    }

    public int getN() {
        return n;
    }

    public List<Point> getCoords() {
        return coords;
    }
}
