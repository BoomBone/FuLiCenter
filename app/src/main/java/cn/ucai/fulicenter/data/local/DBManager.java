package cn.ucai.fulicenter.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.utils.L;

/**
 * Created by Administrator on 2017/5/10.
 */

public class DBManager {
    String TAG="DBManager";
    private static DBOpenHelper sHelper;
    private static DBManager manager = new DBManager();

    public static DBManager getInstance(){
        return manager;
    }
    public void initDB(Context context){
        sHelper = DBOpenHelper.getInstance(context);
    }

    public synchronized boolean saveUser(User user) {
        SQLiteDatabase database = sHelper.getWritableDatabase();
        if(database.isOpen()){
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.USER_COLUMN_NAME,user.getMuserName());
            values.put(DBOpenHelper.USER_COLUMN_NICK, user.getMuserNick());
            long insert = database.replace(DBOpenHelper.USER_TABALE_NAME, null, values);
            L.e(TAG,"insert="+insert);
            return insert > 0 ? true : false;
        }
        return false;
    }
    public synchronized User getUser(String username){
        User user = null;
        SQLiteDatabase database = sHelper.getReadableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from " + DBOpenHelper.USER_TABALE_NAME + " where " +
                    DBOpenHelper.USER_COLUMN_NAME + "=?", new String[]{username});
            L.e(TAG,"cursor="+cursor);
            if(cursor.moveToNext()){
                user = new User();
                int id = cursor.getInt(cursor.getColumnIndex(sHelper.USER_COLUMN_NAME));
                String nick = cursor.getString(cursor.getColumnIndex(sHelper.USER_COLUMN_NICK));
                L.e(TAG, "usernick=" + nick);
                user.setMuserNick(nick);
                user.setMavatarId(id);
                user.setMuserName(username);

            }
        }
        return user;
    }
}
