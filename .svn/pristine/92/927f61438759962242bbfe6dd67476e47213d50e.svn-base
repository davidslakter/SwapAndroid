package com.swap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swap.R;
import com.swap.models.Swaps;
import com.swap.views.activities.SwapProfileActivity;

import java.util.List;

/**
 * Created by anjali on 02-08-2017.
 */

public class SwappedAdapter extends RecyclerView.Adapter<SwappedAdapter.DataObjectHolder> {
    private List<Swaps> swapsList;
    Context context;
    public SwappedAdapter(List<Swaps> swapsUserList,  Context context) {
        this.swapsList = swapsUserList;
        this.context = context;
    }

    @Override
    public SwappedAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_swaps, parent, false);

        SwappedAdapter.DataObjectHolder dataObjectHolder = new SwappedAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(SwappedAdapter.DataObjectHolder holder, int position) {

        Swaps swaps = swapsList.get(position);
        holder.textViewSwapUserName.setText(swaps.getSwapUserName());
        holder.textViewSwapTime.setText(swaps.getSwapTime());
        holder.relativeLayoutRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SwapProfileActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return swapsList.size();
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView textViewSwapUserName;
        TextView textViewSwapTime;
        RelativeLayout relativeLayoutRaw;
        public DataObjectHolder(View itemView) {
            super(itemView);
            textViewSwapUserName = (TextView) itemView.findViewById(R.id.textViewSwapUserName);
            textViewSwapTime = (TextView) itemView.findViewById(R.id.textViewSwapTime);
            relativeLayoutRaw = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutRaw);
        }
    }

}
