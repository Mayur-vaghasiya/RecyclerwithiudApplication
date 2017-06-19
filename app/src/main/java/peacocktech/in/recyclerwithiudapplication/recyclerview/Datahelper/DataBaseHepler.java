package peacocktech.in.recyclerwithiudapplication.recyclerview.Datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import peacocktech.in.recyclerwithiudapplication.recyclerview.Friend;

/**
 * Created by peacock on 5/13/16.
 */
public class DataBaseHepler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASENAME = "USER";
    public static final String DATATABLE = "usermast";
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_JOB = "JOB";
    public static final String KEY_GENDER = "GENDER";
    SQLiteDatabase db;

    public DataBaseHepler(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    public void open() {
        db = this.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CRETATE_TABLE_USER = "CREATE TABLE " + DATATABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_NAME + " TEXT ,"
                + KEY_GENDER + " TEXT ,"
                + KEY_JOB + " TEXT " + ")";
        db.execSQL(CRETATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("" + DATATABLE);
        onCreate(db);
    }

    public void adduser(Friend f) {
        open();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, f.getName());
        values.put(KEY_JOB, f.getJob());
        values.put(KEY_GENDER, f.getGender());
        db.insert(DATATABLE, null, values);
        close();
    }

    public int updatedata(Friend f) {
        open();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, f.getName());
        values.put(KEY_JOB, f.getJob());
        values.put(KEY_GENDER, f.getGender());
        int i = db.update(DATATABLE, values, KEY_ID + " = " + f.getId(), null);
        if (i > 0) {
            Log.e("UPDATE Success", "update data" + String.valueOf(i));
        }
        close();
        return i;

    }

    public ArrayList<Friend> getalluserinfo() {
        ArrayList<Friend> userinfo = new ArrayList<Friend>();
         /*db = this.getWritableDatabase();*/
        open();
        Cursor c1 = db.rawQuery("SELECT * FROM  " + DATATABLE, null);
        if (c1.moveToFirst()) {
            do {
                Friend u = new Friend();
                u.setId(c1.getInt(c1.getColumnIndex(KEY_ID)));
                u.setName(c1.getString(c1.getColumnIndex(KEY_NAME)));
                u.setJob(c1.getString(c1.getColumnIndex(KEY_JOB)));
                u.setGende(c1.getInt(c1.getColumnIndex(KEY_GENDER)));

                userinfo.add(u);
            } while (c1.moveToNext());
        }
        c1.close();
        close();
        return userinfo;
    }

    public void deleteitem(int id) {
        open();
        db.delete(DATATABLE, KEY_ID + " =? ", new String[]{String.valueOf(id)});
        close();
    }

}
