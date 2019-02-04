package com.sample.example.sampledemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder>{

    private ArrayList<SampleData> mArrayList;
    private Context mContext;

    public DataAdapter(Context context, ArrayList<SampleData> mArrayList) {
        this.mContext = context;
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.details_list_item_view, viewGroup,false);

        return new DataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder dataHolder, int i) {
        SampleData data = mArrayList.get(i);
        dataHolder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

     void setList(ArrayList<SampleData> arrayList)
    {
        mArrayList = arrayList;
    }

    class DataHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView, ageTextView;

         DataHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_tv);
            ageTextView = itemView.findViewById(R.id.age_tv);
        }

        private void bindData(SampleData data)
        {
            nameTextView.setText(mContext.getResources().getString(R.string.name, data.getName()));
            ageTextView.setText(mContext.getResources().getString(R.string.age, data.getAge()));
        }


    }
}
