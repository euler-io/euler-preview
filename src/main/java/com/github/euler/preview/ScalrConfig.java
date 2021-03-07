package com.github.euler.preview;

import java.awt.image.BufferedImageOp;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public class ScalrConfig {

    private Scalr.Method method;
    private Scalr.Mode mode;
    private int width;
    private int heigth;
    private BufferedImageOp[] ops;

    public ScalrConfig(Method method, Mode mode, int width, int heigth, BufferedImageOp... ops) {
        super();
        this.method = method;
        this.mode = mode;
        this.width = width;
        this.heigth = heigth;
        this.ops = ops;
    }

    public Scalr.Method getMethod() {
        return method;
    }

    public void setMethod(Scalr.Method method) {
        this.method = method;
    }

    public Scalr.Mode getMode() {
        return mode;
    }

    public void setMode(Scalr.Mode mode) {
        this.mode = mode;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public BufferedImageOp[] getOps() {
        return ops;
    }

    public void setOps(BufferedImageOp... ops) {
        this.ops = ops;
    }

}