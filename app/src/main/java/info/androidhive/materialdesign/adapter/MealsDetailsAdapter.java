package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.activity.FullMenuActivity;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.app.AppController;
import info.androidhive.materialdesign.model.CartItem;
import info.androidhive.materialdesign.model.SingleMealDetails;

/**
 * Created by YASHWANTH on 23/Oct/2015.
 */
public class MealsDetailsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<SingleMealDetails> meals_List;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MealsDetailsAdapter(Activity activity, List<SingleMealDetails> meals_List) {
        this.activity = activity;
        this.meals_List = meals_List;
    }

    @Override
    public int getCount() {

        return meals_List.size();
    }

    @Override
    public Object getItem(int position) {
        return meals_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

   private ViewGroup quant_plus_minus;

    private TextView quantity_text;
    private  View convertView;
    private int  clicked_item_pos ;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.meals_list_single_row, null);
        this.convertView =convertView;}

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView meal_image = (NetworkImageView) convertView.findViewById(R.id.meal_image);

        TextView meal_name = (TextView) convertView.findViewById(R.id.meal_name);
        TextView meal_price = (TextView) convertView.findViewById(R.id.meal_price);
        NetworkImageView meal_source_iamge = (NetworkImageView) convertView.findViewById(R.id.meal_source_image);
        RatingBar meal_rating = (RatingBar) convertView.findViewById(R.id.meal_rating);
        TextView meal_source_name = (TextView) convertView.findViewById(R.id.meal_source_name);
        TextView meal_place = (TextView) convertView.findViewById(R.id.meal_place);
        TextView meal_time = (TextView) convertView.findViewById(R.id.meal_time);
        TextView meal_original_price = (TextView) convertView.findViewById(R.id.original_price);
        TextView meal_discount = (TextView)convertView.findViewById(R.id.discount_percentage);


        ImageView veg_nonveg_icon = (ImageView) convertView.findViewById(R.id.veg_nonveg_icon);

        final  ViewGroup quant_plus_minus = (ViewGroup) convertView.findViewById(R.id.quantity_plus_minus);
        final Button but = (Button) convertView.findViewById(R.id.but_add_to_cart);

         Button plus = (Button) quant_plus_minus.findViewById(R.id.btn_plus);
         Button  minus = (Button) quant_plus_minus.findViewById(R.id.btn_minus);
         final TextView quantity_text = (TextView) quant_plus_minus.findViewById(R.id.cart_singel_quanity);
        int flag = 0;
        CartItem item1 = new CartItem();
        for(int i=0;i<AppConfig.cart_items_list.size();i++)
        {
             item1 = AppConfig.cart_items_list.get(i);
             if(item1.getPos_in_list() == position)
             {
                 flag =1;
                 break;
             }
        }
        if(flag == 1) {
            but.setVisibility(View.INVISIBLE);
            quant_plus_minus.setVisibility(View.VISIBLE);
            quantity_text.setText(String.valueOf(item1.getMeal_order_quantity()));
        }
        else  { but.setVisibility(View.VISIBLE); quant_plus_minus.setVisibility(View.INVISIBLE);}

        SingleMealDetails single = meals_List.get(position);
        meal_image.setImageUrl(single.getPic_url(), imageLoader);
        meal_name.setText(single.getName());
        meal_original_price.setText("\u20B9"+" " +String.valueOf(single.getMarket_price()));
        meal_original_price.setPaintFlags(meal_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        double meal_price_text = (single.getDiscount() /100) *single.getMarket_price() ;
        meal_price.setText(String.valueOf(meal_price_text));
        meal_discount.setText(String.valueOf(single.getDiscount()));
        meal_source_iamge.setImageUrl(single.getSource_pic(), imageLoader);
        meal_rating.setRating(single.getRating());
        meal_source_name.setText(single.getSource_name());
        meal_place.setText(single.getSource_type());
        meal_time.setText(String.valueOf(single.getDelivery_time() + " min"));

        if((single.getMeal_type()).equals("veg"))
            veg_nonveg_icon.setImageResource(R.drawable.veg_icon);
        else
            veg_nonveg_icon.setImageResource(R.drawable.non_veg_icon);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_item_pos = position;
                v.setVisibility(View.GONE);
                quant_plus_minus.setVisibility(View.VISIBLE);
                CartItem item = new CartItem();
                SingleMealDetails single = meals_List.get(position);
                item.setMeal_name(single.getName());
                item.setMeal_id(single.getId());
                item.setMeal_type(single.getMeal_type());
                item.setMeal_classic("classic");
                item.setMeal_order_quantity(1);
                item.setTotal_meal_price(single.getMarket_price());
                item.setSingle_meal_price(single.getMarket_price());
                item.setPos_in_list(position);
                item.setPromised_delivery_time(single.getDelivery_time());
                quantity_text.setText("1");
                AppConfig.cart_items_list.add(item);
                Toast.makeText(activity, "cart items number:" + AppConfig.cart_items_list.size(), Toast.LENGTH_SHORT).show();
                AppConfig.tv.setBackgroundResource(R.drawable.cart_icon_after_added);
                AppConfig.tv.setText(String.valueOf(AppConfig.cart_items_list.size()));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = new CartItem();
                for (int i = 0; i < AppConfig.cart_items_list.size(); i++) {
                    item = AppConfig.cart_items_list.get(i);
                    if (item.getPos_in_list() == position)
                        break;
                }
                int qty = item.getMeal_order_quantity() + 1, max_qty = 50;
                if (qty > 0 && qty < max_qty) {
                    item.setMeal_order_quantity(qty);
                    quantity_text.setText(String.valueOf(qty));
                    item.setTotal_meal_price(qty * item.getSingle_meal_price());
                }
                Toast.makeText(activity, "pos " + item.getMeal_name(), Toast.LENGTH_LONG).show();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = new CartItem();
                int pos =-1;
                for (int i = 0; i < AppConfig.cart_items_list.size(); i++) {
                    item = AppConfig.cart_items_list.get(i);
                    if (item.getPos_in_list() == position){
                        pos = i;
                        break;}
                }
                int qty = item.getMeal_order_quantity()-1;
                if(qty>0)
                {
                item.setMeal_order_quantity(qty);
                item.setTotal_meal_price(qty*item.getSingle_meal_price());
                quantity_text.setText(String.valueOf(qty));
                }
                else
                {
                    AppConfig.cart_items_list.remove(pos);
                    AppConfig.tv.setText(String.valueOf(AppConfig.cart_items_list.size()));
                    Toast.makeText(activity,"Should be one Item. Add Again",Toast.LENGTH_LONG).show();
                    quant_plus_minus.setVisibility(View.GONE);
                    but.setVisibility(View.VISIBLE);
                }
            }
        });
        return convertView;
    }
}