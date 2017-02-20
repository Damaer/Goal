package cn.goal.goal.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenlin on 19/02/2017.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String goalTable = "create table goal(id INTEGER PRIMARY KEY AUTOINCREMENT, _id varchar(100), title varchar(200), content varchar(2000), begin varchar(20), plan varchar(20), end varchar(20), createAt varchar(20), updateAt varchar(20), finished int)";
        db.execSQL(goalTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
