package nzmoter.com.nazimoterfinalproject.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nzmoter on 15.11.2016.
 */
public class SharedPreference {
    static final String PREF_NAME = "user_database";
    static final String PREF_KEY1 = "id";

    public SharedPreference() {
        super();
    }

    public void saveMyId(Context context, String text) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(PREF_KEY1, text);
        editor.commit();
    }

    public String getMyId(Context context) {
        String text;
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREF_KEY1, null);
        return text;
    }
}