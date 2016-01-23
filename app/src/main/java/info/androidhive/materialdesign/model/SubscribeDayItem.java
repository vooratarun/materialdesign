package info.androidhive.materialdesign.model;

/**
 * Created by TARUN on 11/17/2015.
 */
public class SubscribeDayItem {

    private String meal_name;
    private String meal_type;
    private String date;
    private int meal_id;

    public SubscribeDayItem(){

    }
    public SubscribeDayItem(int meal_id,String meal_name,String meal_type)
    {
        this.meal_name = meal_name;
        this.meal_type = meal_type;
        this.meal_id = meal_id;

    }
    public String getMeal_name() {
        return meal_name;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }
}
