package com.eventorio.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorio.app.utils.MyTextView;

public class EventsFragment extends Fragment{
	
	private Activity ctx;
	private MyTextView tv_profile_hint;

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
		
		final View rowView = inflater.inflate(R.layout.events_fragment, container, false);
		return rowView;
		
	}

}
