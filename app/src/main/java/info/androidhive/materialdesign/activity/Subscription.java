package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.SubsriptionItem;

public class Subscription extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    private Button meals_seven;
    private Button meals_fourteen;
    private Button meals_thirty;
    private String meal_duration;
    private int no_of_days;
    private  int no_of_meals;
    private CalendarView calendar;
    private Button start_date;
    private TextView start_date_txt;

    private  Button lunch;
    private  Button dinner;
    private String selected_meal_type;
    private EditText veg_qty;
    private EditText nonveg_qty;

    private TextView total_price;
    private int single_veg_price = 100 ;
    private int single_nonveg_price = 150;

    private String starting_date_string;
    private String ending_date_string;
    private Button proceed_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        meals_seven = (Button)findViewById(R.id.sub_seven_btn) ;
        meals_fourteen = (Button)findViewById(R.id.sub_fourteen_btn) ;
        meals_thirty = (Button)findViewById(R.id.sub_thirty_btn) ;
        meals_seven.setOnClickListener(this);
        meals_thirty.setOnClickListener(this);
        meals_fourteen.setOnClickListener(this);
        start_date =(Button)findViewById(R.id.sub_start_date);
        start_date.setOnClickListener(this);


        calendar = (CalendarView)findViewById(R.id.sub_cal);
        start_date_txt = (TextView)findViewById(R.id.sub_start_date_txt);
        lunch = (Button)findViewById(R.id.sub_mealtype_lunch);
        dinner = (Button)findViewById(R.id.sub_mealtype_dinner);

        lunch.setOnClickListener(this);
        dinner.setOnClickListener(this);
        veg_qty = (EditText)findViewById(R.id.sub_veg_qty);
        nonveg_qty =(EditText)findViewById(R.id.sub_nonveg_qty);
        nonveg_qty.setOnEditorActionListener(this);
        veg_qty.setOnEditorActionListener(this);
        total_price = (TextView)findViewById(R.id.sub_total_price);

        proceed_btn = (Button)findViewById(R.id.sub_proceed);
        proceed_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sub_seven_btn :
            {
                meal_duration = "seven";
                no_of_meals = 7;
                no_of_days =21;
                meals_seven.setBackgroundColor(getResources().getColor(R.color.green));
                meals_thirty.setClickable(false);
                meals_fourteen.setClickable(false);
                break;
            }
            case R.id.sub_fourteen_btn :
            {
                meal_duration ="fourteen";
                no_of_meals = 14;
                no_of_days = 45;
                meals_fourteen.setBackgroundColor(getResources().getColor(R.color.green));
                meals_seven.setClickable(false);
                meals_thirty.setClickable(false);
                break;
            }
            case R.id.sub_thirty_btn :
            {
                meal_duration = "thirty";
                no_of_meals = 30;
                no_of_days = 60;
                meals_thirty.setBackgroundColor(getResources().getColor(R.color.green));
                meals_seven.setClickable(false);
                meals_thirty.setClickable(false);
                break;
            }
            case R.id.sub_start_date : {
                calendar.setVisibility(View.VISIBLE);
                Toast.makeText(Subscription.this, "calender called", Toast.LENGTH_SHORT).show();
                (findViewById(R.id.sub_section_mealtype)).setVisibility(View.GONE);
                (findViewById(R.id.meals_duration_section)).setVisibility(View.GONE);

                initializeCalendar();
                break;
            }
            case R.id.sub_mealtype_lunch :
            {
                selected_meal_type = "lunch";
                lunch.setBackgroundColor(getResources().getColor(R.color.green));
                dinner.setClickable(false);
                set_veg_text();
                break;
            }
            case R.id.sub_mealtype_dinner :
            {
                selected_meal_type = "dinner";
                dinner.setBackgroundColor(getResources().getColor(R.color.green));
                lunch.setClickable(false);
                set_veg_text();
                break;
            }
            case R.id.sub_proceed:
            {

                AppConfig.item.setMeal_duration(meal_duration);
                AppConfig.item.setNo_of_meals(no_of_meals);
                AppConfig.item.setMeal_type(selected_meal_type);
                AppConfig.item.setStarting_date(starting_date_string);
                AppConfig.item.setEnding_date(ending_date_string);
                AppConfig.item.setVeg_meals(Integer.parseInt(veg_qty.getText().toString()));
                AppConfig.item.setNonveg_meals(Integer.parseInt(nonveg_qty.getText().toString()));
                AppConfig.item.setTotal_price(Integer.parseInt(total_price.getText().toString()));
               // Gson gson = new Gson();
               // String json_data = gson.toJson(item);
                //Log.d("mytag",json_data);
                Intent intent =  new Intent(this,ScheduleSubsribeAct.class);
                //intent.putExtra("subsribe_data",json_data);
                startActivity(intent);
                break;
            }
        }
    }

    private void set_veg_text() {

        if(meal_duration.equals("seven"))
        {
            veg_qty.setText(String.valueOf("3"));
            nonveg_qty.setText(String.valueOf("4"));

        }
       else if(meal_duration.equals("fourteen"))
        {
            veg_qty.setText(String.valueOf("7"));
            nonveg_qty.setText(String.valueOf("7"));
        }
       else  if(meal_duration.equals("thirty"))
        {
            veg_qty.setText(String.valueOf("15"));
            nonveg_qty.setText(String.valueOf("15"));
        }
       int veg_price = Integer.parseInt(veg_qty.getText().toString())*single_veg_price;
       int nonveg_price = Integer.parseInt(nonveg_qty.getText().toString())*single_nonveg_price;
       int total = veg_price + nonveg_price;
       total_price.setText(String.valueOf(total));

    }
    public void initializeCalendar() {
        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        }
       // calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
       // calendar.setSelectedDateVerticalBar(R.color.green);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                String date = day +"/" + month + "/" + year;
                Date starting_date;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                starting_date_string = date;
                sdf = new SimpleDateFormat ("dd/MM/yyyy");
                String output = "";
                try {
                    System.out.println(date);
                    starting_date = sdf.parse(date);
                    System.out.println(starting_date);
                    Log.d("mytag","start date" + starting_date);
                    Calendar c = Calendar.getInstance();
                    c.setTime(starting_date); // Now use today date.
                    c.add(Calendar.DATE, no_of_days); // Adding 5 days
                    output = sdf.format(c.getTime());
                    System.out.println(output);
                    Log.d("mytag", "end date" + output);
                } catch (ParseException e) {
                    System.out.println("Unparseable using " + sdf);
                }
                start_date_txt.setText(date);
                ending_date_string = output;
                ((TextView)findViewById(R.id.sub_end_date_txt)).setText(output);
                calendar.setVisibility(View.GONE);
                (findViewById(R.id.sub_section_mealtype)).setVisibility(View.VISIBLE);
                (findViewById(R.id.meals_duration_section)).setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId())
        {
            case R.id.sub_veg_qty:
            {
                int veg  = Integer.parseInt(veg_qty.getText().toString());
                int nonveg  = Integer.parseInt(nonveg_qty.getText().toString());
                 if( veg + nonveg < no_of_meals) {
                     int total = veg * single_veg_price + nonveg * single_nonveg_price;
                     total_price.setText(String.valueOf(total));
                 }
                else
                     Toast.makeText(this,"should select less than" + no_of_meals ,Toast.LENGTH_SHORT).show();
            }
            case R.id.sub_nonveg_qty:
            {
                int veg  = Integer.parseInt(veg_qty.getText().toString());
                int nonveg  = Integer.parseInt(nonveg_qty.getText().toString());
                if( veg + nonveg < no_of_meals) {
                    int total = veg * single_veg_price + nonveg * single_nonveg_price;
                    total_price.setText(String.valueOf(total));
                }
                else
                    Toast.makeText(this,"should select less than" + no_of_meals ,Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                Toast.makeText(this,"id :" + v.getId() + " veg: "+ R.id.sub_veg_qty ,Toast.LENGTH_SHORT).show();
                switch(v.getId())
                {
                    case R.id.sub_veg_qty:
                    {
                        int veg  = Integer.parseInt(veg_qty.getText().toString());
                        int nonveg  = Integer.parseInt(nonveg_qty.getText().toString());
                        if( veg + nonveg < no_of_meals) {
                            int total = veg * single_veg_price + nonveg * single_nonveg_price;
                            total_price.setText(String.valueOf(total));
                        }
                        else
                            Toast.makeText(this,"should select less than" + no_of_meals ,Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.sub_nonveg_qty:
                    {
                        int veg  = Integer.parseInt(veg_qty.getText().toString());
                        int nonveg  = Integer.parseInt(nonveg_qty.getText().toString());
                        if( veg + nonveg < no_of_meals) {
                            int total = veg * single_veg_price + nonveg * single_nonveg_price;
                            total_price.setText(String.valueOf(total));
                        }
                        else
                            Toast.makeText(this,"should select less than" + no_of_meals ,Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                return true; // consume.

        }
        return true;
    }
}
