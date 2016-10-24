package cn.ucai.fulicenter.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.bean.UserBean;

public class DBManager {
    private DBOpenHelper mDBOpenHelper;

    private DBManager() {

    }

    private static class DBManagerIntance{

        static final DBManager instance = new DBManager();
    }
    public static DBManager getInstance(Context context) {
         DBManagerIntance.instance.mDBOpenHelper =
                DBOpenHelper.getDBOpentHelperInstance(context);
        return DBManagerIntance.instance;
    }

    public synchronized boolean addUserData(UserBean userBean) {
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDao.USER_COLUMN_NAME, userBean.getMuserName());
        contentValues.put(UserDao.USER_COLUMN_NICk, userBean.getMuserNick());
        contentValues.put(UserDao.USER_COLUMN_AVATAR_ID, userBean.getMavatarId());
        contentValues.put(UserDao.USER_COLUMN_AVATAR_TYPE, userBean.getMavatarType());
        contentValues.put(UserDao.USER_COLUMN_AVATAR_PATH, userBean.getMavatarPath());
        contentValues.put(UserDao.USER_COLUMN_AVATAR_SUFFIX, userBean.getMavatarSuffix());
        contentValues.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME, userBean.getMavatarPath());

        if (db.isOpen()) {
            // 该方法相当于INSERT OR REPLACE
            return db.replace(UserDao.USER_TABLE_NAME, null, contentValues)!=-1;
        }
        return false;
    }

    public UserBean getUser(String username) {
        UserBean user = null;
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        // 防止数据库脚本注入
        String whereClause = UserDao.USER_COLUMN_NAME + "=?";
        String[] whereArgs=new String[]{username};
        Cursor cursor = db.query(
                UserDao.USER_TABLE_NAME,
                null,// Columns-null selects all columns
                whereClause,
                whereArgs,
                null,// groupBy
                null,// having
                null// orderBy
        );

        if (cursor.moveToNext()) {
            user = new UserBean();
            user.setMuserName(cursor.getString(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_NAME)));
            user.setMuserNick(cursor.getString(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_NICk)));
            user.setMavatarId(cursor.getInt(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarSuffix(cursor.getString(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarPath(cursor.getString(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarLastUpdateTime(cursor.getString(
                    cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
        }

        return user;
    }

    public synchronized boolean updateUserData(UserBean userBean) {
        // 用于记录修改了几条记录
        int result = -1;
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        // 防止数据库脚本注入
        String sql = UserDao.USER_COLUMN_NAME + "=?";
        ContentValues contentValues = new ContentValues();
        // 放入要修改的数据
        contentValues.put(UserDao.USER_COLUMN_NICk, userBean.getMuserNick());
        if (db.isOpen()) {
            // 数据库更新方法
            result = db.update(UserDao.USER_TABLE_NAME, contentValues, sql
                    , new String[]{userBean.getMuserName()});
        }
        return result > 0;
    }

    public synchronized void closeDB() {
        mDBOpenHelper.closeDB();
    }

}
