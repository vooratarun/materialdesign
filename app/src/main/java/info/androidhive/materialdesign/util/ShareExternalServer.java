package info.androidhive.materialdesign.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.materialdesign.app.AppController;

public class ShareExternalServer {
    String result2;

	public String shareRegIdWithAppServer(final Context context,
			final String regId) {

		String result = "";
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("regId", regId);
		try {
            StringRequest req = new StringRequest(Request.Method.POST,Configure.APP_SERVER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("mytag","response from server"+response.toString());
                    result2 = response.toString();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("mytag","error from server");

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("regId", regId);
                    return paramsMap;
                }
            };
            AppController.getInstance().addToRequestQueue(req);
			result =result2;

        } catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
}
