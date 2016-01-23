package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppController;

public class OrderTrackAct extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Track Order" + "</font>"));
        getSupportActionBar().show();
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        track_order();
    }

    private void track_order() {
        String url ="http://instomeal.com/kitchenvilla/track_an_order?&order_id=1";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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
