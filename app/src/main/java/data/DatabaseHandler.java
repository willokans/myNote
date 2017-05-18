package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import model.MyNote;

/**
 * Created by Will on 08/03/2017.
 */

public class DatabaseHandler  extends SQLiteOpenHelper{


    //Declare instance variable
    private final ArrayList<MyNote> noteList = new ArrayList<>();

    //Create a constructor
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table with sql code
        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.TITLE_NAME +
                " TEXT, " + Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME +
                " LONG);";

        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //database code to drop table is exists
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //CREATE A NEW TABLE METHOD
        onCreate(db);

    }



    //delete a note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                new String[] { String.valueOf(id)});


        db.close();

    }

//    //update a note
//    public void updateNote(int id ) {
//
//        //Instantiate our sqlite Database
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        //String to hold out  sql query
//        String selectQuery = "UPDATE " + Constants.TABLE_NAME + " SET " + Constants.TITLE_NAME = ;
//
//
//
//    }


    //add content to table
    public void addNotes (MyNote note) {

        //Instantiate our sqlite Database
        SQLiteDatabase db = this.getWritableDatabase();

        //create a data structure type used for storing data.. like a HashMap
        ContentValues values = new ContentValues();

        //put value into database
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());


        db.insert(Constants.TABLE_NAME, null, values);


        db.close();
    }


    //Get all notes to populate in our view list
    public ArrayList<MyNote> getNotes() {

        //String to hold out  sql query
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        //Instance of SQLight Database
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME,
                }, null, null, null, null, Constants.DATE_NAME + " ASC");

        //loop through cursor
        if (cursor.moveToFirst()) {

            do {

                MyNote note = new MyNote();
                note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
                note.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                //retrieve date
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance();
                String dataDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                //pass in out data
                note.setRecordDate(dataDate);

                noteList.add(note);


            } while (cursor.moveToNext());


        }

        return  noteList;
    }
}
