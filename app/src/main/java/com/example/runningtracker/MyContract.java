package com.example.runningtracker;

import android.net.Uri;

public class MyContract {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "runDB.db";

    public static final String AUTHORITY =
            "com.example.runningtracker.provider.RunningTrackerContentProvider";

    public static final String RUNS_TABLE = "runs";
    public static final String RUN_DETAILS_TABLE = "run_details";

    public static final Uri RUN_URI =
            Uri.parse("content://" + AUTHORITY + "/" + RUNS_TABLE);
    public static final Uri RUN_DETAILS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + RUN_DETAILS_TABLE);
    public static final Uri RUN_DETAILS_RUN_ID_URI =
            Uri.parse("content://" + AUTHORITY + "/" + RUNS_TABLE + "/" + RUN_DETAILS_TABLE);

    public static final String RUN_COLUMN_ID = "_id";
    public static final String RUN_COLUMN_START = "start_timestamp";
    public static final String RUN_COLUMN_END = "end_timestamp";
    public static final String RUN_COLUMN_DISTANCE = "distance";
    public static final String RUN_COLUMN_DURATION = "duration";
    public static final String RUN_COLUMN_PACE = "pace";

    public static final int RUN_COL_ID_NO = 0;
    public static final int RUN_COL_START_NO = 1;
    public static final int RUN_COL_END_NO = 2;
    public static final int RUN_COL_DISTANCE_NO = 3;
    public static final int RUN_COL_DURATION_NO = 4;
    public static final int RUN_COL_PACE_NO = 5;

    public static final String[] runProjection = {
            RUN_COLUMN_ID,
            RUN_COLUMN_START,
            RUN_COLUMN_END,
            RUN_COLUMN_DISTANCE,
            RUN_COLUMN_DURATION,
            RUN_COLUMN_PACE,
    };

    public static final String RD_COLUMN_ID = "_id";
    public static final String RD_COLUMN_RUN_ID = "run_id";
    public static final String RD_COLUMN_LAT = "lat";
    public static final String RD_COLUMN_LON = "lon";
    public static final String RD_COLUMN_TIME = "time";
    public static final String RD_COLUMN_PACE = "pace";

    public static final int RD_COL_ID_NO = 0;
    public static final int RD_COL_RUN_ID_NO = 1;
    public static final int RD_COL_LAT_NO = 2;
    public static final int RD_COL_LON_NO = 3;
    public static final int RD_COL_TIME_NO = 4;
    public static final int RD_COL_PACE_NO = 5;

    public static final String[] runDetailsProjection = {
            RD_COLUMN_ID,
            RD_COLUMN_RUN_ID,
            RD_COLUMN_LAT,
            RD_COLUMN_LON,
            RD_COLUMN_TIME
    };
}
