package info.androidhive.materialdesign.model;

/**
 * Created by YASHWANTH on 23/Oct/2015.
 */
public class CartItem {


    private String meal_base;
    private int meal_id;
    private String meal_name;
    private int meal_order_quantity;
    private double total_meal_price;
    private int pos_in_list ;
    private double single_meal_price;
    private int promised_delivery_time;
    private String meal_type;
    private String meal_classic;

    public CartItem() {

    }

    public CartItem(int meal_id,String meal_name,int meal_order_quantity,double total_meal_price,double single_meal_price)
    {

        this.meal_id = meal_id;
        this.meal_name = meal_name;
        this.meal_order_quantity = meal_order_quantity;
        this.total_meal_price = total_meal_price;
        this.single_meal_price = single_meal_price;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }

    public int getMeal_order_quantity() {
        return meal_order_quantity;
    }

    public void setMeal_order_quantity(int meal_order_quantity) {
        this.meal_order_quantity = meal_order_quantity;
    }

    public double getTotal_meal_price() {
        return total_meal_price;
    }

    public void setTotal_meal_price(double single_meal_price) {
        this.total_meal_price = single_meal_price;
    }

    public int getPos_in_list() {
        return pos_in_list;
    }

    public void setPos_in_list(int pos_in_list) {
        this.pos_in_list = pos_in_list;
    }

    public double getSingle_meal_price() {
        return single_meal_price;
    }

    public void setSingle_meal_price(double single_meal_price) {
        this.single_meal_price = single_meal_price;
    }

    public int getPromised_delivery_time() {
        return promised_delivery_time;
    }

    public void setPromised_delivery_time(int promised_delivery_time) {
        this.promised_delivery_time = promised_delivery_time;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getMeal_base() {
        return meal_base;
    }

    public void setMeal_base(String meal_base) {
        this.meal_base = meal_base;
    }

    public String getMeal_classic() {
        return meal_classic;
    }

    public void setMeal_classic(String meal_classic) {
        this.meal_classic = meal_classic;
    }
}
