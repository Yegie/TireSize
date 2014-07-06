package org.yegie.tiresize.app.test;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.yegie.tiresize.app.TireComp;
import org.yegie.tiresize.app.TireSize;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sergey on 7/5/2014.
 */
public class TireCompTest extends TestCase {

    @SmallTest
    public void testTireSize(){

        TireSize aa = new TireSize(155,45,13);

        assertEquals(18.49,  aa.diameter,    0.01);
        assertEquals(2.75,   aa.wall_in,     0.01);
        assertEquals(69.75,   aa.wall_mm,     0.01);
        assertEquals(58.09,  aa.length,      0.01);

    }

    @SmallTest
    public void testTireComp(){

        TireSize aa = new TireSize(155,45,13);
        TireSize ab = new TireSize(175,40,13);

        TireComp a = new TireComp(aa,ab);

        assertEquals(0.0011,  a.delta,    0.01);
        assertEquals(50.05,   a.mph,      0.01);
        assertEquals(998.94,a.mileage,  0.01);

    }

    @SmallTest
    public void testTireSort(){

        final int[] widths = {145,155,165,175,185,195,205,215,225,235,245,255,265,275,285,295,305,315,325};
        final int[] ratios = {30,35,40,45,50,55,60,65,70,75,80,85};
        final double[] rims = {12,13,14,15,16,16.5,17,18,19,20,21};

        ArrayList<TireComp> objs = new ArrayList<TireComp>();

        TireSize apple = new TireSize(155,45,13);
        for(int i = 0;i<widths.length;i++){
            for(int j = 0;j<ratios.length;j++){
                for(int r = 0;r<rims.length;r++){

                    TireSize orange = new TireSize(widths[i],ratios[j],rims[r]);

                    TireComp pos = new TireComp(apple,orange);

                    if(pos.delta<=0.1){

                        objs.add(pos);

                    }

                }
            }
        }

        Collections.sort(objs);
        final TireComp[] exp = {new TireComp(new TireSize(155,45,13),new TireSize(155,45,13)),
                                new TireComp(new TireSize(155,45,13),new TireSize(165,50,12)),
                                new TireComp(new TireSize(155,45,13),new TireSize(275,30,12)),
                                new TireComp(new TireSize(155,45,13),new TireSize(235,35,12)),
                                new TireComp(new TireSize(155,45,13),new TireSize(175,40,13))};

        boolean taco = true;

        if(objs.size()>=5) {
            for (int i = 0; i < 5; i++) {
                TireComp ak47 = objs.get(i);
                TireComp mp7 = exp[i];
                if (ak47.current.width == mp7.current.width &&
                    ak47.current.ratio == mp7.current.ratio &&
                    Math.abs(ak47.current.rim-mp7.current.rim) < 0.01) {
                    taco = false;
                }
            }
        }

        assertEquals(true,taco);
    }

}
