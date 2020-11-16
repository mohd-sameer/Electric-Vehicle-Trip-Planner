package com.example.seccharge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Vehicle> list = new ArrayList<>();
    ItemClickListener itemClickListener;

    public RecyclerViewAdapter(List<Vehicle> list) {
        this.list = list;
        // Request option for Glide
        // option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vechicle_row_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        final Vehicle vehData = list.get(position);
        holder.vh_make.setText(vehData.getMake());
        holder.vh_model.setText(vehData.getModel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.OnItemClick(position ,vehData);
            }
        });

        // Load Image from the internet and set it into Imageview using Glide
        // Glide.with(wContext).load(wData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void UpdateData(int position,Vehicle vehData){
        list.remove(position);
        list.add(vehData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vh_make, vh_model;
        RadioButton vh_primary;
        //ImageView img_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            vh_make = itemView.findViewById(R.id.make);
            vh_model = itemView.findViewById(R.id.model);
            vh_primary = itemView.findViewById(R.id.primary);
            // img_thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    }


