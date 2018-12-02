package com.gc.materialdesigndemo.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DodajKnjiguAdapter extends ArrayAdapter<Knjiga> {

    private Context mContext;
    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public DodajKnjiguAdapter(@NonNull Context context, ArrayList<Knjiga> list) {
        super(context, 0,  list);
        mContext = context;
        knjige = list;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.element_liste_dodaj_knjigu, parent, false);

        final Knjiga knjiga = knjige.get(position);

        //ImageView image = (ImageView) listItem.findViewById(R.id.eNaslovna);
        //image.setImageBitmap(currentBook.getNaslovna());
        String autori =" ";
        for(int i =0; i<knjiga.getAutori().size(); i++)
            autori+=knjiga.getAutori().get(i) + "; ";

        TextView opis = (TextView) listItem.findViewById(R.id.eOpis);
        opis.setText("Autori:" + autori );

        TextView brojStranica = (TextView) listItem.findViewById(R.id.eBrojStranica);
        brojStranica.setText(String.valueOf("Kategorija:" + knjiga.getKategorija()));

        TextView datum = (TextView) listItem.findViewById(R.id.eDatumObjavljivanja);
        datum.setText("Naziv: " + knjiga.getNaziv());

        //Treba napraviti da se prikazuje i slika
        ImageView slika = (ImageView)listItem.findViewById(R.id.slika);
        Picasso.get().load(knjiga.getSlika().toString()).into(slika);

        Button dPrepporuci = (Button)listItem.findViewById(R.id.dPreporuci);

        dPrepporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Paziti da se doda i kategorija ako je nova, i autoru da se dodaju knjige
                long id = knjiga.dodajKnjigu(knjiga);
                if(id != -1)
                    Toast.makeText(getContext(), "Knjiga uspjesno dodana! ID = " + id, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Knjiga vec postoji!", Toast.LENGTH_LONG).show();

            }
        });

        return listItem;
    }




}
