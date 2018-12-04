package com.example.runningtracker.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.runningtracker.DBHandler;
import com.example.runningtracker.MyContract;

public class RunningTrackerContentProvider extends ContentProvider {

    public static final int RUNS = 1;
    public static final int RUNS_ID = 2;
    public static final int RUN_DETAILS = 3;
    public static final int RUN_DETAILS_ID = 4;
    public static final int RUN_DETAILS_RUN_ID = 5;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(MyContract.AUTHORITY, MyContract.RUNS_TABLE, RUNS);
        sURIMatcher.addURI(MyContract.AUTHORITY, MyContract.RUNS_TABLE + "/#", RUNS_ID);
        sURIMatcher.addURI(MyContract.AUTHORITY, MyContract.RUN_DETAILS_TABLE + "/#", RUN_DETAILS_ID);
        sURIMatcher.addURI(MyContract.AUTHORITY, MyContract.RUNS_TABLE + "/" +
                MyContract.RUN_DETAILS_TABLE + "/#", RUN_DETAILS_RUN_ID);
    }

    private DBHandler myDB;

    public RunningTrackerContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case RUNS:
                break;
            case RUNS_ID:
                break;
            case RUN_DETAILS:
                break;
            case RUN_DETAILS_ID:
                break;
            case RUN_DETAILS_RUN_ID:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case RUNS:
                id = sqlDB.insert(MyContract.RUNS_TABLE,
                        null, values);

                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(MyContract.RUNS_TABLE + "/" + id);
            case RUN_DETAILS_ID:
                id = sqlDB.insert(MyContract.RUN_DETAILS_TABLE,
                        null, values);

                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(MyContract.RUNS_TABLE + "/" + id);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHandler(getContext(), null, null, MyContract.DATABASE_VERSION);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case RUNS:
                break;
            case RUNS_ID:
                queryBuilder.appendWhere(MyContract.RUN_COLUMN_ID + "="
                    + uri.getLastPathSegment());
            case RUN_DETAILS_ID:
                queryBuilder.appendWhere(MyContract.RD_COLUMN_ID + "="
                    + uri.getLastPathSegment());
            case RUN_DETAILS_RUN_ID:
                queryBuilder.appendWhere(MyContract.RD_COLUMN_RUN_ID + "="
                    + uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;
        String id = "";
        switch (uriType) {
            case RUNS:
                rowsUpdated = sqlDB.update(MyContract.RUNS_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case RUNS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MyContract.RUNS_TABLE,
                            values,
                            MyContract.RUN_COLUMN_ID + " = " + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MyContract.RUNS_TABLE,
                            values,
                            MyContract.RUN_COLUMN_ID + " = " + id
                                                            + " and "
                                                            + selection,
                            selectionArgs);
                }
                break;
            case RUN_DETAILS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MyContract.RUN_DETAILS_TABLE,
                            values,
                            MyContract.RD_COLUMN_ID + " = " + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MyContract.RUN_DETAILS_TABLE,
                            values,
                            MyContract.RD_COLUMN_ID + " = " + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case RUN_DETAILS_RUN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MyContract.RUN_DETAILS_TABLE,
                            values,
                            MyContract.RD_COLUMN_RUN_ID + " = " + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MyContract.RUN_DETAILS_TABLE,
                            values,
                            MyContract.RD_COLUMN_RUN_ID + " = " + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
