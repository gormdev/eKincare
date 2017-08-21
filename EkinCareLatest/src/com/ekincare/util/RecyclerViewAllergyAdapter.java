package com.ekincare.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekincare.R;
import com.oneclick.ekincare.vo.AllergieDataset;
import com.oneclick.ekincare.vo.Image;

import java.util.List;

/**
 * Created by Ajay on 23-09-2016.
 */
public class RecyclerViewAllergyAdapter extends RecyclerView.Adapter<RecyclerViewAllergyAdapter.MyViewHolder> {

    List<AllergieDataset> mArrayListAdapter;
    Context context;

    private OnItemClickListener onItemClickListener;

    public RecyclerViewAllergyAdapter(List<AllergieDataset> mArrayListAdapter, Context context) {
        this.mArrayListAdapter = mArrayListAdapter;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        public void onItemClick(MyViewHolder item, int position, View v);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(int location, AllergieDataset allergieDataset)
    {
        mArrayListAdapter.add(location, allergieDataset);
        notifyItemInserted(location);
    }

    public void update(int location, AllergieDataset allergieDataset)
    {
        System.out.println("RecyclerViewAssessmentAdapter.update location="+location);
        notifyItemChanged(location);
    }

    public void remove(int location,AllergieDataset allergieDataset){
        System.out.println("RecyclerViewAssessmentAdapter.remove" + location);
        mArrayListAdapter.remove(location);
        notifyItemRemoved(location);
        notifyItemRangeChanged(location, mArrayListAdapter.size());
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mAllergiName;
        TextView mAllergiRecation;
        RelativeLayout allergyView;
        ImageView imageViewMenu;
        RecyclerViewAllergyAdapter recyclerViewAllergyAdapter;

        public MyViewHolder(View convertView,RecyclerViewAllergyAdapter recyclerViewAllergyAdapter) {
            super(convertView);
            this.recyclerViewAllergyAdapter = recyclerViewAllergyAdapter;

            mAllergiName = (TextView) convertView.findViewById(R.id.allergies_name);
            mAllergiRecation = (TextView) convertView.findViewById(R.id.allergies_reaction);
            allergyView = (RelativeLayout) convertView.findViewById(R.id.allergy_view);
            imageViewMenu=(ImageView) convertView.findViewById(R.id.image_menu);
            imageViewMenu.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = recyclerViewAllergyAdapter.getOnItemClickListener();
            if(listener != null)
            {
                listener.onItemClick(this, getAdapterPosition(),v);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allergies_data_row, parent, false);
        return new MyViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        System.out.println("size======"+mArrayListAdapter.size());
        holder.mAllergiName.setText(mArrayListAdapter.get(position).getName().substring(0,1).toUpperCase()+mArrayListAdapter.get(position).getName().substring(1));
        holder.mAllergiRecation.setText("Reason for allergy: "+mArrayListAdapter.get(position).getReaction());

    }

    @Override
    public int getItemCount() {
        return mArrayListAdapter.size();
    }
}

