package offershow.online.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static String db_name = "data";
    public DBOpenHelper(Context context) {
        super(context, db_name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE offerdetail (_id INTEGER PRIMARY KEY, company TEXT, position TEXT, salary TEXT, city TEXT, remark TEXT" +
                ", number INTEGER, score INTEGER, ip TEXT, time TEXT)");
        db.execSQL("CREATE TABLE offersuggestion( content TEXT PRIMARY KEY, times INTEGER, history INTEGER)");
        db.execSQL("CREATE TABLE offermessages(_id INTEGER PRIMARY KEY, offerid INTEGER, content TEXT, time TEXT)"); // added in v2
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
            case 2:
                db.execSQL("CREATE TABLE offermessages(_id INTEGER PRIMARY KEY, offerid INTEGER, content TEXT, time TEXT)");
            default:
                break;
        }

    }
}
