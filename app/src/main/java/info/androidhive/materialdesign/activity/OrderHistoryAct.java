package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.OrderHistoryAdaper;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.OrderHistoyItem;

public class OrderHistoryAct extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private ListView listView;
    private OrderHistoryAdaper adaper;
    private List<OrderHistoyItem> order_history_list = new ArrayList<OrderHistoyItem>();

    private  Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Order History" + "</font>"));
        getSupportActionBar().show();
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        listView = (ListView) findViewById(R.id.order_history_list_view);
        adaper = new OrderHistoryAdaper(this,order_history_list);
        listView.setAdapter(adaper);
        try {
            get_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_list() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("user_id", "1");
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, "http://instomeal.com/kitchenvilla/orders_history?user_id=1", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("mytag"," Response from server" + response.toString());
                for(int i=0;i<response.length();i++)
                {
                    JSONObject obj = null;
                    try{
                        obj = response.getJSONObject(i);
                        OrderHistoyItem item = new OrderHistoyItem();
                        item.setAddress("address 1");
                        item.setPrice(Double.valueOf(obj.getString("overall_discounted_price")));
                        item.setTime(obj.getString("promised_delivery_time"));
                        item.setStatus(obj.getString("order_status"));
                        item.setOrder_id(obj.getInt("order_id"));
                        order_history_list.add(item);
                    Log.d("mytag"," size :" + order_history_list.size());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adaper.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag","error response" +error.toString());
            }
        }){
            Map <String,String> params = new HashMap<String ,String>();
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("user_id","1");
                    return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);

    }
    private void displayView(int position) {

        Intent intent;
        switch (position) {
            case 0:
                //home map vans
                intent = new Intent(this,MapVansActivity.class);
                startActivity(intent);
                break;
            case 1:
                // profile page
                intent = new Intent(this,ProfilePageAct.class);
                intent.putExtra("parent","mainAct");
                startActivity(intent);
                break;

            case 2:
                // order history
                intent = new Intent(this,OrderHistoryAct.class);
                startActivity(intent);
                break;
            case 3:
                // order tracking
                 intent = new Intent(this,OrderTrackAct.class);
                startActivity(intent);
                break;
            case 4:
                // refer & earn
                intent = new Intent(this,ReferEarnAct.class);
                startActivity(intent);
                break;
            case 5:
                //Contact Us
                intent = new Intent(this,ContactUsAct.class);
                startActivity(intent);
                break;
            case 6:
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
