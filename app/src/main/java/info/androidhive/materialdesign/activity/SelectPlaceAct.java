package info.androidhive.materialdesign.activity;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.util.PlaceProvider;

@SuppressWarnings("ResourceType")
public class SelectPlaceAct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ChooseLocation");
        getSupportActionBar().show();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        AppConfig.parent_context = getApplicationContext();

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null )
            Toast.makeText(this,"Location changed",Toast.LENGTH_LONG).show();
        handleIntent(getIntent());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchMenuItem = menu.findItem( R.id.action_search ); // get my MenuItem with placeholder submenu
        searchMenuItem.expandActionView();
      //  MenuItemCompat.expandActionView(searchMenuItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                onSearchRequested();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
        else if(Intent.ACTION_VIEW.equals(intent.getAction()))
        {
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void doSearch(String query){
        Log.d("mytag", " Do search block" + query);
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query){
        Log.d("mytag", "Query Block" + query);
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        Log.d("mytag", "Loader Cal");
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        showLocations(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }

    private void showLocations(Cursor c)
    {
        String array[] =c.getColumnNames();
        int i = 0;
        String area = "";
        c.moveToNext();
        area = area + c.getString(c.getColumnIndex(array[0]));
        String lat = c.getString(c.getColumnIndex(array[1]));
        String lang = c.getString(c.getColumnIndex(array[2]));
        Toast.makeText(this,area + " " +lat + " "+ lang,Toast.LENGTH_LONG).show();
        Activity parentActivity;
        parentActivity=(Activity)AppConfig.parent_context;
        parentActivity.finish();
        this.finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}