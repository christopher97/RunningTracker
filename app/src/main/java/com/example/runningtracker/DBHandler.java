package com.example.runningtracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.runningtracker.model.Run;
import com.example.runningtracker.model.RunDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private ContentResolver myCR;

    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, MyContract.DATABASE_NAME, factory, MyContract.DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addRunsTable(db);
        addRunDetailsTable(db);
    }

    private void addRunsTable(SQLiteDatabase db) {
        String CREATE_RUNS_TABLE = "CREATE TABLE " +
                MyContract.RUNS_TABLE + " ("
                + MyContract.RUN_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MyContract.RUN_COLUMN_START + " INTEGER, "
                + MyContract.RUN_COLUMN_END + " INTEGER, " +
                MyContract.RUN_COLUMN_DISTANCE + " INTEGER, "
                + MyContract.RUN_COLUMN_DURATION + " INTEGER, " +
                MyContract.RUN_COLUMN_PACE + " INTEGER" + ")";

        db.execSQL(CREATE_RUNS_TABLE);
    }

    private void addRunDetailsTable(SQLiteDatabase db) {
        String CREATE_RUN_DETAILS_TABLE = "CREATE TABLE " +
                MyContract.RUN_DETAILS_TABLE + " ("
                + MyContract.RD_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MyContract.RD_COLUMN_RUN_ID + " INTEGER, "
                + MyContract.RD_COLUMN_LAT + " DOUBLE, " +
                MyContract.RD_COLUMN_LON + " DOUBLE, "
                + MyContract.RD_COLUMN_TIME + " INTEGER," +
                " FOREIGN KEY (" + MyContract.RD_COLUMN_RUN_ID + ") REFERENCES " +
                MyContract.RUNS_TABLE + "(" + MyContract.RUN_COLUMN_ID + ")"
                + "ON DELETE CASCADE ON UPDATE CASCADE)";

        db.execSQL(CREATE_RUN_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyContract.RUNS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MyContract.RUN_DETAILS_TABLE);
        onCreate(db);
    }

    public int addRun(Run run) {
        ContentValues values = new ContentValues();
        values.put(MyContract.RUN_COLUMN_START, run.getStart().getTime());
        values.put(MyContract.RUN_COLUMN_END, run.getEnd().getTime());
        values.put(MyContract.RUN_COLUMN_DURATION, run.getDuration());
        values.put(MyContract.RUN_COLUMN_DISTANCE, run.getDistance());
        values.put(MyContract.RUN_COLUMN_PACE, run.getPace());

        Uri uri = myCR.insert(MyContract.RUN_URI, values);

        return Integer.valueOf(uri.getLastPathSegment());
    }

    public void addRunDetails(List<RunDetails> rdList, int runID) {
        for (int i=0; i<rdList.size(); i++) {
            RunDetails rd = rdList.get(i);
            ContentValues values = new ContentValues();
            values.put(MyContract.RD_COLUMN_RUN_ID, runID);
            values.put(MyContract.RD_COLUMN_LAT, rd.getLat());
            values.put(MyContract.RD_COLUMN_LON, rd.getLon());
            values.put(MyContract.RD_COLUMN_TIME, rd.getTime());

            myCR.insert(MyContract.RUN_DETAILS_URI, values);
        }
    }

    public List<Run> fetchAllRuns() {
        String[] projection = MyContract.runProjection;

        // no selection -> select all data
        String selection = "";
        Cursor cursor = myCR.query(MyContract.RUN_URI,
                projection, selection, null, null);
        List<Run> runs = new ArrayList<Run>();
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            // use do while, must iterate at least once
            do {
                // create new run instance
                Run run = new Run();

                run.setId(Integer.parseInt(cursor.getString(MyContract.RUN_COL_ID_NO)));
                run.setStart(new Timestamp(cursor.getLong(MyContract.RUN_COL_START_NO)));
                run.setEnd(new Timestamp(cursor.getLong(MyContract.RUN_COL_END_NO)));
                run.setDistance(Integer.parseInt(cursor.getString(MyContract.RUN_COL_DISTANCE_NO)));
                run.setDuration(Integer.parseInt(cursor.getString(MyContract.RUN_COL_DURATION_NO)));
                run.setPace(Integer.parseInt(cursor.getString(MyContract.RUN_COL_PACE_NO)));

                runs.add(run);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return runs;
    }

    public List<RunDetails> fetchRunDetails(int id) {
        String[] projection = MyContract.runDetailsProjection;

        String selection = "";
        Uri uri = Uri.parse(MyContract.RUN_DETAILS_RUN_ID_URI.toString() + "//" + id);
        Cursor cursor = myCR.query(uri,
                projection, selection, null, null);

        List<RunDetails> rdList = new ArrayList<RunDetails>();
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            do {
                RunDetails rd = new RunDetails();

                rd.setId(Integer.parseInt(cursor.getString(MyContract.RD_COL_ID_NO)));
                rd.setRunID(Integer.parseInt(cursor.getString(MyContract.RD_COL_RUN_ID_NO)));
                rd.setLat(Double.parseDouble(cursor.getString(MyContract.RD_COL_LAT_NO)));
                rd.setLon(Double.parseDouble(cursor.getString(MyContract.RD_COL_LON_NO)));
                rd.setTime(Integer.parseInt(cursor.getString(MyContract.RD_COL_TIME_NO)));

                rdList.add(rd);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return rdList;
    }

    public boolean deleteRun(int id) {
        String selection = "";
        Uri newURI = Uri.parse(MyContract.RUN_URI.toString() + "//" + id);
        int rowsDeleted = myCR.delete(newURI, selection, null);

        // return boolean based on the result of update
        return (rowsDeleted > 0);
    }
}
