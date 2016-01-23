package info.androidhive.materialdesign.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.ExampleAdapter;
import info.androidhive.materialdesign.adapter.MealsDetailsAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.MealDetailsPostElements;
import info.androidhive.materialdesign.model.SingleMealDetails;
import info.androidhive.materialdesign.util.CustomRequest;

public class FullMenuActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = FullMenuActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://instomeal.com/kitchenvilla/get_meals";
    private ProgressDialog pDialog;
    private List<SingleMealDetails> mealsList = new ArrayList<SingleMealDetails>();
    private ListView listView;
    private MealsDetailsAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

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


    private ViewGroup mainScreen;
    private boolean isPanelShown1;
    private boolean isPanelShown2;
    private boolean isPanelShown3;
    private ViewGroup root;
    private Menu menu;
    private List<String> items = new ArrayList<String>();
    int screenHeight = 0;
    int screenWidth = 0;

    private  RadioGroup filter;
    private RadioGroup filter1;
    private RadioGroup filter2;
    private RadioGroup filter3;

    private int  meal_type_checked_id = R.id.veg_nonveg;
    private  int meal_source_checked_id =R.id.home_rest;
    private  String  meal_source = "both";
    private String meal_type= "both";
    private TextView present_loc_text;
    private Button present_loc_btn;

    private RadioButton filter1_radio_but;
    private  final Map<Integer,String > cuisine_list  = new HashMap<Integer,String>();

    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_menu);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Full Menu" + "</font>"));
        getSupportActionBar().show();


        mainScreen = (ViewGroup) findViewById(R.id.buttons);
        root = (ViewGroup) findViewById(R.id.root);
        hiddenPanel4 = (ViewGroup) getLayoutInflater().inflate(R.layout.hidden_panel4, root, false);

        items.add("chicken");
        items.add("chick");
        items.add("biryani");
        items.add("tarun");

        isPanelShown1 = false;
        isPanelShown2 = false;
        isPanelShown3 = false;

        button1 = (ImageView) findViewById(R.id.slideButton);
        button2 = (ImageView) findViewById(R.id.slideButton2);
        button3 = (ImageView) findViewById(R.id.slideButton3);
        button4 = (ImageView) findViewById(R.id.slideButton4);

        int width = 60;
        int height = 60;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        button1.setLayoutParams(parms);
        button2.setLayoutParams(parms);
        button3.setLayoutParams(parms);
        button4.setLayoutParams(parms);

        but_txt_1 = (TextView) findViewById(R.id.veg_text);
        but_txt_2 = (TextView) findViewById(R.id.home_text);
        but_txt_3 = (TextView) findViewById(R.id.cuis_text);
        but_txt_4 = (TextView) findViewById(R.id.reset_text);
        present_loc_btn = (Button) findViewById(R.id.present_loc_btn);

        present_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullMenuActivity.this,EditPlaceAct.class);
                intent.putExtra("parent","full_menu");
                startActivityForResult(intent, 1000);
            }
        });


        listView = (ListView) findViewById(R.id.meals_list_view);
        adapter = new MealsDetailsAdapter(this, mealsList);
      //  get_list();
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        meal_type_checked_id = R.id.veg_nonveg;
        meal_source_checked_id = R.id.home_rest;
        filter1 = (RadioGroup) findViewById(R.id.filter1);
         filter2 = (RadioGroup) findViewById(R.id.filter2);

        MealDetailsPostElements ele ;
        Gson gson = new Gson();
        String json  = null;
        String flag = null;
        Bundle bundle = getIntent().getExtras();
        String parent = bundle.getString("parent");

        if(parent.equals("mainAct")){
            ele = new MealDetailsPostElements("19.042323","73.069274","both","both");
            Log.d("mytag","full menu page directly got");
            json = gson.toJson(ele);
            flag  = "fullmenu";
        }
        else if(parent.equals("mapAct"))
        {
            Log.d("mytag","string rom home map page");
            String query  = bundle.getString("query");
            ele = new MealDetailsPostElements("19.042323","73.069274",query);
            json = gson.toJson(ele);
            flag = "search_item";
            Log.d("mytag","query :"+" " + bundle.getString("query") +"  "+ json);
        }
        get_list(json,flag);    //USING VOLLEY
    }
    public void get_list(String json,String flag) {

        mealsList.clear();
        Map<String, String> params = new HashMap<String, String>();
        String url_full ="";

        if(flag.equals("search_item")) {
            Log.d("mytag","search item column");

            JSONObject post_params = null;
            String url = null;
            try{
                post_params = new JSONObject(json);
               // params.put("lat", post_params.getString("lat"));
                //params.put("long", post_params.getString("lang"));
                //params.put("search_string", post_params.getString("search_string"));
                url  = "http://instomeal.com/kitchenvilla/search_meals?lat="+ post_params.getString("lat") +"&search_string=" + post_params.getString("search_string") +"&long="+ post_params.getString("lang");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomRequest meals_req = new CustomRequest(url, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
            AppController.getInstance().addToRequestQueue(meals_req);
        }
       else
        {
            Log.d("mytag","else case");

            JSONObject post_params = null;
            try{
                post_params = new JSONObject(json);
                //params.put("lat", post_params.getString("lat"));
                //params.put("long", post_params.getString("lang"));
                // params.put("meal_type", post_params.getString("meal_type"));
                // params.put("meal_source", post_params.getString("meal_source"));
                url_full  = "http://instomeal.com/kitchenvilla/get_meals?lat=" + post_params.getString("lat")
                        +"&long=" + post_params.getString("lang")
                        +"&meal_type=" + post_params.getString("meal_type")
                        + "&meal_source=" + post_params.getString("meal_source");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("mytag","Url :  "+ url_full);
            CustomRequest meals_req = new CustomRequest(url_full, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
            AppController.getInstance().addToRequestQueue(meals_req);
        }

    }
    private ErrorListener createRequestErrorListener() {

      ErrorListener errorListener = new ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

                Log.d("mytag","ERROR FROM SERVER"+ error);
          }
      };
              return errorListener;
    }

    private Response.Listener<JSONObject> createRequestSuccessListener() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag","response from server  WORKS FINE" + response.toString());
                JSONArray meals_list = null;
                try{

                    meals_list = response.getJSONArray("meals");
                    Log.d("mytag" ,"parsing meals_List data"+ meals_list.length());
                    for(int i=0;i<meals_list.length();i++)
                    {
                        JSONObject meals_obj = meals_list.getJSONObject(i);
                        Log.d("mytag", "meals_obj" + meals_obj.toString());
                        SingleMealDetails single = new SingleMealDetails();
                        single.setId(meals_obj.getInt("van_meal_id"));
                        single.setName(meals_obj.getString("meal_name"));
                        single.setPic_url(meals_obj.getString("pic_url"));
                        single.setSource_name(meals_obj.getString("source"));
                        single.setSource_type(meals_obj.getString("source_type"));
                        single.setSource_pic(meals_obj.getString("pic_url"));
                        single.setCuisine(meals_obj.getString("cuisine"));
                        single.setMeal_type(meals_obj.getString("meal_type"));
                        single.setCusine_id(1);
                        single.setIs_veg(false);
                        single.setRating((float) meals_obj.getDouble("source_rating"));
                        single.setNo_ratigs(meals_obj.getInt("no_of_ratings"));
                        single.setMarket_price(meals_obj.getDouble("marked_price"));
                        single.setDiscount(meals_obj.getDouble("discount"));
                        single.setQuantity_available(Integer.parseInt(meals_obj.getString("quantity_available")));
                        single.setDelivery_time(meals_obj.getInt("delivery_time"));
                        mealsList.add(single);
                        Log.d("mytag", "parsing meals_List data 15");
                        Log.d("mytag"," List Size :" + mealsList.size());
                    }
                    hidePDialog();
                    adapter.notifyDataSetChanged();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
        return  response;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.action_search)
        {
            if(AppConfig.cart_items_list.size() > 0) {
                Intent i = new Intent(FullMenuActivity.this,CartItemsActivity.class);
                startActivity(i);
                //Toast.makeText(getApplicationContext(),"cart items"+AppConfig.cart_items_list.size(),Toast.LENGTH_SHORT).show();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),"Empty cart", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        try {
            filter1_radio_but = (RadioButton) group.findViewById(checkedId);
            filter1_radio_but.setChecked(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        MealDetailsPostElements ele ;
        Gson gson = new Gson();
        String json  = null;
        String flag = null;
        switch(checkedId)
        {
            case R.id.veg: {
                meal_type_checked_id = R.id.veg;
                meal_type =( (RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_type.equals("Veg"))
                    meal_type = "Veg";
                else if(meal_type.equals("Non Veg"))
                    meal_type = "NonVeg";
                else
                    meal_type ="both";

                ele = new MealDetailsPostElements("19.042323","73.069274","Veg",meal_source);
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button1.setImageResource(R.drawable.veg_filter_icon);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Veg</b></u></font>"));
                break;
            }
            case R.id.nonveg: {
                meal_type_checked_id = R.id.nonveg;
                meal_type =( (RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_type.equals("Veg"))
                    meal_type = "Veg";
                else if(meal_type.equals("Non Veg"))
                    meal_type = "NonVeg";
                else
                    meal_type ="both";
                ele = new MealDetailsPostElements("19.042323","73.069274","NonVeg",meal_source);
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button1.setImageResource(R.drawable.nonveg_filter);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Non Veg</b></u></font>"));
                break;
            }
            case R.id.veg_nonveg:
            {
                meal_type_checked_id = R.id.veg_nonveg;
                meal_type =( (RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_type.equals("Veg"))
                    meal_type = "Veg";
                else if(meal_type.equals("Non Veg"))
                    meal_type = "NonVeg";
                else
                    meal_type ="both";
                ele = new MealDetailsPostElements("19.042323","73.069274","both",meal_source);
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button1.setImageResource(R.drawable.veg_non_veg_filter);
                but_txt_1.setText(Html.fromHtml("<font color=#ffa500><u><b>Veg & NonVeg</b></u></font>"));
                break;
            }
            case R.id.home_made:
            {
                meal_source_checked_id = R.id.home_made;
                meal_source = ((RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_source.equals("Home"))
                    meal_source="home_made";
                else if(meal_source.equals("Restro"))
                    meal_source="restaurant";
                else
                    meal_source="both";

                ele = new MealDetailsPostElements("19.042323","73.069274",meal_type,"home_made");
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button2.setImageResource(R.drawable.home_made);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Home</b></u></font>"));
                break;
            }
            case R.id.restaurant:
            {
                meal_source_checked_id = R.id.restaurant;
                meal_source = ((RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_source.equals("Home"))
                    meal_source="home_made";
                else if(meal_source.equals("Restro"))
                    meal_source="restaurant";
                else
                    meal_source="both";

                ele = new MealDetailsPostElements("19.042323","73.069274",meal_type,"restaurant");
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button2.setImageResource(R.drawable.restar_icon);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Restro</b></u></font>"));
                break;
            }
            case R.id.home_rest:
            {
                meal_source_checked_id = R.id.home_rest;
                meal_source = ((RadioButton)group.findViewById(checkedId)).getText().toString();
                if(meal_source.equals("Home"))
                    meal_source="home_made";
                else if(meal_source.equals("Restro"))
                    meal_source="restaurant";
                else
                    meal_source="both";

                ele = new MealDetailsPostElements("19.042323","73.069274",meal_type,"both");
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);
                button2.setImageResource(R.drawable.home_restaraunt);
                but_txt_2.setText(Html.fromHtml("<font color=#ffa500><u><b>Home & Restro</b></u></font>"));
                break;
            }
            default :
            {
                meal_type_checked_id = R.id.veg_nonveg;
                meal_type ="both";
                meal_source_checked_id = R.id.home_rest;
                meal_source = "both";
                button1.setImageResource(R.drawable.veg_non_veg_filter);
                but_txt_1.setText(R.string.veg_and_nonveg);
                button2.setImageResource(R.drawable.home_restaraunt);
                but_txt_2.setText(R.string.filter_home_rest);
                ele = new MealDetailsPostElements("19.042323","73.069274","both","both");
                Log.d("mytag","full menu page directly got");
                json = gson.toJson(ele);
                flag  = "veg_filter";
                get_list(json,flag);

            }
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        loadHistory(newText);
        return true;
    }

    public SearchView searchView;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.search_icon); //Converting drawable into bitmap
        Bitmap new_icon = resizeBitmapImageFn(icon, 30); //resizing the bitmap
        Drawable d = new BitmapDrawable(getResources(),new_icon); //Converting bitmap into drawable
        v.setImageDrawable(d);

        searchView.setOnQueryTextListener(this);
        menu.findItem(R.id.menu_search).expandActionView();
        searchView.setQuery("", false); // fill in the search term by default
        searchView.clearFocus();
        searchView.cancelPendingInputEvents();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        int searchTextViewId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchTextView = (TextView) searchView.findViewById(searchTextViewId);
        searchTextView.setTextColor(getResources().getColor(R.color.black));
        searchTextView.setHintTextColor(getResources().getColor(R.color.black));

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
                        Intent i = new Intent(FullMenuActivity.this, CartItemsActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.cart)
            Toast.makeText(getApplicationContext(),"cart items ",Toast.LENGTH_SHORT);
        return super.onOptionsItemSelected(item);
    }
    public   void slideUpDown(final View view) {
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
            filter = (RadioGroup) hiddenPanel.findViewById(R.id.filter1);
            filter.check(meal_type_checked_id);
            filter.setOnCheckedChangeListener(this);

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
            hiddenPanel2.layout(mainScreen.getLeft(), mainScreen.getBottom(), mainScreen.getRight(), screenHeight);
            hiddenPanel2.setVisibility(View.VISIBLE);
            hiddenPanel2.bringToFront();
            filter = (RadioGroup) hiddenPanel2.findViewById(R.id.filter2);
            filter.check(meal_source_checked_id);
            filter.setOnCheckedChangeListener(this);
            isPanelShown2 = true;
        } else {
            isPanelShown2 = false;
            root.removeView(hiddenPanel2);
        }
    }
    public  void slideUpDown3(final View view) {
        isPanelShown1 = false;
        isPanelShown2 = false;
        if (!isPanelShown3) {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, AppConfig.cuisines, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("mytag","respose from server :" + response.toString());
                    try{
                        for(int i=0;i<response.length();i++)
                        {
                            cuisine_list.put(response.getJSONObject(i).getInt("id"),response.getJSONObject(i).getString("name"));
                            Log.d("mytag","size : " + cuisine_list.size() +" " + cuisine_list.get(i));
                        }
                        add_buttons();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("mytag","errot from server :" + error.toString());

                        }
                    });
            AppController.getInstance().addToRequestQueue(req);

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

            filter = (RadioGroup) hiddenPanel3.findViewById(R.id.filter3);
            filter.setOnCheckedChangeListener(this);

            isPanelShown3 = true;
        } else {
            isPanelShown3 = false;
            root.removeView(hiddenPanel3);
        }
    }
    public void slideUpReset(final View view)
    {
        try{

            root.removeView(hiddenPanel);
            root.removeView(hiddenPanel2);
            root.removeView(hiddenPanel3);
            root.removeView(hiddenPanel4);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       onCheckedChanged(filter1,1);

    }
    public void add_buttons()
    {
        /*
        for (int row = 1; row <= (cuisine_list.size() / 2); row++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setId(row);
            for (int i = 1; i <= 2; i++) {
                RadioButton rdbtn = new RadioButton(this);
                int id  = (row-1)*2 + i;
                Log.d("mytag","id : "+ id + "name :" + cuisine_list.get(id));
                rdbtn.setId(id);
                rdbtn.setText(" " +cuisine_list.get(id));
                ll.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.filter3)).addView(ll);
        }
            */
        for (int i = 1; i <= cuisine_list.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            Log.d("mytag", "id : " + i + "name :" + cuisine_list.get(i));
            rdbtn.setId(i);
            rdbtn.setText(" " + cuisine_list.get(i));
            ((ViewGroup) findViewById(R.id.filter3)).addView(rdbtn);
        }


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

                if(search.isShown())
                {
                    menu.findItem(R.id.menu_search).collapseActionView() ;
                    search.setQuery("",false);

                }
                search.clearFocus();
                Log.d("mytag","suggestion clicked :" + items.get(position));
             //   Intent intent  = new Intent(FullMenuActivity.this,FullMenuActivity.class);
               // intent.putExtra("parent","mapAct");
               // intent.putExtra("query",items.get(position));
               // startActivity(intent);
                Gson gson = new Gson();
                MealDetailsPostElements ele = new MealDetailsPostElements("19.042323","73.069274",items.get(position));
                String json = gson.toJson(ele);
                get_list(json,"search_item");
                return true;
            }
        });
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK  && requestCode == 1000) {
            if (data.hasExtra("address_line")) {
                present_loc_text.setText(data.getExtras().getString("address_line"));

            }
        }
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
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent(this,MapVansActivity.class);
        startActivity(intent);

    }
}
