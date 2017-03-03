package com.tigersoft.offerapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity{


    public final static int STATUS_START = 0;
    public final static int STATUS_FINISH = 1;

    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_TEXT = "buttonText";

    private final String LOG_TAG = "MainLog";

    private String _link=getString(R.string.default_link);

    @BindView(R.id.btnLink)
    Button btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        String text=intent.getStringExtra(PARAM_RESULT);
        Gson gson=new GsonBuilder().create();
        JsonBind jsb=gson.fromJson(text,JsonBind.class);
        _link=jsb.getLink();
        btnLink.setText(text==""||text==null?"@string/no_offers":jsb.getText());

        PendingIntent pi;
        Intent getIntent;

        pi = createPendingResult(3, new Intent(), 0);

        getIntent = new Intent(this, GpsJsonService.class).putExtra(PARAM_PINTENT, pi);
        startService(getIntent);
    }

    public void onLinkClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_link));
        startActivity(browserIntent);
    }
}
