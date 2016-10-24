package cn.ucai.fulicenter.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.fulicenter.I;

public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String CREATE_USER_TABLE_SQL =
            "create table " + UserDao.USER_TABLE_NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    UserDao.USER_COLUMN_NAME + ", " +
                    UserDao.USER_COLUMN_NICk + ", " +
                    UserDao.USER_COLUMN_AVATAR_ID + ", " +
                    UserDao.USER_COLUMN_AVATAR_PATH + ", " +
                    UserDao.USER_COLUMN_AVATAR_SUFFIX + ", " +
                    UserDao.USER_COLUMN_AVATAR_TYPE + ", " +
                    UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME +
                    ")";

    private static final int DB_VERSION = 1;
    private static DBOpenHelper instance;

    public static DBOpenHelper getDBOpentHelperInstance(Context context) {
        if (instance == null) {
            // 使用应用的上下文
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DB_VERSION);
    }

    private static String getUserDatabaseName() {
        return I.User.TABLE_NAME+"_demo.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void closeDB() {
        if (instance != null) {
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
