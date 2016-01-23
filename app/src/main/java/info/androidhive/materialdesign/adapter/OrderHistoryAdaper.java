package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import info.androidhive.materialdesign.activity.OrderCancleAct;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.OrderHistoyItem;

/**
 * Created by YASHWANTH on 02/Nov/2015.
 */
public class OrderHistoryAdaper  extends BaseAdapter {


    private int order_id;
    private String message;
    private Activity activity;
    private LayoutInflater inflater;
    private List<OrderHistoyItem> order_history_list = new ArrayList<OrderHistoyItem>();

    public OrderHistoryAdaper(Activity activity,List<OrderHistoyItem> order_history_list )
    {
        this.activity = activity;
        this.order_history_list = order_history_list;
    }
    @Override
    public int getCount() {
        return order_history_list.size();
    }

    @Override
    public Object getItem(int position) {
        return order_history_list.get(position);
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
            convertView = inflater.inflate(R.layout.order_history_single_row, null);

        TextView address = (TextView) convertView.findViewById(R.id.history_address);
        TextView time = (TextView) convertView.findViewById(R.id.history_time);
        TextView price = (TextView) convertView.findViewById(R.id.history_price);
        TextView status = (TextView) convertView.findViewById(R.id.history_status);

        OrderHistoyItem item = order_history_list.get(position);

        address.setText(item.getAddress());
        time.setText(item.getTime());
        price.setText(String.valueOf(item.getTime()));
        status.setText(item.getStatus());

        Button order_cancle_btn = (Button)convertView.findViewById(R.id.cancle_order_btn);
         order_cancle_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(activity, OrderCancleAct.class);
                 intent.putExtra("order_id",order_history_list.get(position).getOrder_id());
                 activity.startActivity(intent);

               /*  LayoutInflater li = LayoutInflater.from(activity);
                 View promptsView = li.inflate(R.layout.alert_box_cancle, null);
                 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                 alertDialogBuilder.setView(promptsView);
                 final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                 alertDialogBuilder
                         .setCancelable(false)
                         .setPositiveButton("OK",
                                 new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         message = userInput.getText().toString();
                                         order_id = order_history_list.get(position).getOrder_id();
                                       //  server_request();
                                         //  order_history_list.remove(position);
                                         notifyDataSetChanged();
                                     }
                                 })
                         .setNegativeButton("Cancel",
                                 new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         dialog.cancel();
                                     }
                                 });
                 AlertDialog alertDialog = alertDialogBuilder.create();
                 alertDialog.show();

                */
             }

         });

        return convertView;
    }



}
