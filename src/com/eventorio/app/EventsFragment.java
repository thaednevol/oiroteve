package com.eventorio.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.eventorio.app.utils.DBAdapter;
import com.eventorio.app.utils.InternetConnection;
import com.eventorio.app.utils.MyTextView;

public class EventsFragment extends Fragment{
	
	private Activity ctx;
	private MyTextView tv_profile_hint;
	private ListView lv_events;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx=getActivity();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		
		tv_profile_hint=(MyTextView)ctx.findViewById(R.id.tv_profile_hint);
		tv_profile_hint.setText("Eventos");
		tv_profile_hint.setTextColor(Color.parseColor("#94c11f"));
		tv_profile_hint.setTextSize(18);
		tv_profile_hint.setTypeface(Typeface.DEFAULT_BOLD);
		
		InternetConnection ic = new InternetConnection(ctx);
		ic.setString_url("http://www.easyandpractical.com/wp/evento.json");
		ic.setCodification("ISO-8859-1");
		Handler puente = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					
				} else {
					showError(msg.obj.toString());
				}
				showEvents(msg.obj.toString());
			}
		};
		ic.setmHandler(puente);
		ic.execute();
		
		final View rowView = inflater.inflate(R.layout.events_fragment, container, false);
		lv_events=(ListView)rowView.findViewById(R.id.lv_events);
		return rowView;
		
	}

	protected void showError(String string) {
		
	}

	protected void showEvents(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			
			if (ja.length()>0){
				
				DBAdapter dbAdapter = new DBAdapter(ctx);
				dbAdapter.open();
				for (int i=0; i<ja.length(); i++){
					JSONObject jo = ja.getJSONObject(i);
					String fecha = jo.getString(DBAdapter.Fecha).split("[\\(\\)]")[1];
					fecha = fecha.replace("\"","\\\"");
					dbAdapter.insertSolicitud(jo.getInt(DBAdapter.Id_Evento), jo.getString(DBAdapter.NomEvento),fecha , jo.getString(DBAdapter.Lugar));
				}
				dbAdapter.close();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DBAdapter dbAdapter = new DBAdapter(ctx);
		dbAdapter.open();
		Cursor c = dbAdapter.getAllSolicitudes(null);
		c.moveToFirst();
		
		EventsAdapter eventsAdapter = new EventsAdapter(ctx, c);
		lv_events.setAdapter(eventsAdapter);
		dbAdapter.close();
		
	}

}
