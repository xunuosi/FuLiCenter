package cn.ucai.fulicenter.shared_prefs;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {
    private static String SHARED_PREFRENCES_NAME = "userInfo";
    private static String SHARED_KEY_USER_NAME = "account";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferencesUtils(){}

    private static class SharedPrefrencesUtilsInstance {
        static SharedPreferencesUtils instance = new SharedPreferencesUtils();
    }

    public static SharedPreferencesUtils getInstance(Context context) {
        SharedPrefrencesUtilsInstance.instance.mSharedPreferences =
                    context.getApplicationContext()
                        .getSharedPreferences(SHARED_PREFRENCES_NAME, Context.MODE_PRIVATE);
        return SharedPrefrencesUtilsInstance.instance;
    }

    public void saveUser(String username) {
        mEditor = mSharedPreferences.edit();
        mEditor.putString(SHARED_KEY_USER_NAME, username);
        mEditor.commit();
    }

    public String getUser() {
        return mSharedPreferences.getString(SHARED_KEY_USER_NAME, null);
    }

    public void removeUser() {
        mSharedPreferences.edit().remove(SHARED_KEY_USER_NAME).apply();
    }
}
