package offershow.online.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import offershow.online.model.OfferInfo;
import offershow.online.model.helper.OfferSuggestion;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class DBService {
    private DBOpenHelper dbOpenHelper;
    public DBService(Context context){
        dbOpenHelper = new DBOpenHelper(context);
    }

    public List<OfferSuggestion> findAllOfferSuggestions(){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        List<OfferSuggestion> res = new LinkedList<>();
        Cursor cursor = db.rawQuery("select * from offersuggestion order by times desc", new String[]{});
        while (cursor.moveToNext()) {
            OfferSuggestion os = new OfferSuggestion(cursor.getString(cursor.getColumnIndex("content")));
            os.setIsHistory(cursor.getInt(cursor.getColumnIndex("history")));
            os.setSearchTimes(cursor.getInt(cursor.getColumnIndex("times")));
            res.add(os);
        }

        cursor.close();
        db.close();
        return res;
    }

    public void addAllOfferSuggestions(Collection<OfferSuggestion> osList){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from offersuggestion");
        try {
            for (OfferSuggestion oi : osList) {
                db.execSQL("insert into offersuggestion values (?, ?, ?)", new Object[]{oi.getBody(), oi.getSearchTimes(), oi.getIsHistory()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 单个插入
     */
    public void addOfferInfo(OfferInfo oi){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("replace into offerdetail values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{oi.getId(), oi.getCompany(), oi.getPosition(),
        oi.getSalary(), oi.getCity(), oi.getRemark(), oi.getNumber(), oi.getScore(), oi.getIp(), oi.getTime()});
        db.close();
    }

    public OfferInfo findOfferInfo(int id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from offerdetail where _id=?",new String[]{id+""});
        if (cursor.moveToFirst()) {
            OfferInfo oi = new OfferInfo();
            oi.setCity(cursor.getString(cursor.getColumnIndex("city")));
            oi.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            oi.setCompany(cursor.getString(cursor.getColumnIndex("company")));
            oi.setPosition(cursor.getString(cursor.getColumnIndex("position")));
            oi.setSalary(cursor.getString(cursor.getColumnIndex("salary")));
            oi.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
            oi.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            oi.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            oi.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            oi.setTime(cursor.getString(cursor.getColumnIndex("time")));
            cursor.close();
            db.close();
            return oi;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    /**
     * 列表插入
     */
    public void addAllOffers(List<OfferInfo> oiList){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (OfferInfo oi : oiList) {
                db.execSQL("replace into offerdetail values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{oi.getId(), oi.getCompany(), oi.getPosition(),
                        oi.getSalary(), oi.getCity(), oi.getRemark(), oi.getNumber(), oi.getScore(), oi.getIp(), oi.getTime()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 按城市分组
     */
    public HashMap<String, List<OfferInfo>> findAllByCitys(){
        HashMap<String, List<OfferInfo>> res = new HashMap<>();

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from offerdetail group by city", new String[]{});

        while(cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));
            OfferInfo oi = new OfferInfo();
            oi.setCity(city);
            oi.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            oi.setCompany(cursor.getString(cursor.getColumnIndex("company")));
            oi.setPosition(cursor.getString(cursor.getColumnIndex("position")));
            oi.setSalary(cursor.getString(cursor.getColumnIndex("salary")));
            oi.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
            oi.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            oi.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            oi.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            oi.setTime(cursor.getString(cursor.getColumnIndex("time")));
            if (res.containsKey(city)){
                res.get(city).add(oi);
            } else {
                res.put(city, new ArrayList<OfferInfo>());
                res.get(city).add(oi);
            }
        }

        cursor.close();
        db.close();

        return res;
    }

    /**
     * 按公司分组
     */
    public HashMap<String, List<OfferInfo>> findAllCompanys(){
        HashMap<String, List<OfferInfo>> res = new HashMap<>();

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from offerdetail group by company", new String[]{});

        while(cursor.moveToNext()){
            String company = cursor.getString(cursor.getColumnIndex("company"));
            OfferInfo oi = new OfferInfo();
            oi.setCompany(company);
            oi.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            oi.setCity(cursor.getString(cursor.getColumnIndex("city")));
            oi.setPosition(cursor.getString(cursor.getColumnIndex("position")));
            oi.setSalary(cursor.getString(cursor.getColumnIndex("salary")));
            oi.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
            oi.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            oi.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            oi.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            oi.setTime(cursor.getString(cursor.getColumnIndex("time")));
            if (res.containsKey(company)){
                res.get(company).add(oi);
            } else {
                res.put(company, new ArrayList<OfferInfo>());
                res.get(company).add(oi);
            }
        }

        cursor.close();
        db.close();

        return res;
    }

    /**
     * 按时间倒序排序
     */
    public List<OfferInfo> findAllByIds(){
        List<OfferInfo> res = new ArrayList<>();

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from offerdetail order by _id desc", new String[]{});

        while(cursor.moveToNext()){
            OfferInfo oi = new OfferInfo();
            oi.setCity(cursor.getString(cursor.getColumnIndex("city")));
            oi.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            oi.setCompany(cursor.getString(cursor.getColumnIndex("company")));
            oi.setPosition(cursor.getString(cursor.getColumnIndex("position")));
            oi.setSalary(cursor.getString(cursor.getColumnIndex("salary")));
            oi.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
            oi.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            oi.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            oi.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            oi.setTime(cursor.getString(cursor.getColumnIndex("time")));
            res.add(oi);
        }

        cursor.close();
        db.close();

        return res;
    }
}
