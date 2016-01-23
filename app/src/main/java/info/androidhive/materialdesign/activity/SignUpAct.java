package info.androidhive.materialdesign.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.util.Configure;
import info.androidhive.materialdesign.util.ConnectionDetector;

public class SignUpAct extends AppCompatActivity {

    private EditText first_name;
    private EditText second_name;
    private EditText email_id;
    private EditText mob_num;
    private EditText otp_text;
    private Button otp_btn;
    private Button sign_up_btn;
    private String parent;
    private static ProgressDialog pDialog;
    private String otp_from_response;
    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Sign Up" + "</font>"));
        getSupportActionBar().show();

        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("mytag", "GCM RegId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(), "Already Registered with GCM Server!", Toast.LENGTH_LONG).show();
        }

//        parent = getIntent().getExtras().getString("parent");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        first_name = (EditText)findViewById(R.id.first_name);
        second_name =(EditText)findViewById(R.id.last_name);
        email_id = (EditText)findViewById(R.id.email_id);
        mob_num = (EditText)findViewById(R.id.mobile_number);

        otp_btn =(Button)findViewById(R.id.req_opt_but);

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                String mobileNumber = mob_num.getText().toString();
                if(mobileNumber.trim().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Invalid mobile number", Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    return;
                }
                String url = "http://www.instomeal.com/kitchenvilla/request_otp?mobile_number="
                        + mobileNumber;
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,  new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            otp_from_response = response.getString("otp");
                            Toast.makeText(SignUpAct.this, "otp :"+otp_from_response, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        return super.parseNetworkResponse(response);
                    }
                };

                if(ConnectionDetector.isConnectingToInternet()) AppController.getInstance().addToRequestQueue(req);
                else {
                    ConnectionDetector.noConnectionDialog(SignUpAct.this);
                }
            }
        });
        sign_up_btn = (Button)findViewById(R.id.sign_up);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp_text =(EditText)findViewById(R.id.otp_text);
                String otp_entered  = otp_text.getText().toString();
                if(first_name.getText().toString().length() == 0) {first_name.setError("Fill this field");return;}
                if(second_name.getText().toString().length() == 0) {second_name.setError("Fill this field");return;}
                if(email_id.getText().toString().length() == 0) {email_id.setError("Fill this field");return;}
                if(mob_num.getText().toString().length() == 0) {mob_num.setError("Fill this field");return;}
                if(otp_text.getText().toString().length() == 0) {otp_text.setError("Fill this field");return;}

                if(otp_from_response.equals(otp_entered)){
                    JSONObject obj  = null;
                    try{
                        obj = new JSONObject();
                        obj.put("first_name",first_name.getText().toString());
                        obj.put("last_name",second_name.getText().toString());
                        obj.put("email",email_id.getText().toString());
                        obj.put("mobile",mob_num.getText().toString());
                        obj.put("reg_id",regId.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.sign_up, obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //after server call , sends response the
                            try {
                                int user_id = response.getInt("user_id");
                                finish();
                                if(parent=="cart_items") {
                                    Intent intent = new Intent(getApplicationContext(), AddAddressAct.class);
                                    intent.putExtra("parent","add_address");
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if(networkResponse.statusCode == 400)
                                Log.d("mytag","error code");
                        }
                    }){
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                           int  code = response.statusCode;
                            try {
                                Log.d("mytag", "[raw json]: " + code + " "+ (new String(response.data)));
                                Gson gson = new Gson();
                                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                Log.d("mytag",json);
                                return super.parseNetworkResponse(response);
                            } catch (UnsupportedEncodingException e) {
                                return Response.error(new ParseError(e));
                            } catch (JsonSyntaxException e) {
                                return Response.error(new ParseError(e));
                            }
                        }
                    };
                    if(ConnectionDetector.isConnectingToInternet()) AppController.getInstance().addToRequestQueue(req);
                    else {
                        ConnectionDetector.noConnectionDialog(SignUpAct.this);
                    }
                }
                else {
                    Toast.makeText(SignUpAct.this, "Check ur OTP once", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }
    private static void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private static void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("mytag",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<String,Void,String>() {

            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Configure.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(MainGCMActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.commit();
    }

}
