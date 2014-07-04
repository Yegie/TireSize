package org.yegie.tiresize.app;

/**
 * Created by Sergey on 7/4/2014.
 */
public class TireSize{

    int width, ratio;
    double rim;

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

    double wall_mm, wall_in;
    double diameter;
    double length;

    public void hello(){

        wall_mm=width*ratio/100;
        wall_in=wall_mm/25.4;
        diameter=wall_in*2+rim;
        length=Math.PI*diameter;

    }

}
