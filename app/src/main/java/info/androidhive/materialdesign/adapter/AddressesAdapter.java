package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.activity.AddAddressAct;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.database.DatabaseAdapter;
import info.androidhive.materialdesign.model.UserAddress;

/**
 * Created by YASHWANTH on 31/Oct/2015.
 */
public class AddressesAdapter extends BaseAdapter {

    public static int flag_checked = 0;
    private DatabaseAdapter dbhelper;

    private Activity activity;
    private LayoutInflater inflater;
    private List<UserAddress> addresses_list = new ArrayList<UserAddress>();

    public AddressesAdapter(Activity activity,List<UserAddress> addresses_list)
    {
        this.activity = activity;
        this.addresses_list = AppConfig.address_list;
        dbhelper = new DatabaseAdapter(activity);

    }

    @Override
    public int getCount() {
        return AppConfig.address_list.size();
    }

    @Override
    public Object getItem(int position) {
        return AppConfig.address_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.address_single_row, null);

        TextView add_line1  = (TextView) convertView.findViewById(R.id.line1);
        TextView add_line2 = (TextView) convertView.findViewById(R.id.line2);
        TextView google_line = (TextView) convertView.findViewById(R.id.google_line1);

        final UserAddress address = AppConfig.address_list.get(position);

        add_line1.setText(address.getUser_line1());
        add_line2.setText(address.getUser_line2());
        google_line.setText(address.getGoogle_line1() + " " + address.getGoogle_line2());

        ImageView  edit_address = (ImageView)convertView.findViewById(R.id.edit_address);
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAddressAct.class);
                intent.putExtra("parent", "edit_address");
                intent.putExtra("user_line1", AppConfig.address_list.get(position).getUser_line1());
                intent.putExtra("user_line2", AppConfig.address_list.get(position).getUser_line2());
                intent.putExtra("google_line1", AppConfig.address_list.get(position).getGoogle_line1());
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("address_id",AppConfig.address_list.get(position).getId());
                activity.startActivity(intent);
                notifyDataSetChanged();

            }
        });
        final CheckBox address_check_box  = (CheckBox) convertView.findViewById(R.id.address_checked);
        if(AppConfig.all_address_adapter_check.equals("profile_page"))
            address_check_box.setVisibility(View.GONE);
        else
            address_check_box.setVisibility(View.VISIBLE);

        address_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(address_check_box.isChecked())
                {
                    if(flag_checked ==1)
                    {
                        address_check_box.setChecked(false);
                        Toast.makeText(activity,"Select Only One address",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AppConfig.selected_address = AppConfig.address_list.get(position);
                       flag_checked = 1;}
                }
                else
                {
                    flag_checked = 0;
                    AppConfig.selected_address = new UserAddress();
                }
            }
        });
        ImageView delete_address = (ImageView)convertView.findViewById(R.id.delete_address);
        delete_address.setOnClickListener(new View.OnClickListener() {
            int  flag = 0;
            @Override
            public void onClick(View v) {

                JSONObject obj =null;
                new JSONObject();
                try {
                    obj = new JSONObject();
                    obj.put("address_id",AppConfig.address_list.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("mytag", "data sending" + obj.toString());

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.delete_address, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag","Response :"+response.toString());
                           flag =1;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                AppController.getInstance().addToRequestQueue(req);
               int count = dbhelper.Deleterow(String.valueOf(AppConfig.address_list.get(position).getId()));
                if(count!=0)  Log.d("mytag","deleted successfully");
                AppConfig.address_list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
