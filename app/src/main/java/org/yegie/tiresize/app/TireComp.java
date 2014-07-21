package org.yegie.tiresize.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 7/4/2014.
 */
public class TireComp implements Comparable<TireComp>, Parcelable{


    TireSize userIn;
    public TireSize current;

    public double delta;
    public double mph;
    public double mileage;
    public boolean fav;

    public TireComp(TireSize a, TireSize b){

        userIn  = a;
        current = b;
        fav = false;

        calculate();

    }


    private void calculate(){

        delta   = Math.abs(current.diameter/userIn.diameter-1);
        mph     = 50+50*delta;
        mileage = 1000-1000*delta;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(new double[]{
                this.delta,
                this.mileage,
                this.mph,
                this.current.rim,
                this.userIn.rim});
        dest.writeBooleanArray(new boolean[]{
                this.fav});
        dest.writeIntArray(new int[] {
                this.current.ratio,
                this.current.width,
                this.userIn.ratio,
                this.userIn.width});
    }

    public TireComp(Parcel in){
        double[] data = new double[5];
        boolean[] data2 = new boolean[1];
        int[] data3 = new int[4];

        in.readDoubleArray(data);
        in.readBooleanArray(data2);
        in.readIntArray(data3);

        this.userIn = new TireSize(data3[2],data3[3],data[4]);
        this.current = new TireSize(data3[0],data3[1],data[3]);
        this.fav = data2[0];
        this.delta = data[0];
        this.mileage = data[1];
        this.mph = data[2];

        calculate();

    }
}
