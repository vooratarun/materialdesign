package info.androidhive.materialdesign.activity;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import info.androidhive.materialdesign.R;
import android.widget.SearchView;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
private  String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        Intent intent = new  Intent(this,FullMenuActivity.class);
        intent.putExtra("parent","mapAct");
        intent.putExtra("query",query);
        startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            this.query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("mytag","INTENT STRING CALLED" + query);
            //use the query to search
        }
    }


}
