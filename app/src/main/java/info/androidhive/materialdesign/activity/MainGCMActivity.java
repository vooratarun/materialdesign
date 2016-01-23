package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.util.ShareExternalServer;

public class MainGCMActivity extends AppCompatActivity {

    ShareExternalServer appUtil;
    String regId;
    AsyncTask shareRegidTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gcm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appUtil = new ShareExternalServer();

        regId = getIntent().getStringExtra("regId");
        Log.d("MainActivity", "regId: " + regId);

        final Context context = this;

        String result = appUtil.shareRegIdWithAppServer(context, regId);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

    }
}