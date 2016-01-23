package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppController;

public class LoginAct extends AppCompatActivity {
    private String parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Log In" + "</font>"));
        getSupportActionBar().show();

        Bundle bundle = getIntent().getExtras();
        parent = bundle.getString("parent");

        EditText mobile_num = (EditText) findViewById(R.id.mobile_number_opt);
        EditText otp_text = (EditText) findViewById(R.id.opt_text);
        Button opt_btn = (Button)findViewById(R.id.request_opt_but);
        Button login_btn = (Button)findViewById(R.id.login_but);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parent.equals("cart_items"))
                {
                 setResult(RESULT_OK);  // have to put after login success , for checking purpose  i put here
                    finish();
                }

            }
        });

        Button sign_up_button = (Button) findViewById(R.id.signup_but);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAct.this,SignUpAct.class);
                if(parent.equals("cart_items"))
                    intent.putExtra("parent",parent);
                else
                    intent.putExtra("parent","login");
                startActivity(intent);
            }
        });

      //  login();
    }
    public void login() {
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj.put("name", "Manideep Polireddi");
            obj.put("email", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

}