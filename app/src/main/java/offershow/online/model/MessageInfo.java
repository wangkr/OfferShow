package offershow.online.model;

import java.io.Serializable;

/**
 * Created by Kairong on 2016/11/10.
 * mail:wangkrhust@gmail.com
 */

public class MessageInfo implements Serializable {
    private String content;
    private int id;
    private int offerid;
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
