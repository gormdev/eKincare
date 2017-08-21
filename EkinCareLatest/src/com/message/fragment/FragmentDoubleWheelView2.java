package com.message.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekincare.R;
import com.message.custominterface.BottomSheetFragmentInterface;
import com.message.custominterface.HorizontalListItemClickEvent;

import java.util.ArrayList;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentDoubleWheelView2 extends Fragment
{
    BottomSheetFragmentInterface bottomSheetFragmentInterface;
    String unit1, unit2;
    ArrayList<String> list1,list2;
    String weelHightFeetPositionValue,weelHightInchPositionValue;
    private View createView;
    private RecyclerView recyclerView1,recyclerView2;
    private int previousPosition = -1;
    private int previousPosition2 = -1;
    private SparseBooleanArray selectedItems,selectedItems2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public FragmentDoubleWheelView2(ArrayList<String> list1,ArrayList<String> list2,String unit1,String unit2,BottomSheetFragmentInterface bottomSheetFragmentInterface) {
        this.bottomSheetFragmentInterface =bottomSheetFragmentInterface;
        this.list1=list1;
        this.list2=list2;
        this.unit1 = unit1;
        this.unit2 = unit2;
        selectedItems = new SparseBooleanArray();
        selectedItems2 = new SparseBooleanArray();
    }

    public FragmentDoubleWheelView2() {
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
        createView = inflater.inflate(R.layout.bottom_sheet_double_list, container, false);

        previousPosition = list1.size()/2;
        previousPosition2 = list2.size()/2;

        selectedItems.put(previousPosition, true);
        selectedItems2.put(previousPosition2, true);

        recyclerView1 = (RecyclerView) createView.findViewById(R.id.recycler_view_1);
        recyclerView2 = (RecyclerView) createView.findViewById(R.id.recycler_view_2);

        RecyclerViewAdapter mAdapter1 = new RecyclerViewAdapter(list1);
        RecyclerViewAdapter2 mAdapter2 = new RecyclerViewAdapter2(list2);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());

        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView2.setLayoutManager(mLayoutManager2);

        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        recyclerView1.setAdapter(mAdapter1);
        recyclerView2.setAdapter(mAdapter2);

        recyclerView1.smoothScrollToPosition(previousPosition);
        recyclerView2.smoothScrollToPosition(previousPosition2);

        return  createView;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
    {
        ArrayList<String> list;

        public RecyclerViewAdapter(ArrayList<String> incomingList){
            list = incomingList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView,textViewUnit;
            LinearLayout containerLayout;

            View itemLayoutView;

            public MyViewHolder(View view) {
                super(view);
                this.itemLayoutView = itemLayoutView;
                textView = (TextView) view.findViewById(R.id.text_view_message_list_item);
                textViewUnit = (TextView) view.findViewById(R.id.text_view_unit_list_item);
                containerLayout = (LinearLayout) view.findViewById(R.id.container_layout);

                containerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedItems.size()==0){
                            selectedItems.put(getAdapterPosition(), true);
                            v.setSelected(true);
                            previousPosition = getAdapterPosition();
                            notifyItemChanged(getLayoutPosition());
                            weelHightFeetPositionValue = list.get(getAdapterPosition());
                            bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                        }else{
                            if (selectedItems.get(getAdapterPosition(), false)){

                            }else{
                                selectedItems.clear();
                                notifyItemChanged(previousPosition);
                                selectedItems.put(getAdapterPosition(), true);
                                v.setSelected(true);
                                previousPosition = getAdapterPosition();
                                notifyItemChanged(previousPosition);
                                //containerLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_rectangle_shape_medicine_blue));
                                //textView.setTextColor(getActivity().getResources().getColor(R.color.white));
                                weelHightFeetPositionValue = list.get(getAdapterPosition());
                                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);

                            }
                        }
                    }
                });
            }
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_bottom_list_item_text, parent, false);
            return new RecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position)
        {
            holder.textView.setText(list.get(position));
            holder.textViewUnit.setText(unit1);

            holder.containerLayout.setSelected(selectedItems.get(position, false));
            holder.textView.setSelected(selectedItems.get(position, false));
            holder.textViewUnit.setSelected(selectedItems.get(position, false));

            if(previousPosition==list.size()/2){
                weelHightFeetPositionValue = list.get(previousPosition);
                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder>
    {
        ArrayList<String> list;

        public RecyclerViewAdapter2(ArrayList<String> incomingList){
            list = incomingList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView,textViewUnit;
            LinearLayout containerLayout;

            View itemLayoutView;

            public MyViewHolder(View view) {
                super(view);
                this.itemLayoutView = itemLayoutView;
                textView = (TextView) view.findViewById(R.id.text_view_message_list_item);
                textViewUnit = (TextView) view.findViewById(R.id.text_view_unit_list_item);
                containerLayout = (LinearLayout) view.findViewById(R.id.container_layout);

                containerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedItems2.size()==0){
                            selectedItems2.put(getAdapterPosition(), true);
                            v.setSelected(true);
                            previousPosition2 = getAdapterPosition();
                            notifyItemChanged(getLayoutPosition());
                            weelHightInchPositionValue = list.get(getAdapterPosition());
                            bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
                        }else{
                            if (selectedItems2.get(getAdapterPosition(), false)){

                            }else{
                                selectedItems2.clear();
                                notifyItemChanged(previousPosition2);
                                selectedItems2.put(getAdapterPosition(), true);
                                v.setSelected(true);
                                previousPosition2 = getAdapterPosition();
                                notifyItemChanged(previousPosition2);
                                //containerLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_rectangle_shape_medicine_blue));
                                //textView.setTextColor(getActivity().getResources().getColor(R.color.white));
                                weelHightInchPositionValue = list2.get(getAdapterPosition());
                                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);

                            }
                        }
                    }
                });
            }
        }

        @Override
        public RecyclerViewAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_bottom_list_item_text, parent, false);

            return new RecyclerViewAdapter2.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter2.MyViewHolder holder, final int position)
        {
            holder.textView.setText(list.get(position));
            holder.textViewUnit.setText(unit2);

            holder.containerLayout.setSelected(selectedItems2.get(position, false));
            holder.textView.setSelected(selectedItems2.get(position, false));
            holder.textViewUnit.setSelected(selectedItems2.get(position, false));

            if(previousPosition2==list.size()/2){
                weelHightInchPositionValue = list.get(previousPosition2);
                bottomSheetFragmentInterface.onBottonFragmentItemClick(weelHightFeetPositionValue + " " + weelHightInchPositionValue);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
