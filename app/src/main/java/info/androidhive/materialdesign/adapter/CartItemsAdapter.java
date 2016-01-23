package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.CartItem;

/**
 * Created by YASHWANTH on 24/Oct/2015.
 */
public class CartItemsAdapter  extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<CartItem> cartitems_list;

    public CartItemsAdapter(Activity activity,List<CartItem> cartItems_list)
    {
        this.activity = activity;
        this.cartitems_list = AppConfig.cart_items_list;
    }
    @Override
    public int getCount() {
        return AppConfig.cart_items_list.size();
    }

    @Override
    public Object getItem(int position) {
        return AppConfig.cart_items_list.get(position);
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
            convertView = inflater.inflate(R.layout.cart_item_single_row, null);

        TextView cart_item_name = (TextView) convertView.findViewById(R.id.cart_item_name);
        TextView cart_item_quantity = (TextView) convertView.findViewById(R.id.cart_item_quanity);
        TextView cart_item_price = (TextView) convertView.findViewById(R.id.cart_item_price);
        ImageButton del_image =(ImageButton) convertView.findViewById(R.id.delete);
        del_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.cart_items_list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(activity, "cart items number:" + AppConfig.cart_items_list.size(), Toast.LENGTH_SHORT).show();
                AppConfig.tv.setText(String.valueOf(AppConfig.cart_items_list.size()));
            }
        });

        CartItem item = AppConfig.cart_items_list.get(position);

        cart_item_name.setText(item.getMeal_name());
        cart_item_quantity.setText(String.valueOf(item.getMeal_order_quantity()));
        cart_item_price.setText(String.valueOf(item.getTotal_meal_price()));
        return convertView;
    }

}
