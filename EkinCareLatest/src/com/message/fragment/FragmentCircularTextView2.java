package com.message.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekincare.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.message.custominterface.BottomSheetFragmentInterface;
import com.message.custominterface.HorizontalListItemClickEvent;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentCircularTextView2 extends Fragment
{
    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    String unit;
    ArrayList<String> list;
    String value = "";
    private View createView;
    private RecyclerView recyclerView1;
    private int previousPosition = -1;
    private SparseBooleanArray selectedItems;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentCircularTextView2(ArrayList<String> list, String unit, BottomSheetFragmentInterface bottomSheetFragmentInterface) {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
        this.list=list;
        this.unit = unit;
        selectedItems = new SparseBooleanArray();
    }

    public FragmentCircularTextView2() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRetainInstance(true);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.bottom_sheet_single_list, container, false);

        recyclerView1 = (RecyclerView) createView.findViewById(R.id.recycler_view_1);

        floatingActionButton = (FloatingActionButton) createView.findViewById(R.id.button_list_done);

        RecyclerViewAdapter mAdapter1 = new RecyclerViewAdapter(list);

        StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);

        recyclerView1.setLayoutManager(mLayoutManager1);

        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        recyclerView1.setAdapter(mAdapter1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.isEmpty()){

                }else {
                    bottomSheetFragmentInterface.onBottonFragmentItemClick(value);
                }
            }
        });

        return  createView;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
    {
        ArrayList<String> list;

        public RecyclerViewAdapter(ArrayList<String> incomingList){
            list = incomingList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            LinearLayout containerLayout;

            View itemLayoutView;

            public MyViewHolder(View view) {
                super(view);
                this.itemLayoutView = itemLayoutView;
                textView = (TextView) view.findViewById(R.id.textview_blood_group);
                containerLayout = (LinearLayout) view.findViewById(R.id.container_layout);

                containerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedItems.size()==0){
                            selectedItems.put(getAdapterPosition(), true);
                            v.setSelected(true);
                            previousPosition = getAdapterPosition();
                            notifyItemChanged(getLayoutPosition());
                            value = list.get(getAdapterPosition());

                        }else{
                            if (selectedItems.get(getAdapterPosition(), false)){

                            }else{
                                selectedItems.clear();
                                notifyItemChanged(previousPosition);
                                selectedItems.put(getAdapterPosition(), true);
                                v.setSelected(true);
                                previousPosition = getAdapterPosition();
                                notifyItemChanged(previousPosition);
                                value = list.get(getAdapterPosition());
                            }
                        }
                    }
                });
            }
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_bottom_list_item_circle_text, parent, false);
            return new RecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position)
        {
            holder.textView.setText(list.get(position));

            holder.containerLayout.setSelected(selectedItems.get(position, false));
            holder.textView.setSelected(selectedItems.get(position, false));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
