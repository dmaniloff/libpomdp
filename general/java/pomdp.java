/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdp.java
 * Description: interface to represent pomdp problem specifications
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

interface pomdp {

    /// P(o|b,a) in vector form for all o's
    //public double[] P_Oba(belState b, int a);

    /// tao(b,a,o)
    public belState tao(belState b, int a, int o);

    /// R(b,a)
    public double Rba(belState b, int a);

    //public double[][][] getT();

    //public double[][] getR();

    public int getnrSta();

    /// nrAct
    public int getnrAct();

    /// nrObs
    public int getnrObs();

    /// \gamma
    public double getGamma();

    public String[] getactStr();

    public String[] getobsStr();

} // pomdp