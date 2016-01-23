package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.SubscribeDayItem;

public class ScheduleSubsribeAct extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner time_slot;

    private List<String> times_list = new ArrayList<String>();
    private CalendarView calendarview;
    private String subscribe_data;
    private  int no_of_meals = 0;
    private  int veg_meals =0;
    private int non_veg_meals =0;
    private int max_no_of_meals;
    private int max_veg_meals;
    private  int max_non_veg_meals;
    private  List<SubscribeDayItem> selected_items = new ArrayList<SubscribeDayItem>();
    private String clicked_date;

    private Button submit_btn;

    private String starting_date;
    private String ending_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_subsribe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        time_slot = (Spinner)findViewById(R.id.sub_time_slot);
        times_list.add("12PM-1pm");
        times_list.add("1PM-3pm");
        times_list.add("3PM-5pm");

        ArrayAdapter<String> data_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,times_list);
        data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        time_slot.setAdapter(data_adapter);
        time_slot.setOnItemSelectedListener(this);
      //  time_slot.setOnItemClickListener(this);


        // subscribe_data = getIntent().getExtras().getString("subsribe_data");

        AppConfig.item.setNo_of_meals(4);
        AppConfig.item.setNonveg_meals(2);
        AppConfig.item.setVeg_meals(2);
        AppConfig.item.setStarting_date("17/11/2015");
        AppConfig.item.setEnding_date("24/11/2015");

        max_no_of_meals = AppConfig.item.getNo_of_meals();
        max_veg_meals = AppConfig.item.getVeg_meals();
        max_non_veg_meals = AppConfig.item.getNonveg_meals();
        starting_date = AppConfig.item.getStarting_date();
        ending_date = AppConfig.item.getEnding_date();

        submit_btn =(Button)findViewById(R.id.sub_meals_submit);
        submit_btn.setOnClickListener(this);
        calendarview = (CalendarView)findViewById(R.id.sub_sch_calender);
        try {
            initializeCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    public void initializeCalendar() throws ParseException {
        calendarview.setShowWeekNumber(false);
        calendarview.setFirstDayOfWeek(2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            calendarview.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      //  Date startingDate  =  sdf.parse("17/11/2015");
      //  Calendar start= Calendar.getInstance();
      //  start.setTime(startingDate);
       // calendarview.setMinDate(start.getTimeInMillis());

        Toast.makeText(ScheduleSubsribeAct.this, " "+ starting_date +" " + ending_date, Toast.LENGTH_SHORT).show();

        Date endingDate = sdf.parse(String.valueOf(ending_date));
        Calendar end = Calendar.getInstance();
        end.setTime(endingDate);
        calendarview.setMaxDate(end.getTimeInMillis());

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                 clicked_date = day + "/" + month + "/" + year;
                //data from server here listview

                ListView options_list = (ListView) findViewById(R.id.sub_day_options);
                ArrayList<SubscribeDayItem> options = new ArrayList<SubscribeDayItem>();
                DayOptionsAdapter adapter = new DayOptionsAdapter(options, getApplicationContext());
                options_list.setAdapter(adapter);
                options.add(new SubscribeDayItem(1, "meal 1", "veg"));
                options.add(new SubscribeDayItem(2,"meal 2", "nonveg"));
                options.add(new SubscribeDayItem(3,"meal 3", "veg"));
                options.add(new SubscribeDayItem(4,"meal 4", "nonveg"));
                options.add(new SubscribeDayItem(5,"meal 5", "nonveg"));
                options.add(new SubscribeDayItem(6,"meal 6", "veg"));
                options.add(new SubscribeDayItem(7,"meal 7", "veg"));
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onClick(View v) {
        // next go to addresses page to select an address and then goes to myplan page.


    }

    class DayOptionsAdapter extends BaseAdapter{

        private ArrayList<SubscribeDayItem> options = new ArrayList<SubscribeDayItem>();
        private Context activity;
        private LayoutInflater inflater;
        private  int flag_checked = 0;


        public DayOptionsAdapter(ArrayList<SubscribeDayItem> options, Context actvity)
        {
            this.options = options;
            this.activity = actvity;

        }

        @Override
        public int getCount() {
            return options.size();
        }

        @Override
        public Object getItem(int position) {
            return options.get(0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.subsribe_one_day_list, null);

            CheckBox chk = (CheckBox)convertView.findViewById(R.id.sub_one_day_option);
            int prev_pos = -1;
            int prev_check_pos = -1;
            for(int i=0;i<selected_items.size();i++)
            {
                if(selected_items.get(i).getDate().equals(clicked_date)){
                    prev_pos = selected_items.get(i).getMeal_id();break;}
            }
            if(prev_pos !=-1 )
            {
                    for(int i=0;i<options.size();i++)
                    {
                       if( options.get(i).getMeal_id() == prev_pos){
                           prev_check_pos = i;
                            break;}
                    }

            }
            final SubscribeDayItem item = options.get(position);
             int direct  = 0;
            if(prev_check_pos != -1 && position == prev_check_pos)
            {
                flag_checked = 1;
                chk.setChecked(true);
                direct = 1;
            }
            chk.setText(item.getMeal_name());
            final int finalDirect = direct;
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (flag_checked == 0 && isChecked) {
                        Toast.makeText(activity, item.getMeal_name(), Toast.LENGTH_SHORT).show();
                        item.setDate(clicked_date);

                        if (no_of_meals < max_no_of_meals) {
                            if (item.getMeal_type().equals("veg")) {
                                if (veg_meals < max_veg_meals) {
                                    veg_meals += 1;
                                    flag_checked = 1;
                                    selected_items.add(item);
                                    no_of_meals += 1;
                                    ((TextView)findViewById(R.id.sub_rem_items)).setText(String.valueOf("Items Remaining:") + String.valueOf(max_no_of_meals - no_of_meals));
                                    ((TextView)findViewById(R.id.sub_rem_veg)).setText(String.valueOf("Veg Remaining:") + String.valueOf(max_veg_meals - veg_meals));

                                } else {
                                    Toast.makeText(activity, "selected max veg meals :" + max_veg_meals, Toast.LENGTH_SHORT).show();
                                    buttonView.setChecked(false);
                                }
                            } else {
                                if (non_veg_meals < max_non_veg_meals) {
                                    non_veg_meals += 1;
                                    flag_checked = 1;
                                    selected_items.add(item);
                                    no_of_meals += 1;
                                    ((TextView)findViewById(R.id.sub_rem_items)).setText(String.valueOf("Items Remaining") + String.valueOf(max_no_of_meals - no_of_meals));
                                    ((TextView)findViewById(R.id.sub_rem_non_veg)).setText(String.valueOf("NonVeg Remained :") + String.valueOf(max_non_veg_meals - non_veg_meals));


                                } else {
                                    Toast.makeText(activity, "selected max nonveg meals :" + max_non_veg_meals, Toast.LENGTH_SHORT).show();
                                      buttonView.setChecked(false);
                                }
                            }


                        } else {
                            Toast.makeText(activity, " Max No.of meals: " + max_no_of_meals, Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(false);
                        }
                    } else if (flag_checked == 1 && isChecked && finalDirect == 0) {
                        buttonView.setChecked(false);
                    } else if (flag_checked == 1 && !isChecked) {
                        int to_remove = -1;
                        for (int i = 0; i < selected_items.size(); i++) {
                            if (selected_items.get(i).getDate().equals(clicked_date)) {
                                to_remove = i;
                                break;
                            }
                        }
                        if (to_remove != -1) {

                            if (selected_items.get(to_remove).getMeal_type().equals("veg")){
                                veg_meals -= 1;
                                ((TextView)findViewById(R.id.sub_rem_veg)).setText(String.valueOf("Veg Remaining:") + String.valueOf(max_veg_meals - veg_meals));

                            }
                            else {
                                non_veg_meals -= 1;
                                ((TextView)findViewById(R.id.sub_rem_non_veg)).setText(String.valueOf("NonVeg Remaining:") + String.valueOf(max_non_veg_meals - non_veg_meals));

                            }

                            selected_items.remove(to_remove);
                            no_of_meals -= 1;
                            ((TextView)findViewById(R.id.sub_rem_items)).setText(String.valueOf("Items Remaining:") + String.valueOf(max_no_of_meals - no_of_meals));


                        }

                        flag_checked = 0;
                    }
                }
            });
            return convertView;
        }
    }
}

