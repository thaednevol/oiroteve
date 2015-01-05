package com.eventorio.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class EventsAdapter extends CursorAdapter {

	private Context context;
	private Cursor c;
	private boolean autoRequery;
	private int flags;
	private TextView tv_event_name;
	private TextView tv_event_date;
	private TextView tv_event_place;
	private TextView tv_event_subscriptions_hint;

	public EventsAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context=context;
		this.c=c;
		this.autoRequery=autoRequery;
	}
	
	public EventsAdapter(Context context, Cursor c) {
		super(context, c);
		this.context=context;
		this.c=c;
		
		Toast.makeText(context, c.getString(1), Toast.LENGTH_SHORT).show();
		
	}
	public EventsAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		this.context=context;
		this.c=c;
		this.flags=flags;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		final View view = inflater.inflate(R.layout.event, parent, false);
		return view;
	}

	@SuppressLint("SimpleDateFormat") @Override
	public void bindView(View view, Context context, Cursor cursor) {
		tv_event_name = (TextView)view.findViewById(R.id.tv_event_name);
		tv_event_name.setTypeface(Typeface.DEFAULT_BOLD);
		tv_event_name.setText(cursor.getString(1));
		
		tv_event_date=(TextView)view.findViewById(R.id.tv_event_date);
		DateFormat df = new SimpleDateFormat("dd/MMMM/yyyy  HH:mm");
		String f=cursor.getString(2);
		String fecha = df.format(new Date(Long.parseLong(f)));
		tv_event_date.setText(fecha);
		tv_event_date.setTypeface(Typeface.DEFAULT);
		
		tv_event_place = (TextView)view.findViewById(R.id.tv_event_place);
		tv_event_place.setTypeface(Typeface.DEFAULT);
		tv_event_place.setText(cursor.getString(3));
		
		tv_event_subscriptions_hint=(TextView)view.findViewById(R.id.tv_event_subscriptions_hint);
		tv_event_subscriptions_hint.setTypeface(Typeface.DEFAULT_BOLD);
		
		Log.d("CURSOR", cursor.getCount()+" "+cursor.getString(1));
		
	}
	
	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	}
	

}
