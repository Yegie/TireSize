package org.yegie.tiresize.app;

/**
 * Created by Sergey on 7/4/2014.
 */
public class TireComp implements Comparable<TireComp>{


    TireSize userIn;
    public TireSize current;

    public double delta;
    public double mph;
    public double mileage;

    public TireComp(TireSize a, TireSize b){


        userIn  = a;
        current = b;

        delta   = Math.abs(b.diameter/a.diameter-1);
        mph     = 50+50*delta;
        mileage = 1000-1000*delta;

//    my $delta=$diameter/$cdiameter-1;
//    my $ad=$delta>0 ? $delta : -$delta;
//    next if $ad>$maxdelta;



    }

    @Override
    public int compareTo(TireComp another) {

        if(delta==another.delta)
            return 0;
        if(delta>another.delta)
            return 1;
        else
            return -1;
    }
}
