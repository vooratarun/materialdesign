package info.androidhive.materialdesign.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.Locations;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener
        {

    private static String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Locations> LocationsList = new ArrayList<Locations>(); ;
    private String url = AppConfig.vans_list;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    // LogCat tag
    SharedPreferences allDetails;
    SharedPreferences.Editor AllEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        allDetails = getSharedPreferences("allDetails", 0);
        AllEditor = allDetails.edit();

        AllEditor.putString("class","main");
        displayView(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                Intent intent = new Intent(this,MapVansActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this,FullMenuActivity.class);
                intent1.putExtra("parent","mainAct");
                startActivity(intent1);

                break;
            case 2:
                Intent intent3 = new Intent(this,SelectPlaceAct.class);
                startActivity(intent3);
                break;

            case 3:
                Intent intent5 = new Intent(this,OrderHistoryAct.class);
                startActivity(intent5);
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

}