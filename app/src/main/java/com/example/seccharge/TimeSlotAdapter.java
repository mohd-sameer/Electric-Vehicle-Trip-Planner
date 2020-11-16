package com.example.seccharge;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

    private ArrayList<String> timeSlots;
    private ArrayList<Boolean> timeSlotsSelected;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button timeSlotButton;

        public MyViewHolder(View view) {
            super(view);
            timeSlotButton = (Button) view.findViewById(R.id.time_slot_item_button);
        }
    }

    public TimeSlotAdapter(Activity activity, ArrayList<String> timeSlots) {
        this.activity = activity;
        this.timeSlots = timeSlots;

    }

    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slot_item, parent, false);

        return new TimeSlotAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TimeSlotAdapter.MyViewHolder holder, int position) {
        String currentTimeSlot = timeSlots.get(position);
        holder.timeSlotButton.setText(currentTimeSlot);



    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

}