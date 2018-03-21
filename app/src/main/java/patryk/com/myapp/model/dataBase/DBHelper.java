package patryk.com.myapp.model.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by patryk on 21.03.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "beers.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + DataBaseConstans.TABLE_NAME + "(" + DataBaseConstans.Columns.BEER_ID + "integer primary key, " + DataBaseConstans.Columns.SERIALIZED_BEER + " text ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstans.TABLE_NAME);
        onCreate(db);
    }
}
