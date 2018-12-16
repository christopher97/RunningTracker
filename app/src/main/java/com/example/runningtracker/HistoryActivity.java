package com.example.runningtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.runningtracker.model.Month;
import com.example.runningtracker.model.Run;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private String TAG = "HistoryActivity";
    private List<Run> runList;
    private ArrayList<Month> months = new ArrayList<Month>();

    // RecyclerView Variables
    RecyclerView monthListView;
    RecyclerView.LayoutManager layoutManager;
    MonthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // handle recyclerView
        monthListView = findViewById(R.id.monthList);

        layoutManager = new LinearLayoutManager(this);
        monthListView.setLayoutManager(layoutManager);

        // set adapter
        adapter = new MonthAdapter(months);
        monthListView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        DBHandler dbHandler = new DBHandler(this, null, null,
                MyContract.DATABASE_VERSION);

        runList = dbHandler.fetchAllRuns();

        setAverages();
        setRecyclerData();
    }

    // set data for the recycler view
    private void setRecyclerData() {
        if (!runList.isEmpty()) {

            Run run = runList.get(0);
            Calendar curr = Calendar.getInstance();
            curr.setTimeInMillis(run.getStart().getTime());

            Month month = new Month(curr.get(Calendar.MONTH), curr.get(Calendar.YEAR));
            month.addRun(run);

            for (int i=1; i<runList.size(); i++) {
                run = runList.get(i);
                curr.setTimeInMillis(run.getStart().getTime());

                int currMonth = curr.get(Calendar.MONTH);
                int currYear = curr.get(Calendar.YEAR);
                if (currMonth > month.getMonth() || currYear > month.getYear()) {
                    months.add(month);
                    month = new Month(currMonth, currYear);
                }
                month.addRun(run);
            }
            months.add(month);
            adapter.notifyDataSetChanged();
        }
    }

    // count the averages
    private void setAverages() {
        TextView totalKilometersText = findViewById(R.id.totalKilometers);
        TextView totalRunsText = findViewById(R.id.totalRuns);
        TextView avgDistanceText = findViewById(R.id.avgDistance);
        TextView avgPaceText = findViewById(R.id.avgPace);

        int totalRuns = 0;
        long totalKM, totalDuration;

        totalKM = totalDuration = 0;

        for (int i=0; i<runList.size(); i++) {
            totalRuns++;

            Run curr = runList.get(i);
            totalKM += curr.getDistance();
            totalDuration += curr.getDuration();
        }

        String kmString = (totalKM/1000) + "." + (totalKM%1000/100) + (totalKM%100/10);
        totalKilometersText.setText(kmString);

        totalRunsText.setText(String.valueOf(totalRuns));

        double avgDistance = Math.round((1.0 * (totalKM/1000.0) / totalRuns) * 100.0) / 100.0;
        avgDistanceText.setText(String.valueOf(avgDistance));

        long avgPace = (long) (1.0 * totalDuration / (totalKM/1000.0));
        String avgPaceString =
                (avgPace/60) + "\'" + (avgPace%60) + "\"";
        avgPaceText.setText(avgPaceString);
    }
}
