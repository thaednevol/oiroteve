package com.eventorio.app.utils.extendedcalendarview;

import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventorio.app.R;

@SuppressLint({ "NewApi", "InflateParams" }) 
public class ExtendedCalendarView extends RelativeLayout implements OnItemClickListener,
	OnClickListener{
	
	private Context context;
	private OnDayClickListener dayListener;
	private GridView calendar;
	private CalendarAdapter mAdapter;
	private Calendar cal;
	private TextView month;
	private RelativeLayout base;
	private ImageView next,prev;
	private int gestureType = 0;
	private final GestureDetector calendarGesture = new GestureDetector(context,new GestureListener());
	
	public static final int NO_GESTURE = 0;
	public static final int LEFT_RIGHT_GESTURE = 1;
	public static final int UP_DOWN_GESTURE = 2;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	public interface OnDayClickListener{
		public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day);
	}

	public ExtendedCalendarView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public ExtendedCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public ExtendedCalendarView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	private void init(){
		cal = Calendar.getInstance();
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View rowView = layoutInflater.inflate(R.layout.base_calendar, null, false);
		
		base= (RelativeLayout) rowView.findViewById(R.id.rl_base);
		
		base.setId(4);
		
		prev = (ImageView) base.findViewById(R.id.iv_navigation_previous_item);
		prev.setId(1);
		prev.setOnClickListener(this);
		
		
		month = (TextView) base.findViewById(R.id.tv_month);
		month.setId(2);
		month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
		//month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+cal.get(Calendar.YEAR));
		
		
		next = (ImageView) base.findViewById(R.id.iv_navigation_next_item);
		next.setImageResource(R.drawable.navigation_next_item);
		next.setId(3);
		next.setOnClickListener(this);
		
		mAdapter = new CalendarAdapter(context,cal);
		
		calendar = (GridView)rowView.findViewById(R.id.gv_calendar);
		
		params = (LayoutParams) calendar.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.BELOW, base.getId());
		calendar.setLayoutParams(params);
		
		calendar.setAdapter(mAdapter);
		calendar.setOnTouchListener(new OnTouchListener() {
			@Override
	        public boolean onTouch(View v, MotionEvent event) {
	            return calendarGesture.onTouchEvent(event);
	        }
	    });
		
		
		addView(rowView);

	}

	private class GestureListener extends SimpleOnGestureListener {
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
	    	
	    	if(gestureType == LEFT_RIGHT_GESTURE){
	    		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		            nextMonth();
		            return true; // Right to left
		        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		            previousMonth();
		            return true; // Left to right
		        }
	    	}else if(gestureType == UP_DOWN_GESTURE){
	        	if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
		        	nextMonth();
		            return true; // Bottom to top
		        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
		        	previousMonth();
		            return true; // Top to bottom
		        }
	        }
	        return false;
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(dayListener != null){
			Day d = (Day) mAdapter.getItem(arg2);
			if(d.getDay() != 0){
				dayListener.onDayClicked(arg0, arg1, arg2, arg3,d);
			}
		}
	}
	
	/**
	 * 
	 * @param listener
	 * 
	 * Set a listener for when you press on a day in the month
	 */
	public void setOnDayClickListener(OnDayClickListener listener){
		if(calendar != null){
			dayListener = listener;
			calendar.setOnItemClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case 1:
			previousMonth();
			break;
		case 3:
			nextMonth();
			break;
		default:
			break;
		}
	}
	
	private void previousMonth(){
		if(cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) {				
			cal.set((cal.get(Calendar.YEAR)-1),cal.getActualMaximum(Calendar.MONTH),1);
		} else {
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
		}
		rebuildCalendar();
	}
	
	private void nextMonth(){
		if(cal.get(Calendar.MONTH) == cal.getActualMaximum(Calendar.MONTH)) {				
			cal.set((cal.get(Calendar.YEAR)+1),cal.getActualMinimum(Calendar.MONTH),1);
		} else {
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
		}
		rebuildCalendar();
	}
	
	private void rebuildCalendar(){
		if(month != null){
			month.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+cal.get(Calendar.YEAR));
			refreshCalendar();
		}
	}
	
	/**
	 * Refreshes the month
	 */
	public void refreshCalendar(){
		mAdapter.refreshDays();
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @param color
	 * 
	 * Sets the background color of the month bar
	 */
	public void setMonthTextBackgroundColor(int color){
		base.setBackgroundColor(color);
	}
	
	@SuppressLint("NewApi")
	/**
	 * 
	 * @param drawable
	 * 
	 * Sets the background color of the month bar. Requires at least API level 16
	 */
	public void setMonthTextBackgroundDrawable(Drawable drawable){
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
			base.setBackground(drawable);
		}
		
	}
	
	/**
	 * 
	 * @param resource
	 * 
	 * Sets the background color of the month bar
	 */
	public void setMonehtTextBackgroundResource(int resource){
		base.setBackgroundResource(resource);
	}
	
	/**
	 * 
	 * @param recource
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageResource(int recource){
		prev.setImageResource(recource);
	}
	
	/**
	 * 
	 * @param bitmap
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageBitmap(Bitmap bitmap){
		prev.setImageBitmap(bitmap);
	}
	
	/**
	 * 
	 * @param drawable
	 * 
	 * change the image of the previous month button
	 */
	public void setPreviousMonthButtonImageDrawable(Drawable drawable){
		prev.setImageDrawable(drawable);
	}
	
	/**
	 * 
	 * @param recource
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageResource(int recource){
		next.setImageResource(recource);
	}
	
	/**
	 * 
	 * @param bitmap
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageBitmap(Bitmap bitmap){
		next.setImageBitmap(bitmap);
	}
	
	/**
	 * 
	 * @param drawable
	 * 
	 * change the image of the next month button
	 */
	public void setNextMonthButtonImageDrawable(Drawable drawable){
		next.setImageDrawable(drawable);
	}
	
	/**
	 * 
	 * @param gestureType
	 * 
	 * Allow swiping the calendar left/right or up/down to change the month. 
	 * 
	 * Default value no gesture
	 */
	public void setGesture(int gestureType){
		this.gestureType = gestureType;
	}

}
