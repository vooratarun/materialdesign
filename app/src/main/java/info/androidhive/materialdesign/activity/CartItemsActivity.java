package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.CartItemsAdapter;
import info.androidhive.materialdesign.adapter.CartItemsNewAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.CartItem;
import info.androidhive.materialdesign.model.CouponDetails;

public class CartItemsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private CartItemsNewAdapter adapter;

    private TextView home_button;
    private TextView add_more_meals;
    public static TextView total;

    private TextView checkout;
    private Button coupon_apply;

    private CouponDetails  coupon_item;
    private Context context;
    private EditText coupon_code;

    private String coupon;
    private  double total_price;


    private TextView cart_items_number;
    private ViewGroup discount_price_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Cart Items" + "</font>"));
        getSupportActionBar().show();

        listView = (ListView) findViewById(R.id.cart_item_listview);
       // adapter = new CartItemsAdapter(this, AppConfig.cart_items_list);
        adapter = new CartItemsNewAdapter(this, AppConfig.cart_items_list);

        listView.setAdapter(adapter);
        cart_items_number = (TextView)findViewById(R.id.cart_items_number);
        cart_items_number.setText(String.valueOf(AppConfig.cart_items_list.size()));

        coupon_code = (EditText) findViewById(R.id.coupon_code);
        coupon_apply = (Button) findViewById(R.id.coupon_apply);
        coupon_apply.setOnClickListener(this);
        checkout  = (TextView) findViewById(R.id.check_btn);
        checkout.setOnClickListener(this);
        total = (TextView) findViewById(R.id.cart_total_price);
        total_price = 0;
        for(int i=0;i<AppConfig.cart_items_list.size();i++)
            total_price = total_price + AppConfig.cart_items_list.get(i).getTotal_meal_price();

        total.setText(String.valueOf(total_price));
        context = getApplicationContext();

        discount_price_section = (ViewGroup) findViewById(R.id.discount_price_section);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.check_btn:
            {
                if(AppConfig.login_check)
                    checkout_json();
                else{
                    Intent intent = new Intent(this,LoginAct.class);
                    intent.putExtra("parent","cart_items");
                    startActivityForResult(intent, 1000);
                }
                break;
            }
            case R.id.coupon_apply:
            {
                int flag = 0;
                coupon = String.valueOf(coupon_code.getText());
                if( coupon.length()==0 || coupon.equals(" ")){
                    Toast.makeText(this,"Enter Coupon Code",Toast.LENGTH_SHORT).show();
                }
                else if(coupon.length()>0)
                {
                    for(int i=0;i<AppConfig.applied_coupons.size();i++)
                    {
                            if(coupon.equals(AppConfig.applied_coupons.get(i)))
                            {   flag =1;break;}
                    }
                    if(flag == 1)
                        Toast.makeText(this,"Coupon Already Applied",Toast.LENGTH_SHORT).show();
                    else
                        coupon_json();
                }

                break;
            }
            default:
                break;
        }
    }
    private void checkout_json() {

        JSONArray cart_items = new JSONArray();
        for(int i=0;i<AppConfig.cart_items_list.size();i++)
        {
            CartItem item ;
            item = AppConfig.cart_items_list.get(i);
            Log.d("mytag"," item :" + item.getMeal_id() + " "+ item.toString());
            JSONObject post_item  = null;
             try{
                 post_item = new JSONObject();
              post_item.put("van_meal_id",AppConfig.cart_items_list.get(i).getMeal_id());
              post_item.put("meal_base","rice");
              post_item.put("quantity",item.getMeal_order_quantity());
              post_item.put("discounted_price",item.getTotal_meal_price());
             } catch (JSONException e) {
                 e.printStackTrace();
             }
            cart_items.put(post_item);
        }
        JSONObject order = null;
        try{
        order = new JSONObject();
        order.put("user_id",2);
        order.put("cart_items",cart_items);
        order.put("overall_discounted_price",300);
        order.put("coupon_applied","CODE");
        order.put("promised_delivery_time","12942304234932");
        order.put("scheduled",false);
        order.put("address_id",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("mytag", " ORDER JSON :" + order);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.checkout,order, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag","respose from server :" + response.toString());
                Intent intent = new Intent(CartItemsActivity.this,AllAddressesAct.class);
                intent.putExtra("parent","cart_items");
                startActivity(intent);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag","errot from server :" + error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    private void coupon_json() {
        JSONArray cart_items = new JSONArray();
        for(int i=0;i<AppConfig.cart_items_list.size();i++)
        {
            CartItem item ;
            item = AppConfig.cart_items_list.get(i);
            Log.d("mytag"," item :" + item.getMeal_id() + " "+ item.toString());
            JSONObject post_item  = null;
            try{
                post_item = new JSONObject();
                post_item.put("van_meal_id",AppConfig.cart_items_list.get(i).getMeal_id());
                post_item.put("meal_base","parrota");
                post_item.put("quantity",item.getMeal_order_quantity());
                post_item.put("meal_price",item.getTotal_meal_price());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cart_items.put(post_item);
        }
        JSONObject order = null;
        try{
            order = new JSONObject();
            order.put("overall_price",total_price);
            order.put("cart_items",cart_items);
            order.put("coupon_applied",coupon);
            order.put("scheduled",false);
            order.put("address_id",12);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("mytag", " COUPON JSON :" + order);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.coupon_apply,order, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getApplicationContext(),"coupon applied",Toast.LENGTH_SHORT).show();
                Log.d("mytag","respose from server :" + response.toString());
                try{
                   coupon_item = new CouponDetails(response.getString("message"),response.getString("success"),response.getInt("discount"),coupon);
                    AppConfig.applied_coupons.add(coupon);
                    Log.d("mytag", "coupon : " + coupon_item.getMessage());
                    Toast.makeText(context,"discount :"+response.getInt("discount"),Toast.LENGTH_SHORT).show();

                   // ((ViewGroup)findViewById(R.id.coupon_section)).setVisibility(View.INVISIBLE);
                    discount_price_section.setVisibility(View.VISIBLE);

                    total_price = total_price - response.getInt("discount") ;
                    ((TextView) discount_price_section.findViewById(R.id.cart_total_discount_price)).setText(String.valueOf(total_price));
                    total.setPaintFlags(total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag","errot from server :" + error.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== RESULT_OK &&requestCode == 1000)
            checkout_json();
    }
}