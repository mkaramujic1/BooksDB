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

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;

public class AutoriFragment extends Fragment{

	ArrayList<String> autori = new ArrayList();
	public AutoriFragment()
	{

	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		final ListView listaAutora = (ListView) getView().findViewById(R.id.listaAutora);
		final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, autori);
		////////////////////////////////////////////////////////////////////////////////////////////
		BibliotekaDBOpenHelper boh;
		SQLiteDatabase sdb;
		String where = null;
		//Definišemo argumente u where upitu, group by, having i order po potrebi
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = null;
		String[] koloneRezulat = new String[]{"_id, ime"};

		boh = new BibliotekaDBOpenHelper(getActivity(), "biblioteka.db", null, 1);
		sdb = boh.getWritableDatabase();
		autori.clear();

		Cursor cursor = sdb.query("Autor", koloneRezulat, where, whereArgs, groupBy, having,
				order);

		int INDEX_KOLONE_ID = cursor.getColumnIndexOrThrow("_id");
		int INDEX_KOLONE_IME = cursor.getColumnIndexOrThrow("ime");
		while(cursor.moveToNext())
			autori.add(cursor.getString(INDEX_KOLONE_IME));

		listaAutora.setAdapter(adapterKategorije);

		listaAutora.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				ListaKnjigaPoAutorima f = new ListaKnjigaPoAutorima();
				Bundle bundle = new Bundle();
				String autor = (String) listaAutora.getItemAtPosition(position);
				bundle.putString("nazivAutora", autor);
				f.setArguments(bundle);
				FragmentManager fm = getFragmentManager();
				fm.beginTransaction().replace(R.id.mjesto1, f).addToBackStack(null).commit();
			}});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		container.removeAllViews();
		container.clearDisappearingChildren();
		return inflater.inflate(R.layout.autori_fragment, container, false);

	}

}
