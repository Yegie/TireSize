package org.yegie.tiresize.app;

/**
 * Created by Sergey on 7/4/2014.
 */
public class TireSize{

    public int width;
    public int ratio;
    public double rim;

    public TireSize(){

        width = 145;
        ratio = 30;
        rim   = 12;

        hello();

    }

    public TireSize(int a,int b,double c){

        width = a;
        ratio = b;
        rim   = c;

        hello();

    }

    public double wall_mm;
    public double wall_in;
    public double diameter;
    public double length;

    public void hello(){
//      The d is important otherwise the calculation is wrong
        wall_mm=width*ratio/100d;
        wall_in=wall_mm/25.4;
        diameter=wall_in*2+rim;
        length=Math.PI*diameter;

    }

}
