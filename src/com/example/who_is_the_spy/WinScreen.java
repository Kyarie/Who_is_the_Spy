package com.example.who_is_the_spy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class WinScreen extends Activity implements OnClickListener {
	
	int num_of_players = 0;
	String[] name_list = new String[20];
	String[] spy_list = new String[3];
	String[] blank_list = new String[3];
	String category = null; 
	String word_civilian;
	boolean blank_card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_screen);
		
		Bundle b = this.getIntent().getExtras();
		spy_list = b.getStringArray("WordList");
		word_civilian = b.getString("word_civilian");
		num_of_players = b.getInt("numberofplayers");
	    name_list = b.getStringArray("names");
	    category = b.getString("category");
		blank_card = b.getBoolean("BlankCard");
		if (blank_card){
			blank_list = b.getStringArray("BlankList");
		}
	    
		LinearLayout winscreen_root = (LinearLayout)findViewById(R.id.winscreen_root);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String user_pref = sp.getString("background_preference","Simple");
		
		TextView win_winner = (TextView)this.findViewById(R.id.win_winner);
		TextView win_word_spy = (TextView)this.findViewById(R.id.win_word_spy);
		TextView win_word_civilian = (TextView)this.findViewById(R.id.win_word_civilian);
		ImageView winner_announce = (ImageView)this.findViewById(R.id.winner_announce);
		
		Preference_Colour_Change (win_winner);
		Preference_Colour_Change (win_word_civilian);
		Preference_Colour_Change (win_word_spy);		
		
		if (blank_card == false) {
			if (spy_list[2].equals("y")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_spy_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_spy_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_spy_2);
		        }				
				win_winner.setText("Winner Spy: " + spy_list[0]);
			}
			else if (spy_list[2].equals("n")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_civilian_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_civilian_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_civilian_2);
		        }
				win_winner.setText("Winner civilian, Spy: " + spy_list[0]);
			}
		}
		else { 
			TextView blank_holder = (TextView)this.findViewById(R.id.blank_holder);
			Preference_Colour_Change (blank_holder);
			if (blank_list[2].equals("n") && spy_list[2].equals("n")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_civilian_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_civilian_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_civilian_2);
		        }	
				win_winner.setText("Winner civilian, Spy: " + spy_list[0]);
				blank_holder.setText("Blank Card Holder: " + blank_list[0]);
			}
			else if (blank_list[2].equals("y") && spy_list[2].equals("n")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_blank_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_blank_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_blank_2);
		        }
				win_winner.setText("Winner blank card holder, Blank Card Holder: " + blank_list[0]);
				blank_holder.setText("Spy: " + spy_list[0]);
			}
			else if (blank_list[2].equals("n") && spy_list[2].equals("y")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_spy_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_civilian_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_spy_2);
		        }
				win_winner.setText("Winner Spy: " + spy_list[0]);
				blank_holder.setText("Blank Card Holder: " + blank_list[0]);
			}
			else if (blank_list[2].equals("y") && spy_list[2].equals("y")){
				if (user_pref.equals("Simple")){
					winscreen_root.setBackgroundResource(R.drawable.background_1);
					winner_announce.setImageResource(R.drawable.winner_spy_1);
					ImageView winner_announce2 = (ImageView)this.findViewById(R.id.winner_announce2);
					winner_announce2.setImageResource(R.drawable.winner_blank_1);
		        }
		        else if (user_pref.equals("Night")){
		        	winscreen_root.setBackgroundResource(R.drawable.background_2);
					winner_announce.setImageResource(R.drawable.winner_spy_1);
					ImageView winner_announce2 = (ImageView)this.findViewById(R.id.winner_announce2);
					winner_announce2.setImageResource(R.drawable.winner_blank_1);
		        	//winscreen_root.setBackgroundResource(R.drawable.winner_spy_blank_2);
		        }
				win_winner.setText("Winner Spy: " + spy_list[0]);
				blank_holder.setText("Winner blank card holder, Blank Card Holder: " + blank_list[0]);
			}
		}
		
		win_word_spy.setText("Spy Word: " + spy_list[1]);
		win_word_civilian.setText("civilian Word: " + word_civilian);
		
		//Button
		Button HomeButton =(Button)this.findViewById(R.id.home_button);
		HomeButton.setOnClickListener(this);
		Button ContinueButton =(Button)this.findViewById(R.id.continue_button);
		ContinueButton.setOnClickListener(this);	
	}
	
	public void Preference_Colour_Change (TextView element) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Rochester-Regular.ttf");
    	element.setTypeface(font);
    	if (user_pref.equals("Simple")){
    		element.setTextColor(Color.BLACK);
    	}
    	else if (user_pref.equals("Night")){
    	}
	}
	
	@Override
	public void onClick(View thisView) {
		switch (thisView.getId()) {
			case R.id.home_button:
				Intent Homescreen = new Intent(this, MainMenu.class);
				startActivity(Homescreen);
				//background_zero();				
				finish();
				break;
			case R.id.continue_button:
				//Passing data to GameScreen Activity				
				
				Bundle b=new Bundle();
				b.putStringArray("names", name_list);
				b.putInt("numberofplayers", num_of_players);
				b.putString("category", category);
				b.putBoolean("BlankCard", blank_card);
				
				Intent Continue = new Intent(this, GameScreen.class);
				Continue.putExtras(b);
				startActivity(Continue);
				//background_zero();
				finish();
				break;
		}
	}
	
	public void background_zero() {
		LinearLayout winscreen_root = (LinearLayout)findViewById(R.id.winscreen_root);			
		winscreen_root.setBackgroundResource(0);
		ImageView winner_announce2 = (ImageView)this.findViewById(R.id.winner_announce2);
		//winner_announce2.setVisibility(View.GONE);
		winner_announce2.setImageResource(0);
	}
	
	private void menu_popup(final Activity context) {	
		//LinearLayout gamescreen_layout = (LinearLayout)context.findViewById(R.id.gamescreen_root_layout);		
		
		// Inflate the popup_layout.xml
		RelativeLayout menu_popup_layout = (RelativeLayout)context.findViewById(R.id.menu_layout);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popup_layout = layoutInflater.inflate(R.layout.menu_popup, menu_popup_layout);
				
		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(popup_layout);
		popup.setWidth(LayoutParams.FILL_PARENT);
		popup.setHeight(LayoutParams.FILL_PARENT);
		popup.setFocusable(true);
		 
		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());
		 
		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(popup_layout, Gravity.TOP, 0, 0);
		
		Button menu_resume =(Button)popup_layout.findViewById(R.id.menu_resume);
		Button menu_main_menu = (Button)popup_layout.findViewById(R.id.menu_main_menu);
		Button menu_how_to_play = (Button)popup_layout.findViewById(R.id.menu_how_to_play);
		Button menu_options = (Button)popup_layout.findViewById(R.id.menu_options);
		Button menu_about = (Button)popup_layout.findViewById(R.id.menu_about);
				
		// Getting a reference to Close button, and close the popup when clicked.
		menu_resume.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				popup.dismiss();
		   }
		   });
		
		menu_main_menu.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				Intent Homescreen = new Intent(context, MainMenu.class);
    			startActivity(Homescreen);
		   }
		   });
		
		menu_how_to_play.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				Intent howtoplay = new Intent(context, HowToPlay.class);
    			startActivity(howtoplay);
		   }
		   });
		
		menu_options.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				Intent SettingsMenue = new Intent(context, PreferenceSettings.class);
    			startActivity(SettingsMenue);
		   }
		   });
		
		menu_about.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				Intent AboutMenu = new Intent(context, About.class);
				startActivity(AboutMenu);
		   }
		   });
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        menu_popup(WinScreen.this);
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
	}
}
