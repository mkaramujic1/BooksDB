package com.gc.materialdesigndemo.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
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

import com.gc.materialdesigndemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KnjigaAdapter extends ArrayAdapter<Knjiga> {

    private Context mContext;
    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public KnjigaAdapter(@NonNull Context context, ArrayList<Knjiga> list) {
        super(context, 0,  list);
        mContext = context;
        knjige = list;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.element_liste_knjiga, parent, false);

        final Knjiga knjiga = knjige.get(position);

        //ImageView image = (ImageView) listItem.findViewById(R.id.eNaslovna);
        //image.setImageBitmap(currentBook.getNaslovna());

        TextView opis = (TextView) listItem.findViewById(R.id.eOpis);
        opis.setText("Kratak opis: " );

        TextView brojStranica = (TextView) listItem.findViewById(R.id.eBrojStranica);
        brojStranica.setText(String.valueOf("Broj stranica: " + knjiga.getBrojStranica()));

        TextView datum = (TextView) listItem.findViewById(R.id.eDatumObjavljivanja);
        datum.setText("Datum objavljivanja: " + knjiga.getDatumObjavljivanja());

        //Treba napraviti da se prikazuje i slika
        ImageView slika = (ImageView)listItem.findViewById(R.id.slika);
        //Picasso.get().load(knjiga.getSlika().toString()).into(slika);
        Picasso.get().load(knjiga.getSlika()).into(slika);

        Button dPrepporuci = (Button)listItem.findViewById(R.id.dPreporuci);

        dPrepporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPreporuci kf = new FragmentPreporuci();
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                Bundle arg = new Bundle();
                ArrayList<Knjiga> book = new ArrayList<>();
                book.add(knjige.get(position));
                arg.putParcelableArrayList("knjige", book);
                kf.setArguments(arg);
                fm.beginTransaction().replace(R.id.mjesto1, kf).addToBackStack(null).commit();
            }
        });

        return listItem;
    }
}
