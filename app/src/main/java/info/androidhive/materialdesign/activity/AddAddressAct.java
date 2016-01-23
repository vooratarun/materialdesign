package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.AddressesAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.database.DatabaseAdapter;

public class AddAddressAct extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DatabaseAdapter dbhelper;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000 ;
    private EditText user_line1;
    private  EditText user_line2;
    private Button btn_done;
    private AutoCompleteTextView autoCompView;
    private Context context;
    private String placeId;
    private static final String LOG_TAG = "mytag";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    GoogleApiClient mGoogleApiClient;
    private static List<String> places_List= new ArrayList<String>();
    private static final String API_KEY = "AIzaSyDpkRYf8Jd-dnkF_-2VkiE36q-awxZGo-8";
    private LatLng lat_lang;
    private String address_flag;
    private  String position;
    private LatLng edit_latlang;
    private int address_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "AddAddress" + "</font>"));
        getSupportActionBar().show();
        lat_lang = new LatLng(19.009342,73.234098);

        user_line1  = (EditText) findViewById(R.id.address_line1);
        user_line2 = (EditText) findViewById(R.id.address_line2);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.addresses_list_item));
        autoCompView.setOnItemClickListener(this);

        dbhelper = new DatabaseAdapter(this);

        Bundle bundle = getIntent().getExtras();
        address_flag = bundle.getString("parent");
        if(address_flag.equals("edit_address"))
        {
            position = bundle.getString("position");
            int pos = Integer.parseInt(position);
            edit_latlang= new LatLng( Double.parseDouble(AppConfig.address_list.get(pos).getLat()), Double.parseDouble(AppConfig.address_list.get(pos).getLang()));
            address_id = bundle.getInt("address_id");
            user_line1.setText( bundle.getString("user_line1"));
            user_line2.setText( bundle.getString("user_line2"));
            autoCompView.setVisibility(View.VISIBLE);
            autoCompView.setText(bundle.getString("google_line1"));
        }
        ImageView address_search = (ImageView)findViewById(R.id.address_search);
        btn_done = (Button) findViewById(R.id.but_add_address);
        btn_done.setOnClickListener(this);
        context= getApplicationContext();
        placeId = "ChIJ-zP-eQ_I5zsRp9PGEXKygV0";

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        address_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompView.setVisibility(View.VISIBLE);
                user_line1.setVisibility(View.GONE);
                user_line2.setVisibility(View.GONE);
            }
        });

    }
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        user_line1.setVisibility(View.VISIBLE);
        user_line2.setVisibility(View.VISIBLE);
        String str = (String) adapterView.getItemAtPosition(position);
        placeId = places_List.get(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        Toast.makeText(getApplicationContext(), "Clicked: " +placeId,Toast.LENGTH_SHORT).show();
        Log.i("mytag", "Called getPlaceById to get Place details for " + placeId);
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                places_List.add(predsJsonArray.getJSONObject(i).getString("place_id"));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "connected", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public void onClick(View v) {
        final String line1 = String.valueOf(user_line1.getText());
        final String line2 = String.valueOf(user_line2.getText());
        final String google_line1 = String.valueOf(autoCompView.getText());

        if(line1.length() ==0  || line2.length()==0 || google_line1.length()==0) {
            Toast.makeText(this, "Fill Address first", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject obj = new JSONObject();
        try {
            if(address_flag.equals("add_address"))
            {
            obj.put("user_id",2);
            obj.put("lat",lat_lang.latitude);
            obj.put("long",lat_lang.longitude);
            obj.put("user_line1",line1);
            obj.put("user_line2",line2);
            obj.put("pincode",521324);
            obj.put("google_line1",google_line1);
            obj.put("google_line2","google_lin2");}
            if(address_flag.equals("edit_address"))
            {
                obj.put("id",address_id);
                try{
                obj.put("lat",lat_lang.latitude);
                obj.put("long",lat_lang.longitude);}
                catch (Exception e)
                {
                    obj.put("lat", edit_latlang.latitude);
                    obj.put("long",edit_latlang.longitude);
                    e.printStackTrace();
                }
                obj.put("user_line1",line1);
                obj.put("user_line2",line2);
                obj.put("pincode",521324);
                obj.put("google_line1",google_line1);
                obj.put("google_line2","google_lin2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = AppConfig.add_address;
        if(address_flag.equals("edit_address"))
            url = AppConfig.edit_address;
        Log.d("mytag","request :" + obj.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url,obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag", "respose from server :" + response.toString());
                Toast.makeText(context, " Address Added ", Toast.LENGTH_LONG).show();

                if(address_flag.equals("edit_address")) {
                    int pos = Integer.parseInt(position);
                    try {
                        AppConfig.address_list.get(pos).setLat(String.valueOf(lat_lang.latitude));
                        AppConfig.address_list.get(pos).setLang(String.valueOf(lat_lang.longitude));
                        AppConfig.address_list.get(pos).setUser_line1(String.valueOf(user_line1.getText()));
                        AppConfig.address_list.get(pos).setUser_line2(String.valueOf(user_line2.getText()));
                        AppConfig.address_list.get(pos).setGoogle_line1(String.valueOf(google_line1));
             int count=   dbhelper.updateTable(String.valueOf(address_id),String.valueOf(lat_lang.latitude),String.valueOf(lat_lang.longitude),line1,line2,google_line1);
                    if(count!=0)
                        Toast.makeText(context, "grand success", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {

                    }
                    AllAddressesAct.adapter.notifyDataSetChanged();
                    Log.d("mytag",AppConfig.address_list.get(pos).getGoogle_line1());
                    finish();
                }

                if(address_flag.equals("add_address")) {
                    int address_id=0;
                    try {
                         address_id  = response.getInt("address_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    long id = dbhelper.insert_data(address_id,String.valueOf(lat_lang.longitude),String.valueOf(lat_lang.longitude),line1,line2,google_line1);
                    if(id < 0) Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    else  Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                    Intent data = new Intent();
                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag","errot from server :" + error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API).build();
        if(mGoogleApiClient.isConnected())
        {
            Log.d("mytag"," conneted");
        }
        else
        {Log.d("mytag","not conneted to client");
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("mytag", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            Log.i("mytag", "Place details received: " + place.getName()+ place.getLatLng().toString() );
            lat_lang = place.getLatLng();
            places.release();
        }
    };
}

