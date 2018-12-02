package com.gc.materialdesigndemo.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Autor {

    String ime;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    Integer _id;

    public String getImeiPrezime() {
        return ime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.ime = imeiPrezime;
    }


    public Autor(String ime, Integer id)
    {
        this.ime = ime;
        this._id = id;

    }

    public Autor(){

    }
    public long dodajAutora(String a)
    {
        String where = "ime = '" + a + "'";
        //Definišemo argumente u where upitu, group by, having i order po potrebi
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        long indeks = -1;
        long beze;
        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        boh = new BibliotekaDBOpenHelper(MainActivity.getContext(),"biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();
        String[] koloneRezulat = new String[]{"_id"};

        if (sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                order).getCount() == 0) {
            ContentValues noveVrijednosti = new ContentValues();
            noveVrijednosti.put("ime", a);
            sdb.insert("Autor", null, noveVrijednosti);
            Cursor cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            int indeks_kolone = cursor.getColumnIndexOrThrow("_id");
            while (cursor.moveToNext()) indeks = cursor.getLong(indeks_kolone);
            cursor.close();
            return  indeks;
        }
        else
            return indeks;
    }


}
