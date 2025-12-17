package com.utilitygang.zerosum.model;

public class Quote {

    private double c;   // current price
    private double d;   // change
    private double dp;  // percent change
    private double h;   // high
    private double l;   // low
    private double o;   // open
    private double pc;  // previous close
    private long t;     // timestamp


    public Quote() {}

    public double getC() { return c; }
    public void setC(double c) { this.c = c; }

    public double getD() { return d; }
    public void setD(double d) { this.d = d; }

    public double getDp() { return dp; }
    public void setDp(double dp) { this.dp = dp; }

    public double getH() { return h; }
    public void setH(double h) { this.h = h; }

    public double getL() { return l; }
    public void setL(double l) { this.l = l; }

    public double getO() { return o; }
    public void setO(double o) { this.o = o; }

    public double getPc() { return pc; }
    public void setPc(double pc) { this.pc = pc; }

    public long getT() { return t; }
    public void setT(long t) { this.t = t; }
}