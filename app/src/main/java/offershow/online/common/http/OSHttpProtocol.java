package offershow.online.common.http;

/**
 * Created by Kairong on 2015/10/26.
 * mail:wangkrhust@gmail.com
 */
public interface OSHttpProtocol {

    // header
    String HEADER_CONTENT_TYPE = "Content-Type";

    // event

    // code
    String CODE_ID = "id";
    String CODE_COMPANY = "company";
    String CODE_POSITION = "position";
    String CODE_SALARY = "salary";
    String CODE_REMARK = "remark";
    String CODE_CITY = "city";
    String CODE_NUMBER = "number";
    String CODE_CONTENT = "content";
    String CODE_ACC_TOKEN = "access_token";
    String CODE_APPID = "appid";
    String CODE_APPSECRET = "appsecret";

    // request


    // result
    String RESULT_CODE = "r";
    String RESULT_MSG = "msg";
    String RESULT_INFO = "info";
}
