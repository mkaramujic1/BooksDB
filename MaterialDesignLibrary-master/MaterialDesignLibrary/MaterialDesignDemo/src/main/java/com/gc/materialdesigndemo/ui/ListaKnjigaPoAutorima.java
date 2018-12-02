package com.gc.materialdesigndemo.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;

public class ListaKnjigaPoAutorima extends Fragment {
    String nazivAutora;

    public ListaKnjigaPoAutorima() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final ListView listaKnjiga = (ListView) getView().findViewById(R.id.listaKnjiga);
        final TextView meni = (TextView) getView().findViewById(R.id.meni);
        meni.setText(nazivAutora);

//        final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, autori);
        ////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<Knjiga> knjige = new ArrayList<>();
        long id = -1;
        //Pronadjimo idAutora
        String where = "ime = '" + nazivAutora + "'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        boh = new BibliotekaDBOpenHelper(MainActivity.getContext(), "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();
        String[] koloneRezulat = new String[]{"_id"};

        Cursor cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
                order);
        int INDEX_KOLONE_ID_KNJIGE = cursor.getColumnIndexOrThrow("_id");
        while (cursor.moveToNext())
            id = cursor.getLong(INDEX_KOLONE_ID_KNJIGE);

        knjige = knjigeAutora(id);
        KnjigaAdapter adapter = new KnjigaAdapter(getActivity(), knjige);
        listaKnjiga.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        container.clearDisappearingChildren();
        nazivAutora = getArguments().getString("nazivAutora");
        return inflater.inflate(R.layout.lista_knjiga_po_kat_fragment, container, false);
    }

    public ArrayList<Knjiga> knjigeAutora(long idAutora) {

        ArrayList<Knjiga> knjige = new ArrayList<>();
        String where = "idautora = '" + idAutora + "'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        long indeks = -1;
        ArrayList<Integer> ideviKnjiga = new ArrayList<>();

        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        boh = new BibliotekaDBOpenHelper(MainActivity.getContext(), "biblioteka.db", null, 1);
        sdb = boh.getReadableDatabase();
        String[] koloneRezulat = new String[]{"idknjige"};

        Cursor cursor = sdb.query("Autorstvo", koloneRezulat, where, whereArgs, groupBy, having,
                order);
        int INDEX_KOLONE_ID_KNJIGE = cursor.getColumnIndexOrThrow("idknjige");
        while (cursor.moveToNext()) ideviKnjiga.add(cursor.getInt(INDEX_KOLONE_ID_KNJIGE));

        //Dodaj ideve knjiga

        for (int i = 0; i < ideviKnjiga.size(); i++) {
            koloneRezulat = new String[]{"_id, idKategorije, naziv, datumObjavljivanja, slika, brojStranica, pregledana, opis, idWebServis"};
            where = "_id = '" + ideviKnjiga.get(i) + "'";
            cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having, order);
            while (cursor.moveToNext()) {

                String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
                String datumObjavljivanja = cursor.getString(cursor.getColumnIndexOrThrow("datumObjavljivanja"));
                String slika = cursor.getString(cursor.getColumnIndexOrThrow("slika"));
                int brojStranica = cursor.getInt(cursor.getColumnIndexOrThrow("brojStranica"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("idWebServis"));
                String naziv = cursor.getString(cursor.getColumnIndexOrThrow("naziv"));
                Integer idKategorije = cursor.getInt(cursor.getColumnIndexOrThrow("idKategorije"));
                Integer idKnjige = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                ArrayList autori;
                String nazivKat = "";
                koloneRezulat = new String[]{"naziv"};
                where = "_id = '" + idKategorije + "'";

                cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE, koloneRezulat, where, whereArgs, groupBy, having,
                        order);

                int INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow(BibliotekaDBOpenHelper.NAZIV_KATEGORIJE);
                while (cursor.moveToNext())
                    nazivKat = cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE);


                //Sada kad imamo idKnjige, pokupimo id-ove autora koji imaju ovaj idKnjige
                ArrayList<Integer> ideviAutora = new ArrayList<>();
                where = "idknjige = '" + idKnjige + "'";
                koloneRezulat = new String[]{"idautora"};
                cursor = sdb.query("Autorstvo", koloneRezulat, where, whereArgs, groupBy, having,
                        order);
                int indeksAutora = 0;
                INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow("idautora");
                while (cursor.moveToNext())
                    ideviAutora.add(cursor.getInt(INDEX_KOLONE_NAZIV_KATEGORIJE));

                //Sada pokupimo autore sa ovim idevima
                ArrayList<String> authors = new ArrayList<>();
                for (int j = 0; j < ideviAutora.size(); j++) {
                    where = "_id = '" + ideviAutora.get(j).toString() + "'";
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
        }
        return knjige;
    }
}
