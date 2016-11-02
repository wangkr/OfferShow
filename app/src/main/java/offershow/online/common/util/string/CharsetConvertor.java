package offershow.online.common.util.string;

import java.io.UnsupportedEncodingException;

/**
 * Created by Kairong on 2016/1/13.
 * mail:wangkrhust@gmail.com
 */
public class CharsetConvertor {
    public static String UTF8ToGBK(String src) throws UnsupportedEncodingException{
        String utf8 = new String(src.getBytes(), "utf-8");
        String unicode = new String(utf8.getBytes(), "utf-8");
        return new String(unicode.getBytes("gbk"));
    }
}
