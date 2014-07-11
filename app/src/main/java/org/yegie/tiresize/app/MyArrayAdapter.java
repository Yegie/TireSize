package org.yegie.tiresize.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sergey on 7/3/2014.
 */
public class MyArrayAdapter extends ArrayAdapter {
    public interface OnFavoriteClickedListener {
        public void onFavoriteClicked(TireComp t);
    }

    private final OnFavoriteClickedListener listener;

    public MyArrayAdapter(Context context, int resource, ArrayList<TireComp> objects, OnFavoriteClickedListener listener){
        super(context,resource,objects);
        objs = objects;
        this.listener=listener;
    }

    ArrayList<TireComp> objs;

    private class FavoriteClickListener implements View.OnClickListener {
        private final int index;
        FavoriteClickListener(int index) {
            this.index=index;
        }

        @Override
        public void onClick(View view) {
            TireComp tc=objs.get(index);

            if(listener!=null)
                listener.onFavoriteClicked(tc);
        }
    }

    ViewGroup vg;

    @Override
    public View getView(int i, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int abc = R.layout.no_detail_object;

        vg = (ViewGroup) inflater.inflate(abc,null);


        TextView a_r_r= (TextView) vg.findViewById(R.id.width_ratio_rim);
        a_r_r.setText(objs.get(i).current.width+"-"+objs.get(i).current.ratio+"/"+objs.get(i).current.rim);

        TextView mph= (TextView) vg.findViewById(R.id.mph);
        mph.setText("50 mph would be "+String.format("%.2f",objs.get(i).mph)+" mph");

        TextView miles= (TextView) vg.findViewById(R.id.miles);
        miles.setText("1000 miles would be "+String.format("%.2f",objs.get(i).mileage)+" miles");

        ImageButton fav = (ImageButton) vg.findViewById(R.id.imageButton);
        fav.setOnClickListener(new FavoriteClickListener(i));

        return vg;
    }

    @Override
    public void notifyDataSetChanged (){
        super.notifyDataSetChanged();

    }


}
