package com.eventorio.app;

import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.*;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorio.app.utils.MyProperties;
import com.eventorio.app.utils.MyTextView;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

public class ProfileFragment extends Fragment{
	
	private static final String TAG = MyProperties.TAG;
	private Activity ctx;
	private MyTextView tv_profile_hint;
	private UiLifecycleHelper uiHelper;
	
	// Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.ctx=getActivity();
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		
		tv_profile_hint=(MyTextView)ctx.findViewById(R.id.tv_profile_hint);
		tv_profile_hint.setText(getResources().getString(R.string.profile));
		tv_profile_hint.setTextColor(getResources().getColor(R.color.profile));
		tv_profile_hint.setTextSize(18);
		tv_profile_hint.setTypeface(Typeface.DEFAULT_BOLD);
		
		final View rowView = inflater.inflate(R.layout.profile_fragment, container, false);
		
		LoginButton authButton = (LoginButton) rowView.findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("public_profile", "user_location", "user_birthday", "user_likes"));
		authButton.setFragment(this);
		
		return rowView;
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        
	        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (user != null) {
	                    // Display the parsed user info
	                   // userInfoTextView.setText(buildUserInfoDisplay(user));
	                	
	                	Log.i(TAG,buildUserInfoDisplay(user));
	                }
	            }
	        });
	        
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	    
	    
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	  
	@Override
	public void onResume() {
	    super.onResume();
	    
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	    
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private String buildUserInfoDisplay(GraphUser user) {
	    StringBuilder userInfo = new StringBuilder("");

	    // Example: typed access (name)
	    // - no special permissions required
	    userInfo.append(String.format("Name: %s\n\n", 
	        user.getName()));

	    // Example: typed access (birthday)
	    // - requires user_birthday permission
	    userInfo.append(String.format("Birthday: %s\n\n", 
	        user.getBirthday()));
/*
	    // Example: partially typed access, to location field,
	    // name key (location)
	    // - requires user_location permission
	    userInfo.append(String.format("Location: %s\n\n", 
	        user.getLocation().getProperty("name")));

	    // Example: access via property name (locale)
	    // - no special permissions required
	    userInfo.append(String.format("Locale: %s\n\n", 
	        user.getProperty("locale")));

	    // Example: access via key for array (languages) 
	    // - requires user_likes permission
	    JSONArray languages = (JSONArray)user.getProperty("languages");
	    if (languages.length() > 0) {
	        ArrayList<String> languageNames = new ArrayList<String> ();
	        for (int i=0; i < languages.length(); i++) {
	            JSONObject language;
				try {
					language = languages.getJSONObject(i);
					languageNames.add(language.getString("name"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            // Add the language name to a list. Use JSON
	            // methods to get access to the name field. 
	            
	        }           
	        userInfo.append(String.format("Languages: %s\n\n", 
	        languageNames.toString()));
	    }
*/
	    return userInfo.toString();
	}
	
}
