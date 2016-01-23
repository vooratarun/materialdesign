package info.androidhive.materialdesign.model;

/**
 * Created by YASHWANTH on 23/Oct/2015.
 */
public class SingleMealDetails {
    private int id;
    private String name;
    private String meal_type;

    //pic_url has to put  URL
    private String pic_url;

    private String source_name;
    private String source_type;

    //source_pic has to put URL
    private String source_pic;

    private String cuisine;
    private int  cusine_id;
    private boolean is_veg;
    private float  rating;
    private int no_ratigs;
    private double market_price;
    private double discount;
    private int quantity_available;
    private int delivery_time ;

    public SingleMealDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCusine_id() {
        return cusine_id;
    }

    public void setCusine_id(int cusine_id) {
        this.cusine_id = cusine_id;
    }

    public boolean is_veg() {
        return is_veg;
    }

    public void setIs_veg(boolean is_veg) {
        this.is_veg = is_veg;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNo_ratigs() {
        return no_ratigs;
    }

    public void setNo_ratigs(int no_ratigs) {
        this.no_ratigs = no_ratigs;
    }

    public double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getSource_pic() {
        return source_pic;
    }

    public void setSource_pic(String source_pic) {
        this.source_pic = source_pic;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }
}
