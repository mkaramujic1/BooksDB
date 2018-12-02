package com.gc.materialdesigndemo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OpisKnjigeAdapter extends ArrayAdapter<Knjiga> {


    private Context mContext;
    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public OpisKnjigeAdapter(@NonNull Context context, ArrayList<Knjiga> list) {
        super(context, 0,  list);
        mContext = context;
        knjige = list;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.opis_knjige, parent, false);

        final Knjiga knjiga = knjige.get(position);

        //ImageView image = (ImageView) listItem.findViewById(R.id.eNaslovna);
        //image.setImageBitmap(currentBook.getNaslovna());

        TextView naziv = (TextView) listItem.findViewById(R.id.naziv);
        naziv.setText("Naziv: "+ knjiga.getNaziv() );

        TextView brojStranica = (TextView) listItem.findViewById(R.id.brojStr);
        brojStranica.setText(String.valueOf("Broj stranica: " + knjiga.getBrojStranica()));

        TextView datum = (TextView) listItem.findViewById(R.id.datum);
        datum.setText("Datum objavljivanja: " + knjiga.getDatumObjavljivanja());

        TextView id = (TextView) listItem.findViewById(R.id.id);
        id.setText("ID: " + knjiga.getId());

        TextView opis = (TextView) listItem.findViewById(R.id.opis);
        opis.setText("Kratak opis: " + knjiga.getOpis());

        String autori = " ";
        for(int i =0; i<knjiga.getAutori().size();i++)
            autori+= knjiga.getAutori().get(i)+"; ";
        TextView a = (TextView) listItem.findViewById(R.id.autori);
        a.setText("Autori:" + autori);

        ImageView slika = (ImageView)listItem.findViewById(R.id.slika);
        Picasso.get().load(knjiga.getSlika().toString()).into(slika);

        return listItem;
    }
}
