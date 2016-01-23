package info.androidhive.materialdesign.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import info.androidhive.materialdesign.app.AppConfig;

/**
 * Created by TARUN on 11/17/2015.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    public static String daySelection = AppConfig.TODAY;
    public static String mealTypeSelection = null;

    // Shared Preferences
    static SharedPreferences pref;

    static SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "user_details";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public static void setLoginMobile (String login_mobile) {
        editor.putString("login_mobile", login_mobile);
    }
    public static String getLoginMobile() {
        return pref.getString("login_mobile", "");
    }
    public static String getFullName() {
        return pref.getString("full_name", "");
    }

    public static boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
