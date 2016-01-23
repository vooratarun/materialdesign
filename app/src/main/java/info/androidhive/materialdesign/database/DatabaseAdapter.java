package info.androidhive.materialdesign.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.model.UserAddress;

/**
 * Created by YASHWANTH on 10/Nov/2015.
 */
public class DatabaseAdapter  {

    DatabaseHelper helper;

    Context context;
    public DatabaseAdapter(Context context)
    {
        helper = new DatabaseHelper(context);
        this.context = context;
    }

    public long insert_data(int id,String lati,String longi,String user_line1,String user_Line2,String google_line1 )
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ADD_ID,id);
        contentValues.put(DatabaseHelper.LATITUDE,lati);
        contentValues.put(DatabaseHelper.LONGITUDE,longi);
        contentValues.put(DatabaseHelper.USER_LINE_1,user_line1);
        contentValues.put(DatabaseHelper.USER_LINE_2,user_Line2);
        contentValues.put(DatabaseHelper.GOOGLE_LINE,google_line1);

        long id2 = db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
    return id2;

    }

    public String getAllData()
    {
        AppConfig.address_list.clear();
        SQLiteDatabase db = helper.getWritableDatabase();
        Toast.makeText(context, "inside", Toast.LENGTH_SHORT).show();

        String[] columns = {DatabaseHelper.UID,DatabaseHelper.ADD_ID,DatabaseHelper.LATITUDE,DatabaseHelper.LONGITUDE,DatabaseHelper.USER_LINE_1,DatabaseHelper.USER_LINE_2,DatabaseHelper.GOOGLE_LINE};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.UID));
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ADD_ID));
            String lati = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LATITUDE));
            String longi = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LONGITUDE));
            String user_line1 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_LINE_1));
            String user_line2 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_LINE_2));
            String google_line1 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOOGLE_LINE));
            UserAddress single = new UserAddress();
            single.setId(id);
            single.setLat(lati);
            single.setLang(longi);
            single.setPincode("521324");
            single.setUser_line1(user_line1);
            single.setUser_line2(user_line2);
            single.setGoogle_line1(google_line1);
            single.setGoogle_line2("google_line2");
        AppConfig.address_list.add(single);
            buffer.append(cid + " "+ id + " "+lati+" "+longi+" "+user_line1+" "+user_line2+" "+google_line1+ "\n");
        }
        return buffer.toString();

    }
    public int updateTable(String old_id,String lati,String longi,String user_line1,String user_Line2,String google_line1)
    {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LATITUDE,lati);
        contentValues.put(DatabaseHelper.LONGITUDE,longi);
        contentValues.put(DatabaseHelper.USER_LINE_1,user_line1);
        contentValues.put(DatabaseHelper.USER_LINE_2, user_Line2);
        contentValues.put(DatabaseHelper.GOOGLE_LINE, google_line1);
        String[] whereArgs ={old_id};
        int count=  db.update(DatabaseHelper.TABLE_NAME,contentValues,DatabaseHelper.ADD_ID+" =?",whereArgs);
        Toast.makeText(context, "make it success", Toast.LENGTH_SHORT).show();
        return count;
    }
    public int Deleterow(String old_id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {old_id};
        int count = db.delete(DatabaseHelper.TABLE_NAME,DatabaseHelper.ADD_ID+" =?",whereArgs);
        Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
        return count;
    }
   static class DatabaseHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME ="user_app";
        private static final  String TABLE_NAME = "user_addresses";
        private static final int DATABASE_VERSION = 4;
        private static final String UID ="_id";
        private static final String ADD_ID ="Address_id";
        private static final String LATITUDE ="Latitude";
        private static final String LONGITUDE ="Logitude";
        private static final String USER_LINE_1 ="User_line_1";
        private static final String USER_LINE_2 ="User_line_2";
        private static final String GOOGLE_LINE = "Google_line";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + ADD_ID + " INT,"
                + LATITUDE+ " VARCHAR(255),"
                + LONGITUDE + " VARCHAR(255),"
                + USER_LINE_1 + " VARCHAR(255),"
                + USER_LINE_2 + " VARCHAR(255),"
                + GOOGLE_LINE + " VARCHAR(255)" + ");";


        private static final String DROP_TABLE ="DROP TABLE IF EXISTS " + TABLE_NAME +";";
        private  Context context;

        public DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            Toast.makeText(context, "constructer called ", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d("mytag",CREATE_TABLE);
                db.execSQL(CREATE_TABLE);
                Toast.makeText(context, "table created", Toast.LENGTH_SHORT).show();

            }catch (SQLException e)
            {
                e.printStackTrace();
                Toast.makeText(context, "error creating db", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                Toast.makeText(context, "drop table executed", Toast.LENGTH_SHORT).show();
                onCreate(db);
            }catch (SQLException e)
            {e.printStackTrace();
                Toast.makeText(context, "drop table problem", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
