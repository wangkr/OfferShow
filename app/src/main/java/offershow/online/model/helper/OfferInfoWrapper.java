package offershow.online.model.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Kairong on 2016/11/1.
 * mail:wangkrhust@gmail.com
 */
public class OfferInfoWrapper implements Parcelable{
    private int id;

    private String company;

    private String position;

    private String city;

    public OfferInfoWrapper(int id, String company, String position, String city) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.city = city;
    }

    public OfferInfoWrapper(Parcel in) {
        this.id = in.readInt();
        this.company = in.readString();
        this.position = in.readString();
        this.city = in.readString();
    }

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

    public static final Creator<OfferInfoWrapper> CREATOR = new Creator<OfferInfoWrapper>() {
        @Override
        public OfferInfoWrapper createFromParcel(Parcel in) {
            return new OfferInfoWrapper(in);
        }

        @Override
        public OfferInfoWrapper[] newArray(int size) {
            return new OfferInfoWrapper[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(company);
        dest.writeString(position);
        dest.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
