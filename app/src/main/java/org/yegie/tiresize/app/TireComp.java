package org.yegie.tiresize.app;

/**
 * Created by Sergey on 7/4/2014.
 */
public class TireComp {


    TireSize userIn;
    TireSize current;

    double delta;
    double mph;
    double mileage;

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

}
