package info.androidhive.materialdesign.model;

/**
 * Created by YASHWANTH on 15/Nov/2015.
 */
public class SubsriptionItem {

        private String meal_duration;
        private String starting_date;
        private String ending_date;
        private String meal_type;
        private int no_of_meals;
        private int veg_meals;
        private int nonveg_meals;
        private int total_price;

        public  String getMeal_duration()
        {
                return meal_duration;
        }
        public  String getStarting_date()
        {
                return starting_date;
        }
        public  String getEnding_date()
        {
                return ending_date;
        }
        public  String getMeal_type()
        {
                return meal_type;
        }

        public int getNo_of_meals() {
                return no_of_meals;
        }

        public int getNonveg_meals() {
                return nonveg_meals;
        }

        public int getTotal_price() {
                return total_price;
        }

        public int getVeg_meals() {
                return veg_meals;
        }

        public void setEnding_date(String ending_date) {
                this.ending_date = ending_date;
        }

        public void setMeal_type(String meal_type) {
                this.meal_type = meal_type;
        }

        public void setMeal_duration(String meal_duration) {
                this.meal_duration = meal_duration;
        }

        public void setNo_of_meals(int no_of_meals) {
                this.no_of_meals = no_of_meals;
        }

        public void setNonveg_meals(int nonveg_meals) {
                this.nonveg_meals = nonveg_meals;
        }

        public void setTotal_price(int total_price) {
                this.total_price = total_price;
        }

        public void setStarting_date(String starting_date) {
                this.starting_date = starting_date;
        }

        public void setVeg_meals(int veg_meals) {
                this.veg_meals = veg_meals;
        }

}

