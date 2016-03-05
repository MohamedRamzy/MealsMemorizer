package com.mohamedramzy.mealsmemorizer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohamedramzy.mealsmemorizer.model.Meal;

/**
 * Created by mmahfouz on 3/3/2016.
 */
public class DBManager {
    private DBHelper dbHelper;
    private static DBManager instance;

    private DBManager (Context context){
        this.dbHelper = new DBHelper(context);
    }


    public static synchronized DBManager getInstance(Context context){
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    private SQLiteDatabase getWritableDB(){
        return this.dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDB(){
        return this.dbHelper.getReadableDatabase();
    }

    public Cursor queryAll (){
        // get all meals
        Cursor cursor = getReadableDB().query(MEALS_TABLE_NAME, null,
                null,
                null,
                null,null,null);
        return cursor;
    }

    public Cursor queryAllFavourites (){
        // get all meals
        Cursor cursor = getReadableDB().query(MEALS_TABLE_NAME, null,
                COLUMN_MEAL_FAV +"=1",
                null,
                null,null,null);
        return cursor;
    }

    public long insert(Meal meal) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME,meal.getName());
        values.put(COLUMN_MEAL_FAV,meal.getFav());
        values.put(COLUMN_MEAL_CAT,meal.getCategoryID());

        long rowID = getWritableDB().insert(MEALS_TABLE_NAME, null, values);

        if(rowID > 0){
            return rowID;
        }
        return -1;
    }

    public boolean queryIsFavourite(String movieID){
        // check the DB if this meal is a favourite one
        Cursor cursor = getReadableDB().query(DBManager.MEALS_TABLE_NAME, null,
                DBManager._ID + "=?" + " AND " + DBManager.COLUMN_MEAL_FAV + "=1",
                new String[]{movieID},
                null, null, null);
        return cursor.getCount() > 0;
    }

    public boolean update(Meal meal){
        ContentValues values = new ContentValues();
        values.put(_ID,meal.getId());
        values.put(COLUMN_MEAL_NAME,meal.getName());
        values.put(COLUMN_MEAL_FAV,meal.getFav());
        values.put(COLUMN_MEAL_CAT,meal.getCategoryID());


        long updated = getWritableDB().update(DBManager.MEALS_TABLE_NAME, values, DBManager._ID + "=?", new String[]{meal.getId() + ""});

        if(updated > 0)
            return true;
        return false;
    }

    public boolean delete(Meal meal) {
        long deleted = getWritableDB().delete(DBManager.MEALS_TABLE_NAME, DBManager._ID + "=?", new String[]{meal.getId() + ""});

        if(deleted > 0)
            return true;
        return false;
    }


    public boolean addToFavourite (Meal meal){
        meal.setFav(1);
        return update(meal);
    }

    public boolean removeFromFavourite (Meal meal){
        ContentValues values = new ContentValues();
        values.put(_ID,meal.getId());
        values.put(COLUMN_MEAL_NAME,meal.getName());
        values.put(COLUMN_MEAL_FAV,0);
        values.put(COLUMN_MEAL_CAT,meal.getCategoryID());

        long updated = getWritableDB().update(DBManager.MEALS_TABLE_NAME, values, DBManager._ID+"=?", new String[]{meal.getId()+""});

        if(updated > 0)
            return true;
        return false;
    }

    public static final String DB_NAME = "meals.db";
    public static final int DB_VERSION = 2;

    public static final String _ID = "meal_id";
    public static final String COLUMN_MEAL_NAME = "name";
    public static final String COLUMN_MEAL_FAV = "favourite";
    public static final String COLUMN_MEAL_CAT= "category";


    static final String MEALS_TABLE_NAME = "meals";
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + MEALS_TABLE_NAME + "(" +
                    _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEAL_NAME + " TEXT NOT NULL, " +
                    COLUMN_MEAL_CAT + " INTEGER NOT NULL, " +
                    COLUMN_MEAL_FAV + " INTEGER);";

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ MEALS_TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ MEALS_TABLE_NAME);
            onCreate(db);
        }
    }

}
