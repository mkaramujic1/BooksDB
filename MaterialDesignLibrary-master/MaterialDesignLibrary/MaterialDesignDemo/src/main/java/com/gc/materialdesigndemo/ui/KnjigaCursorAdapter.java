package com.gc.materialdesigndemo.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class KnjigaCursorAdapter extends CursorAdapter {

    Context mContext;

    public KnjigaCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.element_liste_knjiga, parent, false);
    }
    @Override
    public  void bindView(View view, Context context, Cursor cursor)
    {
        TextView ime = (TextView) view.findViewById(R.id.eBrojStranica);
        ime.setText(cursor.getString(cursor.getColumnIndex("brojStranica")));
        TextView zanr = (TextView) view.findViewById(R.id.eDatumObjavljivanja);
        zanr.setText(cursor.getString(cursor.getColumnIndex("datumObjavljivanja")));
        TextView opiss = (TextView) view.findViewById(R.id.eOpis);
//        opiss.setText(cursor.getString(cursor.getColumnIndex("opis")));
        opiss.setText("OPPIIIIIS....");
        Button dPrepporuci = (Button)view.findViewById(R.id.dPreporuci);

        ArrayList autori;
        String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
        String datumObjavljivanja = cursor.getString(cursor.getColumnIndexOrThrow("datumObjavljivanja"));
        String slika = cursor.getString(cursor.getColumnIndexOrThrow("slika"));
        ImageView pic = (ImageView)view.findViewById(R.id.slika);
        //Picasso.get().load(knjiga.getSlika().toString()).into(slika);
        Picasso.get().load(slika).into(pic);
        int brojStranica  = cursor.getInt(cursor.getColumnIndexOrThrow("brojStranica"));
        String id = cursor.getString(cursor.getColumnIndexOrThrow("idWebServis"));
        String naziv = cursor.getString(cursor.getColumnIndexOrThrow("naziv"));
        Integer idKategorije = cursor.getInt(cursor.getColumnIndexOrThrow("idKategorije"));
        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        boh = new BibliotekaDBOpenHelper(context, "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();

        //Sada pokupimo naziv kategorije iz baze
        Kategorija kat = new Kategorija(sdb);
        String nazivKat = "";
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] koloneRezulat = new String[]{"naziv"};
        where = "_id = '" + idKategorije + "'";

        cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                order);

        int INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow(BibliotekaDBOpenHelper.NAZIV_KATEGORIJE);
        while(cursor.moveToNext())
            nazivKat = cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE);

        //Sada pokupimo autore iz baze
        //Prvo nam treba id knjige
        where = "naziv = '" + naziv + "' AND idWebServis = '" + id + "'";
        koloneRezulat = new String[]{"_id"};
        cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having,
                order);
        int indeksKnjige = 0;
        INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("_id");
        while(cursor.moveToNext())
            indeksKnjige = cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE);

        //Sada kad imamo idKnjige, pokupimo id-ove autora koji imaju ovaj idKnjige
        ArrayList<Integer> idAutora = new ArrayList<>();
        where = "idknjige = '" + indeksKnjige + "'";
        koloneRezulat = new String[]{"idautora"};
        cursor = sdb.query("Autorstvo", koloneRezulat, where, whereArgs, groupBy, having,
                order);
        int indeksAutora = 0;
        INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("idautora");
        while(cursor.moveToNext())
            idAutora.add(cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE));

        //Sada pokupimo autore sa ovim idevima
        ArrayList<String> authors = new ArrayList<>();
        for(int i = 0; i <idAutora.size(); i++){
            where = "_id = '" + idAutora.get(i).toString() + "'";
            koloneRezulat = new String[]{"ime"};
            cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                    order);
            INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("ime");
            while(cursor.moveToNext())
                authors.add(cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE));
        }

        final Knjiga k = new Knjiga(id, naziv, authors, opis, datumObjavljivanja, slika, brojStranica, nazivKat);


        dPrepporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPreporuci kf = new FragmentPreporuci();
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                Bundle arg = new Bundle();
                ArrayList<Knjiga> book = new ArrayList<>();
                book.add(k);
                arg.putParcelableArrayList("knjige", book);
                kf.setArguments(arg);
                fm.beginTransaction().replace(R.id.mjesto1, kf).addToBackStack(null).commit();
            }
        });
    }
}
