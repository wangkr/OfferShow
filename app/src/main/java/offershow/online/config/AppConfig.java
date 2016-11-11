package offershow.online.config;

import android.content.Context;

import offershow.online.OfferShowApp;
import offershow.online.R;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class AppConfig {
    public static String server;
    public static String token;
    public static void init(Context context){
        server = context.getString(R.string.server_address);
        token = OfferShowApp.getAccessToken();
    }
}
