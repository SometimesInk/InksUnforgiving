package com.ink.unforgiving.maths;

public class IVector2 {
  public static final IVector2 ORIGIN = new IVector2(true);
  public int x;
  public int y;
  
  public IVector2(int x, int y) {
    this.x = ORIGIN.x + x;
    this.y = ORIGIN.y + y;
  }
  
  public IVector2() {
    this.x = ORIGIN.x;
    this.y = ORIGIN.y;
  }
  
  public IVector2(boolean init) {
    this.x = 0;
    this.y = 0;
  }
}
