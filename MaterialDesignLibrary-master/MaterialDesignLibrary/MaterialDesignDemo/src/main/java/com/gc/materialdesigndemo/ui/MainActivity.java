package com.gc.materialdesigndemo.ui;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesigndemo.R;
import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends Activity {

	int backgroundColor = Color.parseColor("#1E88E5");
	ButtonFloatSmall buttonSelectColor;
	private BibliotekaDBOpenHelper boh;
	private SQLiteDatabase sdb;
	public static Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		boh = new BibliotekaDBOpenHelper(this, "biblioteka.db", null, 1);
		sdb = boh.getWritableDatabase();
		mContext = getApplicationContext();
		LayoutRipple layoutRipple = (LayoutRipple) findViewById(R.id.itemProgress);
		setOriginRiple(layoutRipple);

		////////////////////////////////////////////////////////////////////////////////////////////
		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				KategorijeFragment fd = new KategorijeFragment();
				getFragmentManager().beginTransaction().replace(R.id.mjesto1, fd).addToBackStack(null).commit();

			}
		});
		layoutRipple = (LayoutRipple) findViewById(R.id.itemWidgets);
		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AutoriFragment af = new AutoriFragment();
				getFragmentManager().beginTransaction().replace(R.id.mjesto1, af).addToBackStack(null).commit();
			}
		});

		layoutRipple = (LayoutRipple) findViewById(R.id.itemSwitches);
		setOriginRiple(layoutRipple);
		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DodajKnjiguOnlineFragment af = new DodajKnjiguOnlineFragment();
				getFragmentManager().beginTransaction().replace(R.id.mjesto1, af).addToBackStack(null).commit();
			}
		});
	}

	private void setOriginRiple(final LayoutRipple layoutRipple){

		layoutRipple.post(new Runnable() {

			@Override
			public void run() {
				View v = layoutRipple.getChildAt(0);
				layoutRipple.setxRippleOrigin(ViewHelper.getX(v)+v.getWidth()/2);
				layoutRipple.setyRippleOrigin(ViewHelper.getY(v)+v.getHeight()/2);

				layoutRipple.setRippleColor(Color.parseColor("#1E88E5"));

				layoutRipple.setRippleSpeed(30);
			}
		});

	}

	public static Context getContext() { return mContext; }


}
