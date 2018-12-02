package com.gc.materialdesigndemo.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BibliotekaDBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Biblioteka.db";
    public static final int DATABASE_VERSION = 1;

    //TABELA KATEGORIJE
    public static final String KATEGORIJE = "Kategorija";
    public static final String NAZIV_KATEGORIJE ="naziv";
    public static final String ID_KATEGORIJE="_id";
    public static final String KNJIGA = "Knjiga";
    public static final String AUTOR = "Autor";
    public static final String AUTORSTVO = "Autorstvo";

    String DATABASE_CREATE = "CREATE TABLE " + KATEGORIJE + "("
            + ID_KATEGORIJE + " integer primary key autoincrement, "
            + NAZIV_KATEGORIJE + " text" + ")"
            ;


    public BibliotekaDBOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, Integer version) {
        super(context, name, factory, version);
    }

    //Poziva se kad ne postoji baza
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL("CREATE TABLE " + "Knjiga" + "("
                + "_id" + " integer primary key autoincrement, "
                + "naziv" + " text, "
                + "opis" + " text, "
                + "datumObjavljivanja" + " text, "
                + "brojStranica" + " integer, "
                + "idWebServis" + " text, "
                + "idKategorije" + " integer, "
                + "slika" + " text, "
                + "pregledana" + " integer" +
                ")");
        db.execSQL("CREATE TABLE " + "Autor" + "("
                + "_id" + " integer primary key autoincrement, "
                + "ime" + " text" +
                ")");
        db.execSQL("CREATE TABLE " + "Autorstvo" + "("
                + "_id" + " integer primary key autoincrement, "
                + "idautora" + " integer, "
                + "idknjige" + " integer" +
                ")");
    }

    // Poziva se kada se ne poklapaju verzije baze na disku i trenutne baze
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + KATEGORIJE);
        db.execSQL("DROP TABLE IF EXISTS " + KNJIGA);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOR);
        db.execSQL("DROP TABLE IF EXISTS " + AUTORSTVO);

        // Create tables again
        onCreate(db);
    }
}

