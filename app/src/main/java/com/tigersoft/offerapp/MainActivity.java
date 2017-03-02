package com.tigersoft.offerapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tigersoft.offerapp.R.mipmap.ic_launcher;


public class MainActivity extends AppCompatActivity implements LocationListener {


    public final static int STATUS_START = 0;
    public final static int STATUS_FINISH = 1;

    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_TEXT = "buttonText";

    private final String LOG_TAG = "MainLog";

    @BindView(R.id.btnLink)
    Button btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        String text=intent.getStringExtra(PARAM_TEXT);
        btnLink.setText(text==""||text==null?"@string/no_offers":text);

        PendingIntent pi;
        Intent getIntent;

        pi = createPendingResult(3, new Intent(), 0);

        getIntent = new Intent(this, GpsJsonService.class).putExtra(PARAM_PINTENT, pi);
        startService(getIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);

        if (resultCode == STATUS_START) {
            //nothing to do. background task.
        }

        if (resultCode == STATUS_FINISH) {
            String sResult = data.getStringExtra(PARAM_RESULT);
            Gson gson=new GsonBuilder().create();
            JsonBind jsb=gson.fromJson(sResult,JsonBind.class);
            btnLink.setText(jsb.getText());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onLinkClick(View view) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(ic_launcher)
                        .setTicker("Ticker text")//short text
                        .setContentTitle("My notification")//full text title
                        .setContentText("Notify text");//full text notification
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
