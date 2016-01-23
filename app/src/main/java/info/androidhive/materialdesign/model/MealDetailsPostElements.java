package info.androidhive.materialdesign.model;

import java.util.ArrayList;

import info.androidhive.materialdesign.adapter.MealsDetailsAdapter;

/**
 * Created by YASHWANTH on 23/Oct/2015.
 */
public class MealDetailsPostElements {
    private String lat;
    private String lang;
    private String meal_type;
    private String meal_source;
    private String scheduled_time;
    private String search_string;

    private ArrayList<String>  cuisine;

    public MealDetailsPostElements()
    {


    }
    public MealDetailsPostElements(String lat,String lang,String meal_type,String meal_source)
    {
        this.lat = lat;
        this.lang =lang;
        this.meal_type = meal_type;
        this.meal_source = meal_source;
    }
    public MealDetailsPostElements(String lat,String lang,String search_string)
    {
        this.lat = lat;
        this.lang = lang;
        this.search_string = search_string;

    }
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getScheduled_time() {
        return scheduled_time;
    }

    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getMeal_source() {
        return meal_source;
    }

    public void setMeal_source(String meal_source) {
        this.meal_source = meal_source;
    }

    public ArrayList<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(ArrayList<String> cuisine) {
        this.cuisine = cuisine;
    }

    public String getSearch_string() {
        return search_string;
    }

    public void setSearch_string(String search_string) {
        this.search_string = search_string;
    }
}
