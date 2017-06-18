package com.tokbox.android.tutorials.basicvideochat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

/**
 * Created by tarun on 6/18/2017.
 */

public class TokActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener, SubscriberKit.SubscriberListener{
    private static String API_KEY = "45884652";
    //private static String SESSION_ID = "1_MX40NTg4NDY1Mn5-MTQ5Njc5OTY3NDQwNn5wY1lpMHdpMzZjYytQMzJ5WHF1cEpJeUl-UH4";
    // private static String TOKEN = "T1==cGFydG5lcl9pZD00NTg4NDY1MiZzaWc9OTQxYTk5MzQwYjgwNjJhMWU4NjM0MzA1NDY5YmMyYWUyMjVkY2Q2NjpzZXNzaW9uX2lkPTFfTVg0ME5UZzRORFkxTW41LU1UUTVOamM1T1RZM05EUXdObjV3WTFscE1IZHBNelpqWXl0UU16SjVXSEYxY0VwSmVVbC1VSDQmY3JlYXRlX3RpbWU9MTQ5Njg0MTA0NiZub25jZT0wLjQ2NDEwMDY2Mzc3NzAxMjg3JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE0OTY4NDQ2NDM=";
    public static final String LOG_TAG = TokActivity.class.getSimpleName();
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private String url = "https://revel-168913.firebaseapp.com/session";
    private String TAG = TokActivity.class.getName();
    private RequestQueue l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        l = Volley.newRequestQueue(this);

        mSession = new Session.Builder(this, OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID).build();
        mSession.setSessionListener(this);
        mSession.connect(OpenTokConfig.TOKEN);
    }

  /*  private void getSessionId() {
        l.cancelAll(TAG);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String[] sessTok = s.split(" ");
                        Constants.TOK_SESSION_ID = sessTok[0];
                        Constants.TOK_TOKEN = sessTok[1];
                        Log.e(TAG,s);
                        getToken();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.toString());
            }
        });

        l.add(stringRequest);
        l.start();
    }*/

    private void getToken() {

    }



    @Override
    public void onConnected(Session session) {
        Log.e(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.e(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Received");
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Dropped");
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriber) {
        mSubscriberViewContainer.addView(subscriber.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriber) {
        Log.i(LOG_TAG, "Subscriber Disconnected");
    }

    @Override
    public void onError(SubscriberKit subscriber, OpentokError opentokError) {
        Log.e(LOG_TAG, "Subscriber error: " + opentokError.getMessage());
    }
}

