package org.yegie.tiresize.app;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * LinearLayout based layout similar to a ListView, but without its internal scrolling.
 */
public class FixedListView extends LinearLayout {
    private Adapter adapter;

    /**
     * Holding place for element IDs to check what views
     * need to be rebuilt on updates.
     */
    private ArrayList<Long> idList=new ArrayList<Long>();

    /**
     * Base class has no default constructor, need to override/make public.
     */
    public FixedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter=adapter;

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                onDatasetChanged();
            }

            @Override
            public void onInvalidated() {
                onDatasetChanged();
            }
        });

        onDatasetChanged();
    }

    public void onDatasetChanged() {
        int eltCount=adapter.getCount();

        // We might have more views than there
        // are elements. Removing them.
        //
        while(idList.size() > eltCount) {
            int i=idList.size() - 1;
            idList.remove(i);
            removeViewAt(i);
        }

        // Checking if elements for which we already have views
        // changed and replacing their views.
        //
        for(int i=0; i<idList.size(); ++i) {
            long eltId=adapter.getItemId(i);

            if(idList.get(i)!=eltId) {
                View v=adapter.getView(i,null,this);
                removeViewAt(i);
                addView(v,i);
                idList.set(i,eltId);
            }
        }

        // Adding extra views
        //
        for(int i=idList.size(); i<eltCount; ++i) {
            addView(adapter.getView(i,null,this));
            idList.add(adapter.getItemId(i));
        }
    }
}
