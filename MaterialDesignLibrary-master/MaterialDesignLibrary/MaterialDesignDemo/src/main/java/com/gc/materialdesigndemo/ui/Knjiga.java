package com.gc.materialdesigndemo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.animation.AnimationUtils;

import java.net.URL;
import java.util.ArrayList;

public class Knjiga implements Parcelable {

    public Knjiga(String id, String naziv, ArrayList<String> autori, String opis, String datum, String slika, int brojStranica, String kategorije)
    {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datum;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.kategorije = kategorije;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<String> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    ArrayList autori;
    String opis;
    String datumObjavljivanja;
    String slika;
    int brojStranica;
    String id;
    String naziv;
    String kategorije;
    Integer idKategorije;

    public String getKategorija() {
        return kategorije;
    }

    public void setKategorija(String kategorija) {
        this.kategorije = kategorija;
    }


    public Knjiga(Parcel in)
    {
        id = in.readString();
        brojStranica = in.readInt();
        naziv = in.readString();
        autori = in.readArrayList(autori.getClass().getClassLoader());
        kategorije = in.readString();
        opis = in.readString();
        datumObjavljivanja = in.readString();
        slika = in.readString();
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeString(opis);
        dest.writeString(slika);
        dest.writeList(autori);
        dest.writeInt(brojStranica);
        dest.writeString(kategorije);
        dest.writeString(datumObjavljivanja);
    }

    public long dodajKnjigu(Knjiga k)
    {
        //Dodaj knjigu u bazu, upisi u tabelu kategorija, autori i autorstvo

        String where = "idWebServis = '" + k.getId() + "' AND naziv = '" + k.getNaziv() + "'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        long indeks = -1;

        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        boh = new BibliotekaDBOpenHelper(MainActivity.getContext(),"biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();
        String[] koloneRezulat = new String[]{"_id"};

        if (sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having,
                order).getCount() == 0) {
            ContentValues noveVrijednosti = new ContentValues();
            noveVrijednosti.put("naziv", k.getNaziv());
            noveVrijednosti.put("opis", k.getOpis());
            noveVrijednosti.put("datumObjavljivanja", k.getDatumObjavljivanja());
            noveVrijednosti.put("brojStranica", k.getBrojStranica());
            noveVrijednosti.put("idWebServis", k.getId());
            noveVrijednosti.put("slika", k.getSlika().toString());
            noveVrijednosti.put("pregledana", 0);

            //id kategorije cemo potraziti u tabeli Kategorija, ako nema, dodat cemo kategoriju u listu
            //Vrati id kategorije, ako nema, dodaj kategoriju i vrati novi id

            Kategorija kat = new Kategorija(sdb);
            long ind = 0;
            ind = kat.dodajKategoriju(k.getKategorija());
            int idKat = -1;
            where = "naziv = '" + k.getKategorija() + "'";

            Cursor cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                    order);

            int INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow(BibliotekaDBOpenHelper.ID_KATEGORIJE);
            while(cursor.moveToNext())
                idKat = cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE);

            //Nasli smo id kategorije, sada upisimo u Knjigu

            noveVrijednosti.put("idKategorije", idKat);

            //Sada provjerimo autore, da li svi postoje u tabeli Autori
            for(int i = 0; i < k.getAutori().size(); i++)
            {
                Autor a = new Autor();
                a.dodajAutora(getAutori().get(i));
            }

            //Sada dodajmo u tabelu Knjiga ovu knjigu
            sdb.insert("Knjiga", null, noveVrijednosti);

            //Sada vratimo indeks dodane knjige
            where = "naziv = '" + k.getNaziv() + "' AND idWebServis = '" + k.getId() + "'";
            cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            int indeks_kolone = cursor.getColumnIndexOrThrow("_id");
            while (cursor.moveToNext()) indeks = cursor.getLong(indeks_kolone);

            //Sada vratimo iz tabele AUTOR id datog autora odnsno datih autora
            int idAutora = 0;
            for(int i = 0; i < k.getAutori().size(); i++) {
                where = "ime = '" + k.getAutori().get(i) + "'";
                cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                        order);
                int kolona = cursor.getColumnIndexOrThrow("_id");
                while (cursor.moveToNext()) idAutora = cursor.getInt(kolona);
                //Sad smo pokupili idAutora, imamo id Knjige, sada provjerimo ima li taj red u AUTORSTVU, ako nema dodajemo
                where = "idautora = '" + idAutora + "' AND idknjige = '" + indeks + "'";
                if (sdb.query("Autorstvo", koloneRezulat, where, whereArgs, groupBy, having,
                        order).getCount() == 0) {
                    ContentValues nove = new ContentValues();
                    nove.put("idknjige", indeks);
                    nove.put("idautora", idAutora);
                    sdb.insert("Autorstvo", null, nove);
                }

            }
            cursor.close();
            return  indeks;
        }
        else
            return indeks;
    }
}
