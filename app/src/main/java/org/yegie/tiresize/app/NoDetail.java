package org.yegie.tiresize.app;

public class NoDetail extends Object {

int width, ratio;
double rim, mph, miles;

    public NoDetail(){
        width = 145;
        ratio = 30;
        rim   = 12;
        mph   = 60;
        miles = 1000;
    }

    public NoDetail(int a, int b, double c,double d,double e){
        width = a;
        ratio = b;
        rim   = c;
        mph   = d;
        miles = e;
    }


}
