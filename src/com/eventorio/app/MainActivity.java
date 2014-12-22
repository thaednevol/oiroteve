package com.eventorio.app;

import java.util.ArrayList;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	
	private ArrayList<String> specs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponents();
	}

	private void initComponents() {
		String[] aLabels = getResources().getStringArray(R.array.menu);
		
		specs = new ArrayList<String>();
    	
    	for (int i=0; i<aLabels.length; i++){
    		specs.add(aLabels[i]);
    	}
    	
    	FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		mTabHost.newTabSpec(specs.get(0));
		mTabHost.addTab(mTabHost.newTabSpec(specs.get(0)).setIndicator(specs.get(0)), HomeFragment.class, null);
		
		mTabHost.newTabSpec(specs.get(1));
		mTabHost.addTab(mTabHost.newTabSpec(specs.get(1)).setIndicator(specs.get(1)), ProfileFragment.class, null);
		
		mTabHost.newTabSpec(specs.get(2));
		mTabHost.addTab(mTabHost.newTabSpec(specs.get(2)).setIndicator(specs.get(2)), EventsFragment.class, null);
		
		mTabHost.newTabSpec(specs.get(3));
		mTabHost.addTab(mTabHost.newTabSpec(specs.get(3)).setIndicator(specs.get(3)), CalendarFragment.class, null);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
