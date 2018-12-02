package com.gc.materialdesigndemo.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;


public class KategorijeFragment extends Fragment {

    private BibliotekaDBOpenHelper boh;
    private SQLiteDatabase sdb;
    String[] koloneRezulat;
    String where = null;
    //Definišemo argumente u where upitu, group by, having i order po potrebi
    String whereArgs[] = null;
    String groupBy = null;
    String having = null;
    String order = null;

    ArrayList<String> kategorije = new ArrayList<String>();
    //    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
//    ArrayList<Autor> autori = new ArrayList<Autor>();
//    ArrayList<Knjiga> listaKnjigaPoKategoriji = new ArrayList<Knjiga>();
//    ArrayList<Knjiga> listaKnjigaPoAutorima = new ArrayList<Knjiga>();
    Integer temp = 0;

    String listaKnjiga;
    String listaKategorija;

    public KategorijeFragment()
    {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final ListView listaKategorija = (ListView) getView().findViewById(R.id.listaKategorija);
        final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, kategorije);
        final ImageButton dugmeDodajKategoriju = (ImageButton) getView().findViewById(R.id.dDodajKategoriju);
        final ImageButton dugmePretraga = (ImageButton) getView().findViewById(R.id.dPretraga);
        final ArrayList<String> kategorije_temp = new ArrayList<String>();
        final ArrayAdapter<String> adapterKategorije_temp = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, kategorije_temp);
        final EditText tekst = (EditText) getView().findViewById(R.id.tekstPretraga);
        ////////////////////////////////////////////////////////////////////////////////////////////

        boh = new BibliotekaDBOpenHelper(getActivity(), "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();

        koloneRezulat= new String[]{BibliotekaDBOpenHelper.ID_KATEGORIJE, BibliotekaDBOpenHelper.NAZIV_KATEGORIJE};
        popuniListuKategorijePodacima();
        listaKategorija.setAdapter(adapterKategorije);

        dugmeDodajKategoriju.setVisibility(View.GONE);
        dugmePretraga.setEnabled(true);
        dugmePretraga.setClickable(true);

        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                ListaKnjigaPoKatFragment f = new ListaKnjigaPoKatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("nazivKategorije", (String)listaKategorija.getItemAtPosition(position));
                f.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mjesto1, f).addToBackStack(null).commit();
            }});

        dugmePretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dugmeDodajKategoriju.setVisibility(View.GONE);

                if (!tekst.getText().toString().isEmpty()) {
                    dugmeDodajKategoriju.setVisibility(View.GONE);
                    adapterKategorije_temp.clear();
                    adapterKategorije_temp.addAll(kategorije);
                    adapterKategorije_temp.getFilter().filter(tekst.getText().toString());
                    adapterKategorije_temp.notifyDataSetChanged();
                    listaKategorija.setAdapter(adapterKategorije_temp);

                    if (adapterKategorije_temp.getCount() == 0) {
                        dugmeDodajKategoriju.setVisibility(View.VISIBLE);
                        dugmeDodajKategoriju.setEnabled(true);
                    }
                    else dugmeDodajKategoriju.setVisibility(View.GONE);
                } else {
                    dugmeDodajKategoriju.setVisibility(View.GONE);
                    adapterKategorije.notifyDataSetChanged();
                    listaKategorija.setAdapter(adapterKategorije);
                    adapterKategorije.notifyDataSetChanged();
                }
            }
        });

        dugmeDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tekst.getText().toString().isEmpty()){
                    Kategorija nova = new Kategorija(sdb);
                    long indeks = nova.dodajKategoriju(tekst.getText().toString());
                    if(indeks != -1)
                        Toast.makeText(getActivity(),"Kategorija uspjesno dodana. ID = " + indeks, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(),"Kategorija već postoji!", Toast.LENGTH_LONG).show();

                }
                tekst.setText("");
                popuniListuKategorijePodacima();
                adapterKategorije.notifyDataSetChanged();
                listaKategorija.setAdapter(adapterKategorije);
                adapterKategorije.notifyDataSetChanged();
                dugmeDodajKategoriju.setVisibility(View.GONE);
                listaKategorija.setVerticalScrollBarEnabled(true);

                kategorije_temp.clear();
                kategorije_temp.addAll(kategorije);
                adapterKategorije_temp.notifyDataSetChanged();
                adapterKategorije.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        container.clearDisappearingChildren();
        return inflater.inflate(R.layout.kategorije_fragment, container, false);

    }

    public void popuniListuKategorijePodacima()
    {
        kategorije.clear();

        Cursor cursor = sdb.query(BibliotekaDBOpenHelper.KATEGORIJE,
                koloneRezulat, where,
                whereArgs, groupBy, having, order);
        int INDEX_KOLONE_NAZIV_KATEGORIJE = cursor.getColumnIndexOrThrow(BibliotekaDBOpenHelper.NAZIV_KATEGORIJE);
        while(cursor.moveToNext())
            kategorije.add(cursor.getString(INDEX_KOLONE_NAZIV_KATEGORIJE));

    }

}
