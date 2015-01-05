package com.eventorio.app.utils.extendedcalendarview;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class MyUtils {

	@SuppressWarnings("deprecation")
	public static int getWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}

}
