package info.androidhive.materialdesign.model;

/**
 * Created by YASHWANTH on 28/Oct/2015.
 */
public class CouponDetails {

    private String message;
    private String status;
    private int discount;
    private String applied_coupon;


    public  CouponDetails()
    {}

    public CouponDetails(String message,String status,int discount, String applied_coupon)
    {
        this.message = message;
        this.status = status;
        this.discount = discount;
        this.applied_coupon = applied_coupon;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getApplied_coupon() {
        return applied_coupon;
    }

    public void setApplied_coupon(String applied_coupon) {
        this.applied_coupon = applied_coupon;
    }
}
