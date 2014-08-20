package org.yegie.tiresize.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    public void recalculate2(int i, int j, double r, TireSize apple){

        TireSize orange = new TireSize(widths[i], ratios[j], r);

        TireComp pos = new TireComp(apple, orange);

        if (pos.delta <= 0.1) {

            for(int k = 0; k<objsFav.size();k++)
                if(pos.equals(objsFav.get(k))){
                    pos.fav = true;
                    break;
                }

            objs.add(pos);

        }

    }

    public void recalculate(){
        System.out.println("Recalculating wheels");
    //    resetFav();
        objs.clear();

        TextView textAll= (TextView) findViewById(R.id.TextAll);
        if(diffRims){
            textAll.setText("All tires within 10%");
        }else{
            textAll.setText(fixDec(rim)+"\" Rim Tires Within 10%");
        }

        if(!turned) {
            cube = 1;
            turned = false;
        }

        TireSize apple = new TireSize(width,ratio,rim);
        for(int i = 0;i<widths.length;i++){
            for(int j = 0;j<ratios.length;j++){
                if(diffRims) {
                    for (int r = 0; r < rims.length; r++) {

                        recalculate2(i,j,rims[r],apple);

                    }
                }
                else{
                    recalculate2(i,j,rim,apple);
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


    private class WidthSelectedListener extends SelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View tv, int pos, long id) {
            if(!super.checkNeed(tv, pos)){
                return;
            }

            String Sel = getResources().getStringArray(R.array.width_array)[pos];

            width = Integer.parseInt(Sel);

            System.out.println(width+"-"+ratio+"/"+rim);

            check();
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

            check();
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

            check();

        }

    }


    ArrayList<TireComp> objs = new ArrayList<TireComp>();
    ArrayList<TireComp> objsDisp = new ArrayList<TireComp>();
    ArrayList<TireComp> objsFav = new ArrayList<TireComp>();
    int cube;
    FixedListView l;
    FixedListView fav;
    MyArrayAdapter adapter;
    MyArrayAdapter adapterFav;
    boolean turned;
    int width;
    int ratio;
    double rim;

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        objs = savedInstanceState.getParcelableArrayList("objs");
        objsFav = savedInstanceState.getParcelableArrayList("objsFav");
        width = savedInstanceState.getInt("width");
        ratio = savedInstanceState.getInt("ratio");
        rim = savedInstanceState.getDouble("rim");
        cube = savedInstanceState.getInt("cube");
        turned = true;
        connectArrayAdapter();
        connectArrayAdapterFav();
    }

    @Override
    protected void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        out.putParcelableArrayList("objs",objs);
        out.putParcelableArrayList("objsFav",objsFav);
        out.putInt("width",width);
        out.putInt("cube",cube);
        out.putInt("ratio",ratio);
        out.putDouble("rim", rim);
    }

    private void connectArrayAdapter() {
        adapter = new MyArrayAdapter(this,android.R.layout.simple_list_item_1,objsDisp,this);
        adapter.favList = false;
        l.setAdapter(adapter);
    }

    private void connectArrayAdapterFav() {
        adapterFav = new MyArrayAdapter(this,android.R.layout.simple_list_item_1,objsFav,this);
        adapterFav.favList = true;
        fav.setAdapter(adapterFav);
    }

    @Override
    public void onFavoriteClicked(TireComp t) {
        int index=objsFav.indexOf(t);
        int index2=objs.indexOf(t);

        if(index>=0) {
            objsFav.remove(index);
            if(index2!=-1)
                objs.get(index2).fav = false;
        }
        else {
            t.fav = true;
            objsFav.add(t);
            if(index2!=-1)
                objs.get(index2).fav = true;
        }
        adapter.notifyDataSetChanged();
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

        fav = (FixedListView) findViewById(R.id.listViewFav);

        connectArrayAdapterFav();

        l = (FixedListView) findViewById(R.id.listView);

        connectArrayAdapter();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    public void resetFav(){

        for(int i = 0; i<objsFav.size();i++){
            TireComp t = objsFav.get(i);
            int index2=objs.indexOf(t);
            if(index2 !=-1){
                objs.get(index2).fav = false;
            }
        }

        objsFav.clear();
        adapter.notifyDataSetChanged();
        adapterFav.notifyDataSetChanged();

    }

    boolean diffRims;
    final String helpMes = "Favorite a tire to see more information about it.\nToggle Diff Rims will toggle between showing only tires with the same rim and all rims.";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.fav_reset) {
            Log.d(TAG, "Reseting Fav");
            resetFav();
            return true;
        }

        if (id == R.id.other_rims) {
            Log.d(TAG, "changing rims boolean");
            if(diffRims){
                diffRims = false;
            }
            else{
                diffRims = true;
            }

            check();

            return true;
        }

        if (id == R.id.share) {
            Log.d(TAG, "Sharing");

            shareText();

            return true;
        }

        if (id == R.id.help) {
            Log.d(TAG, "Halp me plox");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(helpMes);
            builder1.setCancelable(false);
            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String fixDec(double a){

        if(Math.abs(Math.round(a)-a)<0.01){
            return ""+(int)a;
        }else{
            return ""+a;
        }

    }

    public void shareText(){

        Intent intent = new Intent();

        String text = "Favorite List for tire "+width+"/"+ratio+"-"+fixDec(rim);

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, text+" (Android Tire Size Calculator)");

        for(int i = 0; i < objsFav.size();i++){
            TireSize a = objsFav.get(i).current;
            double b = Math.round(objsFav.get(i).delta*1000)/10.0;
            text = text + "\n "+a.width+"/"+a.ratio+"-"+fixDec(a.rim)+" (difference "+b+"%)";
        }
        text+="\n\n-- \nTire Size Calculator - https://play.google.com/store/apps/details?id=org.yegie.tiresize.app";
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,text);

        intent.setType("text/plain");
        startActivity(intent);

    }

}
