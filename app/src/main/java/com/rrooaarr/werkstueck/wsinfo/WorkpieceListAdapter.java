package com.rrooaarr.werkstueck.wsinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rrooaarr.werkstueck.R;

import java.util.List;

public class WorkpieceListAdapter extends RecyclerView.Adapter<WorkpieceListAdapter.WorkpieceHolder> {

    class WorkpieceHolder extends RecyclerView.ViewHolder {
        private final TextView workpieceItemView;

        private WorkpieceHolder(View itemView) {
            super(itemView);
            workpieceItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<WorkpieceListElement> workpieces; // Cached copy of words

    public WorkpieceListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WorkpieceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WorkpieceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkpieceHolder holder, int position) {
        if (workpieces != null) {
            WorkpieceListElement current = workpieces.get(position);
            holder.workpieceItemView.setText(current.getKey());
        } else {
            // Covers the case of data not being ready yet.
            holder.workpieceItemView.setText("Kein Werkstück");
        }
    }

    public void setWorkpieces(List<WorkpieceListElement> workpieces){
        this.workpieces = workpieces;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (workpieces != null)
            return workpieces.size();
        else return 0;
    }
}