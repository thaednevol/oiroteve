package com.eventorio.app;

import java.util.ArrayList;

import com.eventorio.app.utils.MyTextView;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class MainActivity extends ActionBarActivity {

	
	private ArrayList<String> specs;
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initComponents();
	}

	private void initComponents() {
		String[] aLabels = getResources().getStringArray(R.array.menu);
		
		specs = new ArrayList<String>();
    	
    	for (int i=0; i<aLabels.length; i++){
    		specs.add(aLabels[i]);
    	}
    	
    	mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		
		
		TabSpec tabSpec = getTabspec(mTabHost.getContext(), specs.get(0), R.layout.home_selector);
		mTabHost.addTab(tabSpec,  HomeFragment.class, null);
		
		tabSpec = getTabspec(mTabHost.getContext(), specs.get(1), R.layout.profile_selector);
		mTabHost.addTab(tabSpec,  ProfileFragment.class, null);
		
		tabSpec = getTabspec(mTabHost.getContext(), specs.get(2), R.layout.events_selector);
		mTabHost.addTab(tabSpec, EventsFragment.class, null);
		
		tabSpec = getTabspec(mTabHost.getContext(), specs.get(3), R.layout.calendar_selector);
		mTabHost.addTab(tabSpec, CalendarFragment.class, null);
	}

	private TabSpec getTabspec(Context context, String string, int homeSelector) {
		View tabview = createTabView(context, string, homeSelector);
		
		TabSpec setContent = mTabHost.newTabSpec(string).setIndicator(tabview).setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				MyTextView tv = new MyTextView(MainActivity.this);
				return tv;}
		});
		return setContent;
	}

	private static View createTabView(final Context context, final String text, int layout) {
		View view = LayoutInflater.from(context).inflate(layout, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
