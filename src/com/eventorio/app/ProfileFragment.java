package com.eventorio.app;

import java.util.ArrayList;
import java.util.Arrays;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
	private SharedPreferences mSharedPreferences;
	private Button btnTwitter;
	private String salida;
	//static String TWITTER_CONSUMER_KEY = "KT3N4qmnDVHe3EOkLxwx8q9gp"; // place your cosumer key here
	

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.ctx=getActivity();
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	    if (!getArguments().getString(MainActivity.DATA,"0").contentEquals("0")){
	    	TwitterData twitterData = new TwitterData();
	    	
	    	Uri myUri = Uri.parse(getArguments().getString(MainActivity.DATA));
	    	twitterData.setUri(myUri);
	    	twitterData.execute();
	    	
	    }
	    
	}
	
	@SuppressLint("NewApi") 
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
		
		btnTwitter = (Button)rowView.findViewById(R.id.btnTwitter);
		btnTwitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginToTwitter();
			}
		});
		
		initTwitter();
		
		return rowView;
		
		
		
	}
	
	protected void loginToTwitter() {
		LoginToTwitter loginToTwitter = new LoginToTwitter();
		
		Handler puente = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.e("WHAT?", "> " + msg.what);
				if (msg.what == 0) {
					getData();
				} else {
					
				}
			}
		};
		loginToTwitter.setPuente(puente);
		loginToTwitter.execute();
	}

	protected void getData() {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
		
		i.putExtra("SALIDA", salida);
		startActivityForResult(i,111);
		//startActivity(i);
		//ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
		
		
		//TwitterData twitterData = new TwitterData();
		//twitterData.execute();
	}

	private void initTwitter() {
		mSharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		
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
		
		Log.d("onActivityReslt called ", requestCode+" "+resultCode);
	    if (data!=null){
	    	Log.d("onActivityReslt called ", data.toString());	
	    }
	    
	    Uri uri = ctx.getIntent().getData();
	    
	    if (uri!=null){
	    	Log.d("onActivityReslt called ", uri.toString());	
	    }
	    
	    if (salida!=null){
	    	Log.d("onActivityReslt called SALIDA", salida);	
	    }
	    
	    /*Log.d("onActivityReslaaaat called ", requestCode+" "+resultCode);
		
		
		
		Log.d("onActivityReslt called ", "URI: "+uri.toString());
		
		
	    if (data!=null){
	    	Log.d("onActivityReslt called ", data.toString());	
	    }*/
		
		
		
	    
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
	
	class LoginToTwitter extends AsyncTask<String, String, String> {

		private Handler puente;
		private Message mensaje;
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mensaje = new Message();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(MyProperties.TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(MyProperties.TWITTER_CONSUMER_SECRET);
				Configuration configuration = builder.build();
				
				TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();
				requestToken = twitter
						.getOAuthRequestToken(MyProperties.TWITTER_CALLBACK_URL);
			} catch (TwitterException e) {
				e.printStackTrace();
				mensaje.what=1;
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			puente.sendMessage(mensaje);
		}

		public Handler getPuente() {
			return puente;
		}

		public void setPuente(Handler puente) {
			this.puente = puente;
		}

		public Message getMensaje() {
			return mensaje;
		}

		public void setMensaje(Message mensaje) {
			this.mensaje = mensaje;
		}

	}
	
	class TwitterData extends AsyncTask<String, String, String> {
		private Uri uri;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
				String verifier = uri
							.getQueryParameter(MyProperties.URL_TWITTER_OAUTH_VERIFIER);

					try {
						// Get the access token
						AccessToken accessToken = twitter.getOAuthAccessToken(
								requestToken, verifier);

						// Shared Preferences
						Editor e = mSharedPreferences.edit();

						// After getting access token, access token secret
						// store them in application preferences
						e.putString(MyProperties.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
						e.putString(MyProperties.PREF_KEY_OAUTH_SECRET,
								accessToken.getTokenSecret());
						// Store login status - true
						e.putBoolean(MyProperties.PREF_KEY_TWITTER_LOGIN, true);
						e.commit(); // save changes

						Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

						// Hide login button
				/*		btnLoginTwitter.setVisibility(View.GONE);

						// Show Update Twitter
						lblUpdate.setVisibility(View.VISIBLE);
						txtUpdate.setVisibility(View.VISIBLE);
						btnUpdateStatus.setVisibility(View.VISIBLE);
						btnLogoutTwitter.setVisibility(View.VISIBLE);
					*/	
						// Getting user details from twitter
						// For now i am getting his name only
						long userID = accessToken.getUserId();
						User user = twitter.showUser(userID);
						String username = user.getName();
						
						Log.d("USERNAME", username);
						
						// Displaying in xml ui
			//			lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
					} catch (Exception e) {
						// Check log for login errors
						Log.e("Twitter Login Error", "> " + e.toString());
					}
			return null;
		}

		public Uri getUri() {
			return uri;
		}

		public void setUri(Uri uri) {
			this.uri = uri;
		}

		protected void onPostExecute(String file_url) {
			
		}

	}

	
}