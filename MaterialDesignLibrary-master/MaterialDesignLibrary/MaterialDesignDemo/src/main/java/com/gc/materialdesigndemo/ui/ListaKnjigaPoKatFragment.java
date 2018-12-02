package com.gc.materialdesigndemo.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;

public class ListaKnjigaPoKatFragment extends Fragment  {

    String nazivKat;

    public ListaKnjigaPoKatFragment()
    {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        final ListView listaKnjiga = (ListView) getView().findViewById(R.id.listaKnjiga);
        final TextView meni =(TextView) getView().findViewById(R.id.meni);
        meni.setText(nazivKat);

//        final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, autori);
        ////////////////////////////////////////////////////////////////////////////////////////////
        BibliotekaDBOpenHelper boh;
        SQLiteDatabase sdb;
        String where = "naziv = '" + nazivKat + "'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] koloneRezulat = new String[]{"_id"};
        int idKat=0;

        boh = new BibliotekaDBOpenHelper(getActivity(), "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();

        Cursor cursor = sdb.query("Kategorija", koloneRezulat, where, whereArgs, groupBy, having,
                order);

        int INDEX_KOLONE_ID = cursor.getColumnIndexOrThrow("_id");
        while(cursor.moveToNext()){
            idKat = cursor.getInt(INDEX_KOLONE_ID);
            break;
        }

        //Sada kad smoo nasli indeks kategorije sa datim nazivom, prolazimo kroz tabelu KNJIGA i trazimo knjige
        //koje imaju idKategorije kao ovaj
        koloneRezulat = new String[]{"_id, idKategorije, naziv, datumObjavljivanja, slika, brojStranica, pregledana, opis, idWebServis"};
        where = "idKategorije = '" + idKat + "'";
        cursor = sdb.query("Knjiga", koloneRezulat, where, whereArgs, groupBy, having, order);
        KnjigaCursorAdapter adapter = new KnjigaCursorAdapter(getActivity(), cursor);
        listaKnjiga.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        container.clearDisappearingChildren();
        nazivKat = getArguments().getString("nazivKategorije");
        return inflater.inflate(R.layout.lista_knjiga_po_kat_fragment, container, false);
    }

}

