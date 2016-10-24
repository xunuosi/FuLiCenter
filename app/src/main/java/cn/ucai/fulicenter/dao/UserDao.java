package cn.ucai.fulicenter.dao;

import android.content.Context;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.UserBean;

public class UserDao {
    public static final String USER_TABLE_NAME = I.User.TABLE_NAME;
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICk = "m_user_nick";
    public static final String USER_COLUMN_AVATAR_ID = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_LASTUPDATE_TIME = "m_user_avatar_lastupdate_time";

    DBManager dbManager;

    public UserDao(Context context) {
        dbManager = DBManager.getInstance(context);
    }

    public boolean addUser(UserBean userBean) {
        return dbManager.addUserData(userBean);
    }

    public UserBean getUser(String username) {
        return dbManager.getUser(username);
    }

    public boolean updateUser(UserBean userBean) {
        return dbManager.updateUserData(userBean);
    }
}
