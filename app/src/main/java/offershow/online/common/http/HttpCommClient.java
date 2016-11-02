package offershow.online.common.http;


import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import offershow.online.config.AppConfig;
import offershow.online.OfferShowApp;
import offershow.online.R;
import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2015/11/9.
 * mail:wangkrhust@gmail.com
 */
public class HttpCommClient implements ICommProtocol{
    private static final String TAG = "ICommProtocol";
    private static String URL_CHECK_GJVERSION = "";
    private static String URL_CHECK_APPVERSION = "";

    private static String API_ADD_OFFER;
    private static String API_GET_OFFER;
    private static String API_GET_OFFER_ALL;
    private static String API_LIKE_OFFER;
    private static String API_DISLIKE_OFFER;

    private static HttpCommClient instance = null;

    public static HttpCommClient getInstance(){

        if(instance == null){
            synchronized (HttpCommClient.class) {
                if(instance == null) {
                    instance = new HttpCommClient();
                }
            }
        }
        return instance;
    }

    public static void init() {
        API_ADD_OFFER = OfferShowApp.getContext().getString(R.string.api_add_offer);
        API_GET_OFFER = OfferShowApp.getContext().getString(R.string.api_get_offer);
        API_GET_OFFER_ALL = OfferShowApp.getContext().getString(R.string.api_get_offer_all);
        API_LIKE_OFFER = OfferShowApp.getContext().getString(R.string.api_like_offer);
        API_DISLIKE_OFFER = OfferShowApp.getContext().getString(R.string.api_dislike_offer);
    }

    @Override
    public void likeOfferInfo(int id, final CommCallback<String> callback) {
        String url = AppConfig.server + API_LIKE_OFFER + id;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        HttpClientBase.getInstance().execute(url, headers, null, new HttpClientBase.JoyHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    Log.e(TAG, "like offer failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed("" + code, errorMsg);
                    }
                    return;
                }

                if (callback == null) {
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(response);
                int code1 = resObj.getIntValue(RESULT_CODE);
                if (code1 == 1){
                    callback.onSuccess(resObj.getString(RESULT_MSG));
                } else {
                    callback.onFailed("0", resObj.getString(RESULT_MSG));
                }
            }
        }, false, true);
    }

    @Override
    public void dislikeOfferInfo(int id, final CommCallback<String> callback) {
        String url = AppConfig.server + API_DISLIKE_OFFER + id;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        HttpClientBase.getInstance().execute(url, headers, null, new HttpClientBase.JoyHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    Log.e(TAG, "dislike offer failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed("" + code, errorMsg);
                    }
                    return;
                }

                if (callback == null) {
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(response);
                int code1 = resObj.getIntValue(RESULT_CODE);
                if (code1 == 1){
                    callback.onSuccess(resObj.getString(RESULT_MSG));
                } else {
                    callback.onFailed("0", resObj.getString(RESULT_MSG));
                }
            }
        }, false, true);
    }

    @Override
    public void getAllOfferInfo(final CommCallback<List<OfferInfo>> callback) {
        String url = AppConfig.server + API_GET_OFFER_ALL;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        HttpClientBase.getInstance().execute(url, headers, null, new HttpClientBase.JoyHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    Log.e(TAG, "get all offer failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed("" + code, errorMsg);
                    }
                    return;
                }

                if (callback == null) {
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(response);
                int code1 = resObj.getIntValue(RESULT_CODE);
                if (code1 == 1){
                    List<OfferInfo> res = new ArrayList<OfferInfo>();
                    JSONArray ja = resObj.getJSONArray(RESULT_INFO);
                    for(int i = 0;i < ja.size();i++){
                        OfferInfo oi = ja.getObject(i, OfferInfo.class);
                        res.add(oi);
                    }
                    callback.onSuccess(res);

                } else {
                    callback.onFailed("0", resObj.getString(RESULT_MSG));
                }
            }
        }, false, false);
    }

    @Override
    public void getOfferInfo(int id,final CommCallback<OfferInfo> callback) {
        String url = AppConfig.server + API_GET_OFFER + id;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        HttpClientBase.getInstance().execute(url, headers, null, new HttpClientBase.JoyHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    Log.e(TAG, "get offer failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed("" + code, errorMsg);
                    }
                    return;
                }

                if (callback == null) {
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(response);
                int code1 = resObj.getIntValue(RESULT_CODE);
                if (code1 == 1){
                    callback.onSuccess(resObj.getObject(RESULT_INFO, OfferInfo.class));
                } else {
                    callback.onFailed("0", resObj.getString(RESULT_MSG));
                }
            }
        }, false, true);
    }

    @Override
    public void addOfferInfo(OfferInfo oi, final CommCallback<Void> callback) {
        String url = AppConfig.server + API_ADD_OFFER;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        JSONObject jo = new JSONObject();
        jo.put(CODE_COMPANY, oi.getCompany());
        jo.put(CODE_CITY, oi.getCity());
        jo.put(CODE_POSITION, oi.getPosition());
        jo.put(CODE_REMARK, oi.getRemark());
        jo.put(CODE_SALARY, oi.getSalary());

        HttpClientBase.getInstance().execute(url, headers, jo, new HttpClientBase.JoyHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 0) {
                    Log.e(TAG, "add offer failed : code = " + code + ", errorMsg = " + errorMsg);
                    if (callback != null) {
                        callback.onFailed("" + code, errorMsg);
                    }
                    return;
                }

                if (callback == null) {
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(response);
                int code1 = resObj.getIntValue(RESULT_CODE);
                if (code1 == 1){
                    callback.onSuccess(null);
                } else {
                    callback.onFailed("0", resObj.getString(RESULT_MSG));
                }
            }
        }, true, true);
    }

    @Override
    public void checkAppUpdate(CommCallback<JSONArray> callback) {

    }

    private HttpCommClient(){
        HttpClientBase.getInstance().init();
    }


}
