package com.tigersoft.offerapp;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GpsJsonService extends Service {

    final String LOG_TAG = "ServiceLog";

    Timer timer;
    ExecutorService es;

    public GpsJsonService() {
        super.onCreate();

        Log.d(LOG_TAG, "GpsJsonService onCreate");
        es = Executors.newFixedThreadPool(1);
    }

    public int onStartCommand(Intent intent, int flags, int taskId) {
        Log.d(LOG_TAG, "EvjYahooService onStartCommand");

        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        Exchange exc = new Exchange(pi);
        es.execute(exc);

        return super.onStartCommand(intent, flags, taskId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class Exchange implements Runnable {

        //offers.json on gdrive
        //https://bitbucket.org/!api/2.0/snippets/Tigrend/GezGr/f16808e9d6e75b453e961736497f148f01ea3c00/files/offers.json
        private final String BB_JSON="https://bitbucket.org/!api/2.0/snippets/Tigrend/GezGr/f16808e9d6e75b453e961736497f148f01ea3c00/files/";

        Retrofit retrofit;
        PendingIntent pi;
        Intent intent;

        public Exchange(PendingIntent pi) {
            this.pi = pi;
            Log.d(LOG_TAG, "Exchange created");
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "Exchange started");
            try {
                pi.send(MainActivity.STATUS_START);
            } catch (PendingIntent.CanceledException e) {
                Log.d(LOG_TAG, "Exchange send start status failure");
            }

            if(timer != null){
                timer.cancel();
            }

            Timer myTimer = new Timer(); // Создаем таймер

            myTimer.schedule(new TimerTask() { // Определяем задачу
                @Override
                public void run() {
                    intent = new Intent();
                    getJson();
                }
            }, 0L, 60L * 1000); // интервал - 60000 миллисекунд, 0 миллисекунд до первого запуска.

            /*intent=new Intent();
            getJson();*/
        }

        private void getJson()
        {
            Log.d(LOG_TAG, "Exchange getJson");
            retrofit = new Retrofit.Builder().baseUrl(BB_JSON).addConverterFactory(ScalarsConverterFactory.create()).build();
            GpsJsonEndpointInterface apiService =
                    retrofit.create(GpsJsonEndpointInterface.class);
            Call<String> call = apiService.getJson();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Log.d(LOG_TAG, "Exchange getJson success");
                    intent.putExtra(MainActivity.PARAM_RESULT,response.body().toString());
                    try {
                        pi.send(GpsJsonService.this, MainActivity.STATUS_FINISH, intent);
                    } catch (PendingIntent.CanceledException e) {
                        Log.d(LOG_TAG, "PendingIntent CanceledException:"+e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(LOG_TAG, "Exchange getJson failure");
                }
            });
        }
    }

    public interface GpsJsonEndpointInterface {
        @GET("offers.json")
        Call<String> getJson();
        //Call<String> getJson(@Query("longt") double longt, @Query("lengt") double lengt);
    }
}
