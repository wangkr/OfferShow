package offershow.online;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;

import offershow.online.common.http.HttpCommClient;
import offershow.online.common.infra.NimTaskExecutor;
import offershow.online.common.util.sys.ScreenUtil;
import offershow.online.config.AppCache;
import offershow.online.config.AppConfig;
import offershow.online.model.helper.DataHelper;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class OfferShowApp extends Application {
    private static Context _context;
    private static Resources _resources;

    private static boolean sIsAtLeastGB;

    private static boolean is_android_6_0;

    private static final String PREF_NAME = "offershow_pref";

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
        is_android_6_0 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        _resources = getResources();
        AppConfig.init(this);
        HttpCommClient.init();
        ScreenUtil.init(this);
        AppCache.init(_context);
        DataHelper.init();
    }

    public static Context getContext() {
        return _context;
    }

    public static Resources getResource() {
        return _resources;
    }

    public static boolean isAndroid6(){
        return is_android_6_0;
    }

    public static boolean isFirstUse() {
        return get("firstUse", true);
    }

    public static void setFirstUse() {
        set("firstUse", false);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void set(String key, long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        apply(editor);
    }

    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        return getPreferences(PREF_NAME);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String prefName) {
        return getContext().getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
    }
}
