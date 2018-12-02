package com.gc.materialdesigndemo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;


public class DodajKnjiguOnlineFragment extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone​{

	ArrayList<Knjiga> knjige = new ArrayList<>();
	int backgroundColor = Color.parseColor("#1E88E5");
	private BibliotekaDBOpenHelper boh;
	private SQLiteDatabase sdb;
	String[] koloneRezulat;
	String where = null;
	//Definišemo argumente u where upitu, group by, having i order po potrebi
	String whereArgs[] = null;
	String groupBy = null;
	String having = null;
	String order = null;

	public DodajKnjiguOnlineFragment()
	{

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		final Button dugme = (Button) getView().findViewById(R.id.dSearchBooks);
		final EditText upit = (EditText) getView().findViewById(R.id.upit);

		dugme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone​) DodajKnjiguOnlineFragment.this).execute(upit.getText().toString());
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		container.removeAllViews();
		container.clearDisappearingChildren();
		boh = new BibliotekaDBOpenHelper(getActivity(), "biblioteka.db", null, 1);
		sdb = boh.getWritableDatabase();
		return inflater.inflate(R.layout.dodaj_knjigu_online_fragment, container, false);

	}

	@Override
	public void onDohvatiDone(ArrayList<Knjiga> k) {
		knjige.clear();
		knjige.addAll(k);
		ListView listaKnjiga = (ListView) getView().findViewById(R.id.lista);
		DodajKnjiguAdapter adapter = new DodajKnjiguAdapter(getActivity(), knjige);
		listaKnjiga.setAdapter(adapter);
	}
}
