package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class NetworkHandler {

    RequestQueue queue = null;

    private static final NetworkHandler ourInstance = new NetworkHandler();

    private NetworkHandler() {
    }

    public static NetworkHandler getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
    }

    public static void sendStringRequest(String data, Response.Listener<String> onResponseListener, Response.ErrorListener onErrorResponseListener) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "127.0.0.1", onResponseListener, onErrorResponseListener);

        // Add the request to the RequestQueue.
        ourInstance.queue.add(stringRequest);
    }
}
