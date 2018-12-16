package com.example.runningtracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.runningtracker.model.Run;
import com.example.runningtracker.model.RunDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class RunDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private Run run;
    private List<RunDetails> rdList;
    private String TAG = "RunDetailActivity";

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_detail);

        // bind map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // get data
        run = bundle.getParcelable("run");

        // bind the textview
        TextView kmTextView = findViewById(R.id.runDetailDistance);
        TextView paceTextView = findViewById(R.id.runDetailPace);
        TextView durationTextView = findViewById(R.id.runDetailDuration);

        // set the text
        String distance = Config.getDistanceString(run.getDistance());
        String pace = Config.getPaceString(run.getPace());
        String duration = Config.getDurationString(run.getDuration());

        kmTextView.setText(distance);
        paceTextView.setText(pace);
        durationTextView.setText(duration);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // when map is ready, fill the data
        getRunDetails();
        gMap = googleMap;
        drawPolyline();
    }

    private void getRunDetails() {
        DBHandler dbHandler =
                new DBHandler(this, null, null, MyContract.DATABASE_VERSION);
        rdList = dbHandler.fetchRunDetails(run.getId());
    }

    // draws the path of the run
    private void drawPolyline() {
        RunDetails startPoint = rdList.get(0);

        PolylineOptions options = new PolylineOptions().clickable(false);
        for(int i=0; i<rdList.size(); i++) {
            RunDetails currLoc = rdList.get(i);
            options.add(new LatLng(currLoc.getLat(), currLoc.getLon()));
        }
        Polyline polyline = gMap.addPolyline(options);

        stylePolyline(polyline);
        setMarker();

        // moves the position of map "camera" to the starting point
        LatLng startLoc = new LatLng(startPoint.getLat(), startPoint.getLon());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(startLoc).zoom(17).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // color the path
    private void stylePolyline(Polyline polyline) {
        polyline.setColor(Color.parseColor("#FFA000"));
        polyline.setWidth(10);
    }

    // set marker for start and end
    private void setMarker() {
        RunDetails start = rdList.get(0);
        RunDetails end = rdList.get(rdList.size() - 1);

        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(start.getLat(), start.getLon()))
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(end.getLat(), end.getLon()))
                .title("End")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
