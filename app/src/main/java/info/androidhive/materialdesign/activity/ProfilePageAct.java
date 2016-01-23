package info.androidhive.materialdesign.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import info.androidhive.materialdesign.R;

public class ProfilePageAct extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    private FragmentDrawer drawerFragment;
    private Toolbar  mToolbar;
    private TextView personal_info;
    private TextView saved_addresses;
    private TextView wallet_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Profile" + "</font>"));
        getSupportActionBar().show();
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        personal_info = (TextView)findViewById(R.id.personal_info);
        personal_info.setOnClickListener(this);
        saved_addresses =(TextView)findViewById(R.id.saved_addresses);
        saved_addresses.setOnClickListener(this);
        wallet_money =(TextView)findViewById(R.id.wallet_money);
        wallet_money.setOnClickListener(this);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.personal_info:{
                 intent = new Intent(this,PersonalInfoAct.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
                 break;
            }
            case R.id.saved_addresses:
            {
                intent = new Intent(this,AllAddressesAct.class);
                intent.putExtra("parent","profile_page");
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
                break;
            }
            case R.id.wallet_money:
                break;
            case R.id.signout_but:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
