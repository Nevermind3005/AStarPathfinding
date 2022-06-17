package com.nevermind.astarpathfinding;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box extends Canvas {

    private GraphicsContext graphicsContext;
    public double x;
    public double y;
    public double width;
    public double height;

    public Box( double x, double y, double width, double height) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        graphicsContext = getGraphicsContext2D();
    }

    public void draw(Color mainColor, Color borderColor) {
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        graphicsContext.setFill(mainColor);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());
        graphicsContext.setStroke(borderColor);
        graphicsContext.strokeRect(0, 0, getWidth(), getHeight());
    }
}
