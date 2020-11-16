package com.example.seccharge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter<NumberedAdapter.ViewHolder> {
    private ArrayList<String> labels = new ArrayList<String>();

    public NumberedAdapter(int start,int end) {

        int hour=9;
        int min=0;

        for(int i=0;i<(end-start)*4;i++)
        {
            if(min==60)
            {
                hour++;
                min=0;

            }
            labels.add(hour+":"+String.format("%02d",min));
            min =min+15;



        }

      //  labels.add("9:00");






      /*  labels = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            labels.add(String.valueOf(i));
        }*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String label = labels.get(position);
        holder.button.setText(label);

        //handling item click event
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.button.getContext(), label, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button =  itemView.findViewById(R.id.time_slot_item_button);
        }
    }
}
