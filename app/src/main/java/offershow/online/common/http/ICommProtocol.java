package offershow.online.common.http;


import com.alibaba.fastjson.JSONArray;

import java.util.List;

import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2015/11/9.
 * mail:wangkrhust@gmail.com
 */
public interface ICommProtocol extends OSHttpProtocol {
    interface CommCallback<T>{
        void onSuccess(T t);
        void onFailed(String code, String errorMsg);
    }


    /*检查软件更新*/
    void checkAppUpdate(CommCallback<JSONArray> callback);

    /*插入单条offer信息*/
    void addOfferInfo(OfferInfo oi, CommCallback<Void> callback);
    /*查询单条信息*/
    void getOfferInfo(int id, CommCallback<OfferInfo> callback);
    /*获取所有offer信息*/
    void getAllOfferInfo(CommCallback<List<OfferInfo>> callback);
    /*薪水差评*/
    void dislikeOfferInfo(int id,CommCallback<String> callback);
    /*薪水好评*/
    void likeOfferInfo(int id,CommCallback<String> callback);

}
