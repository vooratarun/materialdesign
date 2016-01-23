package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.AddressesAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.database.DatabaseAdapter;
import info.androidhive.materialdesign.model.UserAddress;

public class AllAddressesAct extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private List<UserAddress> addresses_list = AppConfig.address_list ;
    private ListView listView;
    public static  AddressesAdapter adapter;
    private RadioGroup address_group;
    private String selected_address="";
    private Button  proceed;
    DatabaseAdapter dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_addresses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "All Addresses" + "</font>"));
        getSupportActionBar().show();

        listView = (ListView) findViewById(R.id.all_address_list_view);
        adapter = new AddressesAdapter(this, AppConfig.address_list);
        listView.setAdapter(adapter);
        dbhelper = new DatabaseAdapter(this);
        String data = dbhelper.getAllData();
        adapter.notifyDataSetChanged();

        proceed =  (Button)findViewById(R.id.address_proceed);
        Bundle bundle = getIntent().getExtras();
        String parent = bundle.getString("parent");
        if(parent.equals("profile_page")) {
            AppConfig.all_address_adapter_check = "profile_page";
            proceed.setVisibility(View.GONE);
        }
        else
            AppConfig.all_address_adapter_check ="all_addresses";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllAddressesAct.this, AddAddressAct.class);
                intent.putExtra("parent", "add_address");
                startActivityForResult(intent, 1000);
            }
        });
      //  add_to_list();
       // address_group = (RadioGroup) findViewById(R.id.address_radio_group);
       // address_group.setOnCheckedChangeListener(this);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(AddressesAdapter.flag_checked == 0)
                   Toast.makeText(getApplicationContext(),"Select or add address",Toast.LENGTH_SHORT).show();
               else
               {
                   Toast.makeText(AllAddressesAct.this, AppConfig.selected_address.getUser_line1(), Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(AllAddressesAct.this,MakePaymentAct.class);
                   startActivity(intent);
               }
            }
        });
    }

    private void add_to_list() {
        for (int i = 0; i < addresses_list.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            Log.d("mytag", "id : " + i + "name :" + i);
            //StringBuilder str = new StringBuilder();
            //str.append(addresses_list.get(i).getUser_line1() + "\n" + addresses_list.get(i).getUser_line2());
            String line1 = addresses_list.get(i).getUser_line1();
            String line2 = addresses_list.get(i).getUser_line2();
            String line3 = addresses_list.get(i).getGoogle_line1();
            rdbtn.setId(i);
            rdbtn.setText(Html.fromHtml("<font color=black size=3em>" + line1 + "<br>" + line2 + "<br></font>" + "<font color=grey size=3em>" + line3 + "</font><br>"));
          //  ((ViewGroup) findViewById(R.id.address_radio_group)).addView(rdbtn);
        }

    }
    public void get_list()
    {


        // AppConfig.address_list.clear();
       /* String url = "http://instomeal.com/kitchenvilla/get_addresses?user_id=5";
        JsonArrayRequest req  = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    Log.d("mytag",response.toString());
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject obj = response.getJSONObject(i);
                        UserAddress single = new UserAddress();
                        single.setId(obj.getInt("address_id"));
                        single.setLat(obj.getString("lat"));
                        single.setLang(obj.getString("long"));
                        single.setUser_line1(obj.getString("user_line1"));
                        single.setUser_line2(obj.getString("user_line2"));
                        single.setPincode(obj.getString("pincode"));
                        single.setGoogle_line1(obj.getString("google_line1"));
                        single.setGoogle_line2(obj.getString("google_line2"));
                        AppConfig.address_list.add(single);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance().addToRequestQueue(req);

        */
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton address = (RadioButton)group.findViewById(checkedId);
        selected_address = address.getText().toString();
        Toast.makeText(this,address.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1000)
        {
            AppConfig.address_list.clear();
            this.finish();
            startActivity(getIntent());

        }
    }
}
