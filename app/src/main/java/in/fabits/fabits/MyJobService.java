package in.fabits.fabits;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.NotificationUtils;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {

        if (ApiUtil.getName() == null) {
            Bundle b = job.getExtras();
            String notification = b.getString("NOTIFICATION");
            String message = b.getString("MESSAGE");
            if (message != null)
                isNewMessage(message);
            if (notification != null)
                NotificationListThread(notification);
        }
        return false; // Answers the question: "Is there still work going on?"
    }

    void isNewMessage(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                                String conversation_ID = (String) obj.get("conversation_id");
                                String image = (String) obj.get("image");
                                String name =  obj.getString("names");

                                new NotificationUtils(getBaseContext(),name+": "+ obj.getString("message"), "New Message", image).execute();
                                JSONArray msg = new JSONArray();
                                msg.put(arr.get(i));
                                Preferences.saveMessages(conversation_ID, msg, getBaseContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(getBaseContext()).addToRequestQueue(stringRequest);
    }

    void NotificationListThread(String url1) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.equals("[]") && response != null) {
                                JSONArray arr = new JSONArray(response);
                                JSONObject obb = arr.getJSONObject(0);
                                String img = obb.getString("image");
                                new NotificationUtils(getBaseContext(), obb.getString("message"), "New Notification", img).execute();
                                Preferences.addNotification(arr, getBaseContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}