package com.rrooaarr.werkstueck.experimental;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

        public class BaseViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private final ViewDataBinding binding;

            public BaseViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
            public void bind(Object obj) {
                binding.setVariable(com.rrooaarr.werkstueck.BR.mainViewModel,obj);
                binding.executePendingBindings();
            }
        }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, getLayoutIdForType(viewType), parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(getDataAtPosition(position));
    }

    public abstract Object getDataAtPosition(int position);

    public abstract int getLayoutIdForType(int viewType);

}