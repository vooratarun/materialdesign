package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.activity.CartItemsActivity;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.CartItem;

/**
 * Created by YASHWANTH on 31/Oct/2015.
 */
public class CartItemsNewAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<CartItem> cartitems_list;


    public CartItemsNewAdapter(Activity activity,List<CartItem> cartItems_list)
    {
        this.activity = activity;
        this.cartitems_list = AppConfig.cart_items_list;
    }

    @Override
    public int getCount() {
        return cartitems_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cartitems_list.get(position);
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
            convertView = inflater.inflate(R.layout.cart_item_single_row_another, null);

        TextView cart_item_type = (TextView) convertView.findViewById(R.id.cart_item_type);
        TextView cart_item_meal_type = (TextView) convertView.findViewById(R.id.cart_item_meal_type);

        TextView cart_item_name = (TextView) convertView.findViewById(R.id.cart_item_meal_name);
        TextView cart_item_price = (TextView) convertView.findViewById(R.id.cart_item_meal_price);
        TextView cart_item_quantity = (TextView) convertView.findViewById(R.id.cart_item_quanity);
        ImageView veg_nonveg = (ImageView)convertView.findViewById(R.id.veg_nonveg_icon);


        CartItem item = AppConfig.cart_items_list.get(position);

        cart_item_meal_type.setText(item.getMeal_type());

        if(item.getMeal_type().equals("veg"))
            veg_nonveg.setImageResource(R.drawable.veg_icon);
        else
            veg_nonveg.setImageResource(R.drawable.non_veg_icon);
        cart_item_type.setText(item.getMeal_classic());
        cart_item_name.setText(item.getMeal_name());
        cart_item_price.setText(String.valueOf(item.getSingle_meal_price() * item.getMeal_order_quantity()));
        cart_item_quantity.setText(String.valueOf(item.getMeal_order_quantity()));
        Button plus = (Button) convertView.findViewById(R.id.cart_item_btn_plus);
        Button minus = (Button) convertView.findViewById(R.id.cart_item_btn_minus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CartItem item = AppConfig.cart_items_list.get(position);
                    item.setMeal_order_quantity(item.getMeal_order_quantity() + 1);
                    item.setTotal_meal_price(item.getSingle_meal_price()*item.getMeal_order_quantity());
                notifyDataSetChanged();
                double total_price = 0;
                for(int i=0;i<AppConfig.cart_items_list.size();i++)
                    total_price = total_price + AppConfig.cart_items_list.get(i).getTotal_meal_price();
                CartItemsActivity.total.setText(String.valueOf(total_price));

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = AppConfig.cart_items_list.get(position);
                item.setMeal_order_quantity(item.getMeal_order_quantity()-1);
                item.setTotal_meal_price(item.getSingle_meal_price()*item.getMeal_order_quantity());
                notifyDataSetChanged();
                double total_price = 0;
                for(int i=0;i<AppConfig.cart_items_list.size();i++)
                    total_price = total_price + AppConfig.cart_items_list.get(i).getTotal_meal_price();
                CartItemsActivity.total.setText(String.valueOf(total_price));
            }
        });
        return convertView;
    }
}
