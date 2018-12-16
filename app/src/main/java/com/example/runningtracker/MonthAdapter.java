package com.example.runningtracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.runningtracker.model.Month;
import com.example.runningtracker.model.Run;

import java.util.ArrayList;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {

    ArrayList<Month> monthList;
    RunAdapter adapter;

    public static class MonthViewHolder extends RecyclerView.ViewHolder {

        TextView monthTextView;
        TextView runTextView;
        TextView distanceTextView;
        TextView paceTextView;
        RecyclerView runRecycler;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            // bind elements
            monthTextView = itemView.findViewById(R.id.monthTextView);
            runTextView = itemView.findViewById(R.id.runTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            paceTextView = itemView.findViewById(R.id.paceTextView);
            runRecycler = itemView.findViewById(R.id.runRecycler);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            runRecycler.setLayoutManager(layoutManager);
        }
    }

    public MonthAdapter(ArrayList<Month> months) {
        this.monthList = months;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // we need to "inflate" our layout for each row, and then pass it along
        // to our view holder
        View row = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.month_detail, viewGroup, false);
        MonthViewHolder viewHolder = new MonthViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {

        // we customize our row (ViewHolder) here
        final Month month = monthList.get(position);

        String runString = month.getTotalRun() + " run(s)";
        String distString = month.getTotalDistance() + " km";

        holder.monthTextView.setText(month.getYearMonth());
        holder.runTextView.setText(runString);
        holder.distanceTextView.setText(distString);
        holder.paceTextView.setText(month.getPace());

        adapter = new RunAdapter(month.getRuns());

        final Context mContext = holder.itemView.getContext();
        adapter.setOnClick(new RunAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position) {
                // when a run is clicked, go the detail of that run
                Run run = month.getRuns().get(position);

                Intent intent = new Intent(mContext, RunDetailActivity.class);
                intent.putExtra("run", run);
                mContext.startActivity(intent);
            }
        });

        holder.runRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }
}
