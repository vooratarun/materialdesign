package info.androidhive.materialdesign.activity;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.DatabaseAdapter;

public class DatabaseAct extends AppCompatActivity {

    DatabaseAdapter dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        dbhelper = new DatabaseAdapter(this);
    }

    public void add_user(View view)
    {

        long id = dbhelper.insert_data(1,"98.435334","53.43342","line1","line2","google_line1");
        if(id < 0)
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
        }

    }
    public void viewDetails(View view)
    {
      String data =  dbhelper.getAllData();
        Toast.makeText(DatabaseAct.this, data, Toast.LENGTH_SHORT).show();

    }
}
