package com.eventorio.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;
import com.tyczj.extendedcalendarview.ExtendedCalendarView.OnDayClickListener;

@SuppressLint({ "NewApi", "ResourceAsColor", "SimpleDateFormat" })
public class CalendarFragment extends Fragment{
protected static final String ACCOUNTS = "accounts";
	
	private Activity act;
	private FrameLayout content_frame;
	private FragmentManager fragmentManager;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.act=getActivity();
        
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		
		final View rowView = inflater.inflate(R.layout.calendar_fragment, null, false);
		
		ExtendedCalendarView calendar = (ExtendedCalendarView)rowView.findViewById(R.id.calendarView1);
		

		    ContentValues values = new ContentValues();
		    values.put(CalendarProvider.COLOR, Event.COLOR_RED);
		    values.put(CalendarProvider.DESCRIPTION, "Some Description");
		    values.put(CalendarProvider.LOCATION, "Some location");
		    values.put(CalendarProvider.EVENT, "Event name");

		    Calendar cal = Calendar.getInstance();
		    TimeZone tz = TimeZone.getDefault();
		    cal.set(2014, 10, 8, 10, 00);
		    int StartDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
		    values.put(CalendarProvider.START, cal.getTimeInMillis());
		    values.put(CalendarProvider.START_DAY, StartDayJulian);

		    cal.set(2014, 10, 8, 11, 00);
		    int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

		    values.put(CalendarProvider.END, cal.getTimeInMillis());
		    values.put(CalendarProvider.END_DAY, endDayJulian);
		    
		    calendar.setOnDayClickListener(new OnDayClickListener() {
		        @Override
		        public void onDayClicked(AdapterView<?> adapter, View view,
		                int position, long id, Day day) {
		        			getScheduleDetails(day);

		                }

		            });
		    
		 //  act.getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
		
/*		CalendarView calendarView1 = (CalendarView) rowView.findViewById(R.id.calendarView1);
		Date now = new Date();
		
		String date = "8/11/2014";
	    String parts[] = date.split("/");

	    int day = Integer.parseInt(parts[0]);
	    int month = Integer.parseInt(parts[1]);
	    int year = Integer.parseInt(parts[2]);

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.MONTH, month);
	    calendar.set(Calendar.DAY_OF_MONTH, day);

	    long milliTime = calendar.getTimeInMillis();
		 
		calendarView1.setDate(milliTime);
		getInstances(calendarView1);*/
		return rowView;
	}

	protected void getScheduleDetails(Day day) {
		// TODO Auto-generated method stub
		
	}

	public void getInstances(ViewGroup calendarView1) {
		Date now = new Date();
		String format2 = new SimpleDateFormat("MMMM yyyy").format(now);

		for (int i=0; i<calendarView1.getChildCount(); i++){
			Log.d("CALENDAR", calendarView1.getChildAt(i).toString()+" "+format2);
			
			if (calendarView1.getChildAt(i) instanceof TextView){
				TextView tv = (TextView) calendarView1.getChildAt(i);
				
				if (tv.getText().toString().contentEquals(format2)){
					tv.setBackgroundResource(0);
					tv.setTextColor(R.color.white);
					tv.setGravity(Gravity.CENTER);
					LinearLayout.LayoutParams llp = (LayoutParams) tv.getLayoutParams();
					
					llp.width= LinearLayout.LayoutParams.MATCH_PARENT;
					tv.setBackgroundResource(R.color.blue_calendar);
				}
				else {
					tv.setBackgroundResource(0);
					tv.setTextColor(Color.parseColor("#000000"));
				}
				

				
			}
			
			if (calendarView1.getChildAt(i) instanceof ViewGroup){
				getInstances((ViewGroup) calendarView1.getChildAt(i));
			}
			
			if (calendarView1.getChildAt(i) instanceof ListView){
				getInstances((ListView)calendarView1.getChildAt(i));
			}
			
		}
	}
	
	public void getInstances(ListView listView) {
		int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
		
		for (int i=0; i<listView.getHeaderViewsCount(); i++){
			View wantedView = listView.getChildAt(i);
			Log.d("CALENDAR_LISTVIEW", listView.getChildAt(i).toString()+" "+wantedView+" "+firstPosition);
		}
		
		
	}
}