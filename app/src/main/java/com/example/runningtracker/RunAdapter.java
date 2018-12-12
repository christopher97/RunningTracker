package com.example.runningtracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.runningtracker.model.Run;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RunAdapter extends RecyclerView.Adapter<RunAdapter.RunViewHolder> {

    List<Run> runList;

    // This is an inner class that represents the Java object of a single row
    // From here, we bind the elements we have in run_detail to a variable
    public static class RunViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        TextView dayTV;
        TextView distanceTV;
        TextView paceTV;
        TextView durationTV;

        public RunViewHolder(@NonNull View itemView) {
            super(itemView);

            // bind the elements
            dateTV = itemView.findViewById(R.id.dateID);
            dayTV = itemView.findViewById(R.id.dayID);
            distanceTV = itemView.findViewById(R.id.distanceID);
            paceTV = itemView.findViewById(R.id.paceID);
            durationTV = itemView.findViewById(R.id.durationID);
        }
    }

    public RunAdapter(List<Run> runs) {
        this.runList = runs;
    }

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // we need to "inflate" our layout for each row, and then pass it along
        // to our view holder
        View row = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.run_detail, viewGroup, false);
        RunViewHolder viewHolder = new RunViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {

        // we customize our row (ViewHolder) here
        Run run = runList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String date = dateFormat.format(run.getStart());

        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(run.getStart().getTime());

        holder.dateTV.setText(date);
        holder.dayTV.setText(Config.dayOfWeek[curr.get(Calendar.DAY_OF_WEEK)]);

        int dist = run.getDistance();
        String distString = (dist/1000) + "." + (dist%1000/100) + (dist%100/10) + " km";
        holder.distanceTV.setText(distString);

        int duration = run.getDuration();
        String durationString =
                duration/60 + ":" + (duration % 60 < 10 ? "0"+(duration%60):(duration%60));
        holder.durationTV.setText(durationString);

        long avgPace = (long) (1.0 * duration / (dist/1000.0));
        String avgPaceString =
                (avgPace/60) + "\'" + (avgPace%60) + "\"";
        holder.paceTV.setText(avgPaceString);

        Log.i("RunAdapter", distString + " " + durationString + " " + avgPaceString);
    }

    @Override
    public int getItemCount() {
        return runList.size();
    }
}
