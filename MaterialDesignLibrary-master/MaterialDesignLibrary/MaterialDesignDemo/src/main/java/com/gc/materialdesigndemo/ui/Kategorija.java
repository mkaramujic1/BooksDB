package com.gc.materialdesigndemo.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Kategorija {

    private SQLiteDatabase sdb;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    String naziv;
    long _id;

    Kategorija(SQLiteDatabase sdb) {
        this.sdb = sdb;
    }

    public long dodajKategoriju(String naziv) {
        String where = "naziv = '" + naziv + "'";
        //Definišemo argumente u where upitu, group by, having i order po potrebi
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        long indeks = -1;
        long beze;
        String[] koloneRezulat = new String[]{BibliotekaDBOpenHelper.ID_KATEGORIJE};

        if (sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                order).getCount() == 0) {
            ContentValues noveVrijednosti = new ContentValues();
            noveVrijednosti.put(BibliotekaDBOpenHelper.NAZIV_KATEGORIJE, naziv);
            sdb.insert(BibliotekaDBOpenHelper.KATEGORIJE, null, noveVrijednosti);
            Cursor cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            int indeks_kolone = cursor.getColumnIndexOrThrow("_id");
            while (cursor.moveToNext()) indeks = cursor.getLong(indeks_kolone);
            cursor.close();
            return  indeks;
        }
        else
            return indeks;
    }
    public ArrayList<Knjiga> knjigeKategorije(long idKategorije){
        ArrayList<Knjiga> knjige = new ArrayList<>();

        ////////////////////////////////////////////////////////////////////////////////////////////
        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        String where = "_id = '" + idKategorije + "'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] koloneRezulat = new String[]{"_id"};

        boh = new BibliotekaDBOpenHelper(MainActivity.getContext(), "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();

        koloneRezulat = new String[]{"_id, idKategorije, naziv, datumObjavljivanja, slika, brojStranica, pregledana, opis, idWebServis"};
        where = "idKategorije = '" + idKategorije + "'";
        Cursor cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having, order);
        while(cursor.moveToNext()) {

            String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
            String datumObjavljivanja = cursor.getString(cursor.getColumnIndexOrThrow("datumObjavljivanja"));
            String slika = cursor.getString(cursor.getColumnIndexOrThrow("slika"));
            int brojStranica = cursor.getInt(cursor.getColumnIndexOrThrow("brojStranica"));
            String id = cursor.getString(cursor.getColumnIndexOrThrow("idWebServis"));
            String naziv = cursor.getString(cursor.getColumnIndexOrThrow("naziv"));
            ArrayList autori;
            String nazivKat = "";
            koloneRezulat = new String[]{"naziv"};
            where = "_id = '" + idKategorije + "'";

            cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                    order);

            int INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow(BibliotekaDBOpenHelper.NAZIV_KATEGORIJE);
            while (cursor.moveToNext())
                nazivKat = cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE);

            //Prvo nam treba id knjige
            where = "naziv = '" + naziv + "' AND idWebServis = '" + id + "'";
            koloneRezulat = new String[]{"_id"};
            cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            int indeksKnjige = 0;
            INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("_id");
            while (cursor.moveToNext())
                indeksKnjige = cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE);

            //Sada kad imamo idKnjige, pokupimo id-ove autora koji imaju ovaj idKnjige
            ArrayList<Integer> idAutora = new ArrayList<>();
            where = "idknjige = '" + indeksKnjige + "'";
            koloneRezulat = new String[]{"idautora"};
            cursor = sdb.query("Autorstvo", koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            int indeksAutora = 0;
            INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("idautora");
            while (cursor.moveToNext())
                idAutora.add(cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE));

            //Sada pokupimo autore sa ovim idevima
            ArrayList<String> authors = new ArrayList<>();
            for (int i = 0; i < idAutora.size(); i++) {
                where = "_id = '" + idAutora.get(i).toString() + "'";
                koloneRezulat = new String[]{"ime"};
                cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                        order);
                INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("ime");
                while (cursor.moveToNext())
                    authors.add(cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE));
            }

            final Knjiga k = new Knjiga(id, naziv, authors, opis, datumObjavljivanja, slika, brojStranica, nazivKat);
            knjige.add(k);
        }

        return  knjige;

    }
}

