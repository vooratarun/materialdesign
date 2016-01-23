package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;

public class OrderCancleAct extends Activity implements View.OnClickListener {

    private EditText conform_text;
    private Button button;
    private int order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_order_cancle);

        order_id = getIntent().getExtras().getInt("order_id");
        conform_text = (EditText)findViewById(R.id.order_cancle_text);
        button = (Button)findViewById(R.id.conform_btn);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this,"done" + order_id ,Toast.LENGTH_SHORT).show();
        JSONObject obj = null;
        try{
            obj = new JSONObject();
            obj.put("order_id",order_id);
            obj.put("reason","something");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("mytag", "obj " + obj.toString());

        StringRequest req = new StringRequest(Request.Method.POST,"",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

               HashMap<String,String> params   = new HashMap<>();
                params.put("order_id",String.valueOf(order_id));
                params.put("reason","something");
                return  params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }
}
