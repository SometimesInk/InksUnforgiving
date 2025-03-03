package com.ink.unforgiving.maths;

public class IVector2 {
    public int x;
    public int y;

    public static final IVector2 ORIGIN = new IVector2(0, 0);

    public IVector2(int x, int y) {
        this.x = ORIGIN.x + x;
        this.y = ORIGIN.y + y;
    }

    public IVector2() {
        this.x = ORIGIN.x;
        this.y = ORIGIN.y;
    }
}