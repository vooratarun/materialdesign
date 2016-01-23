package info.androidhive.materialdesign.app;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.model.CartItem;
import info.androidhive.materialdesign.model.SubsriptionItem;
import info.androidhive.materialdesign.model.UserAddress;

/**
 * Created by YASHWANTH on 16/Oct/2015.
 */
public class AppConfig
{
    public static String vans_list ="http://instomeal.com/kitchenvilla/vans_details";

    public static List<CartItem> cart_items_list = new ArrayList<CartItem>();

    public static String checkout ="http://instomeal.com/kitchenvilla/order";

    public static String coupon_apply = "http://instomeal.com/kitchenvilla/coupon_discount_details";

    public  static String cuisines = "http://instomeal.com/kitchenvilla/get_cuisines";

    public static String add_address =  "http://instomeal.com/kitchenvilla/add_address";
    public static String edit_address =  "http://instomeal.com/kitchenvilla/edit_address";
    public static String user_google_address = "";

    public static Context parent_context;
    public static String get_all_addresses = "http://instomeal.com/kitchenvilla/get_addresses";

    public static ArrayList<String> applied_coupons = new ArrayList<>();
    public static ArrayList<UserAddress> address_list = new ArrayList<>();

    public static UserAddress selected_address = new UserAddress();

    public static boolean login_check = true;

    public static String all_address_adapter_check = "";

    public static String delete_address = "http://instomeal.com/kitchenvilla/delete_address";
    public static String order_cancel ="http://instomeal.com/kitchenvilla/cancel_an_order";

    public static TextView tv;
    public static SubsriptionItem item = new SubsriptionItem();

    public static String MSG_SENDER_ID = "KCHNVL";
    public static String TODAY = "AH";

    public static String sign_up = "http://instomeal.com/kitchenvilla/signup";




}
