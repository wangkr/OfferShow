package offershow.online.model;

import java.io.Serializable;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class OfferInfo implements Serializable {
    private int id;
    private String company;
    private String position;
    private String city;
    private String salary;
    private String remark;
    private String ip;
    private String time;
    private int number;
    private int score;
    private int mask;
    private boolean tag = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OfferInfo{");
        sb.append("id=").append(id);
        sb.append(", company='").append(company).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", salary='").append(salary).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append(", number=").append(number);
        sb.append(", score=").append(score);
        sb.append(", mask=").append(mask);
        sb.append(", tag=").append(tag);
        sb.append('}');
        return sb.toString();
    }
}
