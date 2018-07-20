package com.example.ashwinchauhan.imagegallrey.app;

import android.app.Application;
import android.text.TextUtils;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.SynchronousQueue;

public class AppController extends Application {

    public static final String TAG=AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized AppController getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue==null)
        {
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> reg, String tag)
    {
        reg.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(reg);
    }

    public <T> void addToRequestQueue(Request<T> reg)
    {
        reg.setTag(TAG);
        getRequestQueue().add(reg);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}