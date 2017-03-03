package com.tigersoft.offerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.tigersoft.offerapp.MainActivity.PARAM_RESULT;
import static com.tigersoft.offerapp.MainActivity.STATUS_FINISH;
import static com.tigersoft.offerapp.MainActivity.STATUS_START;

public class EmptyActivity extends AppCompatActivity {

    private final String LOG_TAG = "EmptyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_empty);

        Intent getIntent;

        getIntent = new Intent(this, GpsJsonService.class);
        startService(getIntent);
        finish();
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

            Intent intent = new Intent(this,MainActivity.class).putExtra(MainActivity.PARAM_TEXT,jsb.getText());
            startActivity(intent);

            //btnLink.setText(jsb.getText());
        }
    }
}
