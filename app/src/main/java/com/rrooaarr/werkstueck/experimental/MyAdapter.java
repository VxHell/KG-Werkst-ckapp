package com.rrooaarr.werkstueck.experimental;

import com.rrooaarr.werkstueck.MainViewModel;
import com.rrooaarr.werkstueck.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    List<MainViewModel> data;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<MainViewModel> myDataset) {
        data = myDataset;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.recyclerview_item;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}