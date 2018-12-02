package com.gc.materialdesigndemo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;

import java.util.ArrayList;


public class PretraziKnjigeIzBaze extends Fragment {

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

    public PretraziKnjigeIzBaze()
    {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        container.clearDisappearingChildren();
        boh = new BibliotekaDBOpenHelper(getActivity(), "biblioteka.db", null, 1);
        sdb = boh.getWritableDatabase();
        return inflater.inflate(R.layout.kategorije_fragment, container, false);

    }

}
