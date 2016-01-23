package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import info.androidhive.materialdesign.R;

public class ReferEarnAct extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private Button refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_earn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Refer & Earn" + "</font>"));
        getSupportActionBar().show();
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        refer = (Button)findViewById(R.id.invite_frnds);
        refer.setOnClickListener(this);

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
    public void onClick(View v) {
        //create the send intent
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        //set the type
        shareIntent.setType("text/plain");

        //add a subject
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Refer");

        //build the body of the message to be shared
        String shareMessage = "Share the app";

        //add the message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent, "Refer app"));
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}
