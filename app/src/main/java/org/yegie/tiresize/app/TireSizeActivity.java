package org.yegie.tiresize.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class TireSizeActivity extends ActionBarActivity implements MyArrayAdapter.OnFavoriteClickedListener {

    private static final String TAG = TireSizeActivity.class.getSimpleName();

    private class PromptClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            cube++;
            updateDisp();
        }

    }

    final int[] widths = {145,155,165,175,185,195,205,215,225,235,245,255,265,275,285,295,305,315,325};
    final int[] ratios = {30,35,40,45,50,55,60,65,70,75,80,85};
    final double[] rims = {12,13,14,15,16,16.5,17,18,19,20,21};

    public void recalculate(){
        System.out.println("Recalculating wheels");
        objs.clear();

        cube = 1;

        TireSize apple = new TireSize(width,ratio,rim);
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

        updateDisp();
    }

    public void updateDisp(){

        objsDisp.clear();

        for(int i = 0; i< cube*3; i++){
            if(i<objs.size())
                objsDisp.add(objs.get(i));

        }

        adapter.notifyDataSetChanged();

    }


    private class SelectedListener implements Spinner.OnItemSelectedListener {


        public boolean checkNeed( View tv, int pos) {

            // This is a mystery wrapped in an enigma -- android calls us
            // twice on rotation, first time with a null view.
            //
            if(tv==null)
                return false;

            if(don == 0) {
                don++;
                return false;
            }

            if(pos == 0)
                return false;

            return true;
            // Log.d(TAG, Log.getStackTraceString(new Exception()));


        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        public void check(){

            if(
                    width != 0 &&
                    ratio != 0 &&
                    rim != 0
            )
                recalculate();
            else
                return;
        }

    }
    private class WidthSelectedListener extends SelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View tv, int pos, long id) {
            if(!super.checkNeed(tv, pos)){
                return;
            }

            String Sel = getResources().getStringArray(R.array.width_array)[pos];

            width = Integer.parseInt(Sel);

            System.out.println(width+"-"+ratio+"/"+rim);

            super.check();
        }

    }
    private class RatioSelectedListener extends SelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View tv, int pos, long id) {
            if(!super.checkNeed(tv, pos)){
                return;
            }

            String Sel = getResources().getStringArray(R.array.ratio_array)[pos];

            ratio = Integer.parseInt(Sel);

            System.out.println(width+"-"+ratio+"/"+rim);

            super.check();
        }

    }
    private class RimSelectedListener extends SelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View tv, int pos, long id) {
            if(!super.checkNeed(tv, pos)){
                return;
            }

            String Sel = getResources().getStringArray(R.array.rim_array)[pos];

            rim = Double.parseDouble(Sel);


            System.out.println(width+"-"+ratio+"/"+rim);

            super.check();

        }

    }

    ArrayList<String> arr = new ArrayList<String>();
    ArrayList<TireComp> objs = new ArrayList<TireComp>();
    ArrayList<TireComp> objsDisp = new ArrayList<TireComp>();
    ArrayList<TireComp> objsFav = new ArrayList<TireComp>();
    int cube;
    ListView l;
    ListView fav;
    MyArrayAdapter adapter;
    MyArrayAdapter adapterFav;

    int width;
    int ratio;
    double rim;

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        arr = savedInstanceState.getStringArrayList("aaa");
        connectArrayAdapter();
    }

    private void connectArrayAdapter() {
        adapter = new MyArrayAdapter(this,android.R.layout.simple_list_item_1,objsDisp,this);
        l.setAdapter(adapter);
    }

    private void connectArrayAdapterFav() {
        adapterFav = new MyArrayAdapter(this,android.R.layout.simple_list_item_1,objsFav,this);
        fav.setAdapter(adapterFav);
    }

    @Override
    public void onFavoriteClicked(TireComp t) {
        int index=objsFav.indexOf(t);

        if(index>=0)
            objsFav.remove(index);
        else
            objsFav.add(t);

        adapterFav.notifyDataSetChanged();
    }

    int don = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_size_home);

        View view=findViewById(R.id.more_stuff);
        view.setOnClickListener(new PromptClickListener());

        Spinner a = (Spinner) findViewById(R.id.test_spinner);
        a.setOnItemSelectedListener(new WidthSelectedListener());

        Spinner b = (Spinner) findViewById(R.id.test_spinner2);
        b.setOnItemSelectedListener(new RatioSelectedListener());

        Spinner c = (Spinner) findViewById(R.id.test_spinner3);
        c.setOnItemSelectedListener(new RimSelectedListener());

        fav = (ListView) findViewById(R.id.listViewFav);

        connectArrayAdapterFav();

        l = (ListView) findViewById(R.id.listView);

        connectArrayAdapter();



    }
    @Override
    protected void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        out.putStringArrayList("aaa" ,arr);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d(TAG, "Someone wants settings");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
