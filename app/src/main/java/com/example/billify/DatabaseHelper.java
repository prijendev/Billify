package com.example.billify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class DatabaseHelper extends SQLiteOpenHelper
{
    private Context mycontext;
    //Birday_Reminder birday = new Birday_Reminder();
    private static String DATABASE_NAME = "billify.db";
    private static String DATABASE_PATH=null ;
    public SQLiteDatabase myDataBase;

    public ArrayList<String> name=new ArrayList<String>();
    public ArrayList<String> image=new ArrayList<>();
    public ArrayList<String> email=new ArrayList<>();
    public ArrayList<String> b_date=new ArrayList<>();
    public ArrayList<Friend> friends=new ArrayList<>();


    private static Calendar startDate, endDate;
    public Friend friend;
    public void createDataBase(){

        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.d("Database", "Exists");
        }
        else
            {
                Log.d("Database doesnot exist", "Exists");

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.

            this.getReadableDatabase();
                this.close();

            try
            {

                copyDataBase();

            }
            catch (IOException e)
            {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {

        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }
        catch(SQLiteException e)
        {

            //database does't exist yet.

        }

        if(checkDB != null)
        {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException
    {

        //Open your local db as the input stream
        InputStream myInput = mycontext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH+DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException
    {

        //Open the database
        String myPath = DATABASE_PATH+DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        myDataBase.close();

    }

    @Override
    public synchronized void close()
    {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.



    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,1);

        DATABASE_PATH= "/data/data/" + context.getPackageName() + "/" + "databases/";
        this.mycontext=context;

    }

    public ArrayList<Friend> geFriend()
    {

        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        if (dbFile.exists())
        {
            Log.d("Database", "Exists");

        }
        else
            {
            Log.d("Database", " Not Exists");
        }


        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="partner";
            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb, null);

            Log.d("branch", "database accessed successfully  123456");

            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
               //list.add(cursor.getString(1));
                friend = new Friend();
                friend.setName(cursor.getString(1));
                friend.setProfile(cursor.getString(5));
                friend.setEmail(cursor.getString(3));

                friend.setContact(cursor.getString(2));
                friend.setId(cursor.getLong(0));

                friend.setBalance(cursor.getInt(4));

                friends.add(friend);

                Log.d("branch", "database accessed successfully  ");
                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  friends;
    }

    public  boolean addNew(String name,String email,String contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        Log.d("instered", "sadsadad");
        values.put("Name", name);
        values.put("Email", email);

        values.put("Contact",contact);


// Insert the new row, returning the primary key value of the new row
        try
        {
            long newRowId = db.insert("partner", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }









}