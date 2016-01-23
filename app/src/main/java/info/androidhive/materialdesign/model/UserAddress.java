package info.androidhive.materialdesign.model;

/**
 * Created by YASHWANTH on 31/Oct/2015.
 */
public class UserAddress {

    private int id;
    private String lat;
    private String lang;
    private String user_line1;
    private String user_line2;
    private String pincode;
    private String google_line1;
    private String google_line2;


    public UserAddress()
    {

    }
    public  UserAddress(int id, String lat, String lang, String user_line1,String user_line2,String pincode,String google_line1,String google_line2)
    {
        this.id = id;
        this.lat = lat;
        this.lang = lang;
        this.user_line1 = user_line1;
        this.user_line2 = user_line2;
        this.pincode = pincode;
        this.google_line1 = google_line1;
        this.google_line2 = google_line2;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUser_line1() {
        return user_line1;
    }

    public void setUser_line1(String user_line1) {
        this.user_line1 = user_line1;
    }

    public String getUser_line2() {
        return user_line2;
    }

    public void setUser_line2(String user_line2) {
        this.user_line2 = user_line2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGoogle_line1() {
        return google_line1;
    }

    public void setGoogle_line1(String google_line1) {
        this.google_line1 = google_line1;
    }

    public String getGoogle_line2() {
        return google_line2;
    }

    public void setGoogle_line2(String google_line2) {
        this.google_line2 = google_line2;
    }
}
