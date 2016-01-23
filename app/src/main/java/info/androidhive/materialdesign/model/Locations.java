package info.androidhive.materialdesign.model;

import java.io.Serializable;

/**
 * Created by YASHWANTH on 20/Oct/2015.
 */
public class Locations implements Serializable{

    private int van_id;
    private double latitude;
    private double longitude;
    private int time_to_user;

    public Locations()
    {

    }
    public Locations(int van_id, double latitude, double longitude,int time_to_user)
    {
        this.van_id = van_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time_to_user = time_to_user;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitue(double longitude) {
        this.longitude = longitude;
    }

    public int getVan_id() {
        return van_id;
    }

    public void setVan_id(int van_id) {
        this.van_id = van_id;
    }

    public int getTime_to_user() {
        return time_to_user;
    }

    public void setTime_to_user(int time_to_user) {
        this.time_to_user = time_to_user;
    }
}
