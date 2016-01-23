package info.androidhive.materialdesign.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.ExampleAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.Locations;



public class MapVansActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, SearchView.OnQueryTextListener, LocationListener, GoogleMap.OnCameraChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FragmentDrawer.FragmentDrawerListener, GoogleMap.OnMarkerClickListener {
    // Google Map
    private GoogleMap googleMap;
    private List<Locations> LocationsList = new ArrayList<>();
    private Map<String,Integer> dish_count = new HashMap<String,Integer>();

    private String url = AppConfig.vans_list;
    private Geocoder geocoder;
    List<Address> addresses;

    private ViewGroup hiddenPanel;
    private ViewGroup hiddenPanel2;
    private ViewGroup hiddenPanel3;
    private ViewGroup hiddenPanel4;
    private ImageView button1;
    private ImageView button2;
    private ImageView button3;
    private ImageView button4;
    private TextView but_txt_1;
    private TextView but_txt_2;
    private TextView but_txt_3;
    private TextView but_txt_4;
    private TextView full_menu_dishes;
    private TextView quick_menu_dishes;
    private TextView quick_menu_text;
    private TextView full_menu_price;
    private TextView present_loc_text;
    private Button present_loc_btn;

    private boolean isPanelShown1;
    private boolean isPanelShown2;
    private boolean isPanelShown3;
    private ViewGroup root;
    private Menu menu;
    private List<String> items = new ArrayList<String>();
    private RadioGroup filter1;
    private RadioGroup filter2;
    private RadioGroup filter3;

    private RadioButton filter1_radio_but;
    private Toolbar mToolbar;

    private ViewGroup but_full_menu;
    private ViewGroup but_minute_menu;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    public static LatLng latLng;
    private LatLng center;

    private Bitmap icon;

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyDpkRYf8Jd-dnkF_-2VkiE36q-awxZGo-8";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FragmentDrawer drawerFragment;
    private LocationManager locationManager;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_vans);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.title_home) + "</font>"));
        getSupportActionBar().show();
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        items.add("chicken");items.add("chick");items.add("biryani");items.add("tarun");
        present_loc_text = (TextView) findViewById(R.id.present_loc_text);
         icon = BitmapFactory.decodeResource(getResources(), R.drawable.user_location2); //Converting drawable into bitmap
         icon = resizeBitmapImageFn(icon, 200);
        ImageView loc_image = (ImageView)findViewById(R.id.loc_Image);
        loc_image.getLayoutParams().height = 150;
        loc_image.getLayoutParams().width = 150;
        loc_image.requestLayout();

        try {

            Log.d("mytag", "Try block to initialize map");
            initilizeMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.setOnCameraChangeListener(this);
            googleMap.setOnMarkerClickListener(this);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,80); // size of button in dp
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            View mapView = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getView();
            View btnMyLocation = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            btnMyLocation.setLayoutParams(params);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                onLocationChanged(location);
                get_list();
            }
            else
            {
                Toast.makeText(MapVansActivity.this, "location is null,connecting to api client", Toast.LENGTH_SHORT).show();
                buildGoogleApiClient();

            }

            try {
                new GetLocationAsync(location.getLatitude(), location.getLongitude())
                        .execute();

            } catch (Exception e) {
            }
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

             present_loc_text.setText(String.valueOf(address));


        } catch (Exception e) {
            e.printStackTrace();
        }
        root = (ViewGroup) findViewById(R.id.taus);

        isPanelShown1 = false;
        isPanelShown2 = false;
        isPanelShown3 = false;

        button1 = (ImageView) findViewById(R.id.slideButton);
        button2 = (ImageView) findViewById(R.id.slideButton2);
        button3 = (ImageView) findViewById(R.id.slideButton3);
        button4 = (ImageView)findViewById(R.id.slideButton4);
       // int width = 60;
       // int height = 60;
       // LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
      //  button1.setLayoutParams(parms);
      //  button2.setLayoutParams(parms);
       // button3.setLayoutParams(parms);
       // button4.setLayoutParams(parms);

        but_txt_1 = (TextView) findViewById(R.id.veg_text);
        but_txt_2 = (TextView) findViewById(R.id.home_text);
        but_txt_3 = (TextView) findViewById(R.id.cuis_text);
        but_txt_4 = (TextView) findViewById(R.id.reset_text);

        but_full_menu = (ViewGroup) findViewById(R.id.layout_full_menu);
        but_full_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapVansActivity.this,FullMenuActivity.class);
                intent.putExtra("parent","mainAct");
                startActivity(intent);
            }
        });
        full_menu_dishes = (TextView) findViewById(R.id.full_menu_dishes1);
        quick_menu_dishes = (TextView) findViewById(R.id.minute_menu_dishes);
        quick_menu_text = (TextView) findViewById(R.id.mintext);
        full_menu_price = (TextView) findViewById(R.id.full_menu_price2);
        full_menu_price.setText("\u20B9" + 300);

        present_loc_btn = (Button) findViewById(R.id.present_loc_btn);
        present_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MapVansActivity.this,EditPlaceAct.class);
                intent.putExtra("parent","map_vans");
                startActivityForResult(intent, 1000);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();

    }

    /**
     * function to load map If map is not created it will create it for you
     * */
    private void initilizeMap() {
        Log.d("mytag", "Initialize Map block");
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    public void get_list()
    {
        JSONObject obj =null;
        try {
            obj = new JSONObject();
            obj.put("lat", "19.042323");
            obj.put("long","73.069274");
            obj.put("meal_type","both");
            obj.put("meal_source","both");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url= "http://instomeal.com/kitchenvilla/vans_details?" +
                "lat="+ String.valueOf(latLng.latitude) +
                "&meal_type=both" +
                "&long="+ String.valueOf(latLng.longitude) +
                "&meal_source=both";
        Log.d("mytag", "get List function"+ url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag","response from server"+ response.toString());
                        JSONArray array = null;
                        try {
                            array = response.getJSONArray("vans");
                            dish_count.put("full_menu_count",response.getInt("full_menu_count"));
                            dish_count.put("quick_menu_count",response.getInt("quick_menu_count"));
                            dish_count.put("quick_menu_time",response.getInt("quick_menu_time"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Parsing json
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                System.out.println(obj.toString());
                                Log.d("mytag","data: " + obj.toString());
                                //new Locations(obj.getInt("van_id"),obj.(Double)getString("lat"),obj.getString("long"));
                                int van_id = obj.getInt("van_id");
                                double lat  = Double.parseDouble(obj.getString("van_lat"));
                                double longi = Double.parseDouble(obj.getString("van_lng"));
                                Locations loc = new Locations();
                                loc.setVan_id(van_id);
                                loc.setLatitude(lat);
                                loc.setLongitue(longi);
                                loc.setTime_to_user(obj.getInt("time_to_reach_user_in_min"));
                                LocationsList.add(loc);
                                Log.d("mytag", "loc " + loc.getLatitude() + "  " + loc.getLatitude() + LocationsList.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("mytag","data set updated from server ");
                        update_map();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("mytag", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }
    public void update_map() {
        Log.d("mytag", "Update map function: size :  "+ LocationsList.size());
        for (int i = 0; i < LocationsList.size(); i++) {
            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(LocationsList.get(i).getLatitude(),LocationsList.get(i).getLongitude())).title("v"+LocationsList.get(i).getVan_id());

            marker.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.truck_location4, "20 min,16 items")));
            googleMap.addMarker(marker);
            Toast.makeText(MapVansActivity.this, "marker added", Toast.LENGTH_SHORT).show();
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i=0; i<LocationsList.size();i++)
                {
                    String title = "v"+LocationsList.get(i).getVan_id();
                    if(marker.getTitle().equals(title))
                    {
                        int clicked_van_id = LocationsList.get(i).getVan_id();
                        Toast.makeText(getApplicationContext(),"van "+ clicked_van_id,Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        full_menu_dishes.setText(String.valueOf(dish_count.get("full_menu_count")));
        quick_menu_dishes.setText(String.valueOf(dish_count.get("quick_menu_count")));
        quick_menu_text.setText(String.valueOf(dish_count.get("quick_menu_time")) + " Minute Menu");
    }

    public void slideUpDown(final View view) {
        isPanelShown2 = false;
        isPanelShown3 = false;
        if (!isPanelShown1) {

            hiddenPanel = (ViewGroup) getLayoutInflater().inflate(R.layout.hidden_panel, root, false);
            try{
                root.removeView(hiddenPanel4);
                root.removeView(hiddenPanel2);
                root.removeView(hiddenPanel3);}
            catch (Exception e)
            {
                e.printStackTrace();
            }
            root.addView(hiddenPanel);
            hiddenPanel.setVisibility(View.VISIBLE);
            hiddenPanel.bringToFront();
           // Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up_map_vans);
            //hiddenPanel.startAnimation(bottomUp);
            filter1 = (RadioGroup) hiddenPanel.findViewById(R.id.filter1);
            filter1.setOnCheckedChangeListener(this);

            isPanelShown1 = true;
        } else {
            isPanelShown1 = false;
            root.removeView(hiddenPanel);
        }
    }

    public void slideUpDown2(final View view) {

        isPanelShown1 = false;
        isPanelShown3 = false;
        if (!isPanelShown2) {
            hiddenPanel2 = (ViewGroup) getLayoutInflater().inflate(R.layout.hidden_panel2, root, false);
            try{
                root.removeView(hiddenPanel4);
                root.removeView(hiddenPanel);
                root.removeView(hiddenPanel3);}
            catch (Exception e)
            {
                e.printStackTrace();
            }
            root.addView(hiddenPanel2);
            hiddenPanel2.setVisibility(View.VISIBLE);
            hiddenPanel2.bringToFront();
            filter2 = (RadioGroup) hiddenPanel2.findViewById(R.id.filter2);
            filter2.setOnCheckedChangeListener(this);

            isPanelShown2 = true;
        } else {
            isPanelShown2 = false;
            root.removeView(hiddenPanel2);
        }

    }
    public void slideUpDown3(final View view) {
        isPanelShown1 = false;
        isPanelShown2 = false;
        if (!isPanelShown3) {
            hiddenPanel3 = (ViewGroup) getLayoutInflater().inflate(R.layout.hidden_panel3, root, false);
            try{
                root.removeView(hiddenPanel4);
                root.removeView(hiddenPanel);
                root.removeView(hiddenPanel2);}
            catch (Exception e)
            {
                e.printStackTrace();
            }
            root.addView(hiddenPanel3);
            hiddenPanel3.setVisibility(View.VISIBLE);
            hiddenPanel3.bringToFront();
            filter3 = (RadioGroup) hiddenPanel3.findViewById(R.id.filter3);
            filter3.setOnCheckedChangeListener(this);


            isPanelShown3 = true;
        } else {
            isPanelShown3 = false;
            root.removeView(hiddenPanel3);
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        filter1_radio_but = (RadioButton) group.findViewById(checkedId);
        filter1_radio_but.setChecked(true);
        switch(checkedId)
        {
            case R.id.veg: {
                button1.setImageResource(R.drawable.veg_filter_icon);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Veg</b></u></font>"));
                break;
            }
            case R.id.nonveg: {
                button1.setImageResource(R.drawable.nonveg_filter);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Non Veg</b></u></font>"));
                break;
            }
            case R.id.veg_nonveg:
            {
                button1.setImageResource(R.drawable.veg_non_veg_filter);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Veg & NonVeg</b></u></font>"));
                break;
            }
            case R.id.home_made:
            {
                button2.setImageResource(R.drawable.home_made);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Home</b></u></font>"));
                break;
            }
            case R.id.restaurant:
            {
                button2.setImageResource(R.drawable.restar_icon);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Restro</b></u></font>"));
                break;
            }
            case R.id.home_rest:
            {
                button2.setImageResource(R.drawable.home_restaraunt);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Home & Restro</b></u></font>"));
                break;
            }
        }
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu  = menu;
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

       int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
       ImageView v = (ImageView) searchView.findViewById(searchImgId);
       Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.search_icon); //Converting drawable into bitmap
       Bitmap new_icon = resizeBitmapImageFn(icon, 50); //resizing the bitmap
       Drawable d = new BitmapDrawable(getResources(),new_icon); //Converting bitmap into drawable
       v.setImageDrawable(d);


        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setBackgroundColor(Color.TRANSPARENT);

        int searchTextViewId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchTextView = (TextView) searchView.findViewById(searchTextViewId);
        searchTextView.setTextColor(getResources().getColor(R.color.black));
        searchTextView.setHintTextColor(getResources().getColor(R.color.black));
        searchView.setOnQueryTextListener(this);

        MenuItem item = menu.findItem(R.id.cart);
        Log.d("menu","menu item selected");
        View itemChooser = item.getActionView();
        if (itemChooser != null) {
            AppConfig.tv = (TextView) itemChooser.findViewById(R.id.actionbar_textview);
            if(AppConfig.cart_items_list.size() > 0){
                AppConfig.tv.setBackgroundResource(R.drawable.cart_icon_after_added);
                AppConfig.tv.setText(String.valueOf(AppConfig.cart_items_list.size()));}
            AppConfig.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppConfig.cart_items_list.size() > 0) {
                        Intent i = new Intent(MapVansActivity.this, CartItemsActivity.class);
                        startActivity(i);
                        //Toast.makeText(getApplicationContext(),"cart items"+AppConfig.cart_items_list.size(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Empty cart", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

        }

        return true;
    }

    private void loadHistory(String query) {

        // Cursor
        String[] columns = new String[] { "_id", "text" };
        Object[] temp = new Object[] { 0, "default" };

        MatrixCursor cursor = new MatrixCursor(columns);

        for(int i = 0; i < items.size(); i++) {
            temp[0] = i;
            temp[1] = items.get(i);
            cursor.addRow(temp);

        }
        // SearchView
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        search.setSuggestionsAdapter(new ExampleAdapter(this, cursor, items));
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                search.clearFocus();
                if (search.isShown()) {
                    search.setQuery("", false);
                    menu.findItem(R.id.menu_search).collapseActionView();

                }
                Log.d("mytag", "suggestion clicked :" + items.get(position));
                Intent intent = new Intent(MapVansActivity.this, FullMenuActivity.class);
                intent.putExtra("parent", "mapAct");
                intent.putExtra("query", items.get(position));
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        loadHistory(newText);
        return true;
    }
    @Override
    public void onLocationChanged(Location location) {

      //  Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.user_location2); //Converting drawable into bitmap
        //Bitmap new_icon = resizeBitmapImageFn(icon, 200);
      //  googleMap.addMarker(new MarkerOptions().position(latLng));
          googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
          googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 13));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 8));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 6) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.8f) ;

    }

    private Bitmap resizeBitmapImageFn(Bitmap bmpSource, int maxResolution){
        int iWidth = bmpSource.getWidth();
        int iHeight = bmpSource.getHeight();
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        if(iWidth > iHeight ){
            if(maxResolution < iWidth ){
                rate = maxResolution / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < iHeight ){
                rate = maxResolution / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK  && requestCode == 1000) {
            if (data.hasExtra("address_line")) {

                present_loc_text.setText(data.getExtras().getString("address_line"));

                }
             String latlang = data.getExtras().getString("latlang");

            onLocationChanged(null);

        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        center = googleMap.getCameraPosition().target;
        try {
            new GetLocationAsync(center.latitude, center.longitude)
                    .execute();

        } catch (Exception e) {
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("mytag", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MapVansActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    try {
                        Address returnAddress = addresses.get(0);

                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getCountryName();
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();

                        str.append(localityString + " ");
                        str.append(city + " " + region_code + " ");
                        str.append(zipcode + " ");
                    }
                    catch (Exception e)
                    {e.printStackTrace();}

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                present_loc_text.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
                if(AppConfig.login_check)
                {
                intent = new Intent(this,ProfilePageAct.class);
                startActivity(intent);
                }
                else
                {
                    intent = new Intent(this,LoginAct.class);
                    intent.putExtra("parent","profile_page");
                    startActivity(intent);
                }
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
                intent = new Intent(this,SignUpAct.class);
                startActivity(intent);
                break;
            case 6:
                break;
            default:
                break;
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            latLng = new LatLng(latitude,longitude);
            Toast.makeText(MapVansActivity.this, "location api client" + latLng.toString(), Toast.LENGTH_SHORT).show();
            onLocationChanged(mLastLocation);
            get_list();

        } else {
            Toast.makeText(MapVansActivity.this, "failed here also", Toast.LENGTH_SHORT).show();
          
        }
    }
}
