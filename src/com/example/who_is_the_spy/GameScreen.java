package com.example.who_is_the_spy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button; 
import android.widget.Toast;
import android.view.View.OnClickListener;

public class GameScreen extends Activity implements OnClickListener, OnLongClickListener{
	
	//List of Variables
	//private TextView word_popup = null;
	Button[] names_button = new Button[20];
	int num_of_players = 0;
	int num_of_players_new = 0;
	String[] name_list = new String[20];
	String category = null; 
	char status = 's';
	String[][] word_list = new String[20][3];
	String[] word_pair = new String[2];
	String word_spy = null ;
	String word_civilian = null;	
	int num_spy;
	int num_blank;
	TextView instructions;
	Point p;
	boolean blank_card = false;
	boolean forgot = false;
	int forgot_num = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		
		//Get passed data	    
		Bundle b = this.getIntent().getExtras();
		num_of_players = b.getInt("numberofplayers");
	    name_list = b.getStringArray("names");
	    category = b.getString("category");
	    blank_card = b.getBoolean("BlankCard");
    	
	    //Settings
        User_Preferences ();
        TextView instructions = (TextView)this.findViewById(R.id.instructions);
        Preference_Colour_Change (instructions, "none");    	
	    
	    RelativeLayout names_layout = (RelativeLayout)findViewById(R.id.names_layout);	    
	    RelativeLayout.LayoutParams[] names_param = new RelativeLayout.LayoutParams[num_of_players];
	    
	    //LinearLayout gamescreen_root = (LinearLayout)this.findViewById(R.id.gamescreen_root);
	    //int parent_width = (int) Math.floor(gamescreen_root.getWidth() * 0.8);	    
	    
	    //Create Names Buttons	    	    
	    for (int i = 0; i < num_of_players; i++) {
	    	names_button[i] = new Button(this);
	    	names_button[i].setText(name_list[i]);
	    	names_button[i].setId(i+1);
	    	names_param[i]=new RelativeLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    }        
	    
	    names_layout.addView(names_button[0], names_param[0]);
	    
	    for (int i = 1; i < num_of_players; i++) {
	    	names_param[i].addRule(RelativeLayout.BELOW, names_button[i-1].getId());
	    	names_layout.addView(names_button[i], names_param[i]);
	    }	    
	    
	    //Retrieving words
	    int category_num = 0;
	    if (category.equals("Random")){
	    	category_num = 1 + (int)(Math.random() * 4);	    	
	    }
	    	    
	    Resources res = getResources();
	    if ((category.equals("School")) || (category_num == 1)){
	    	String[] school_words = res.getStringArray(R.array.school_words);
	    	int arr_length = school_words.length;
		    int word_index = (int)(Math.random() * arr_length);
		    word_pair = school_words[word_index].split(",");
	    }
	    else if ((category.equals("Entertainment")) || (category_num == 2)){
	    	String[] entertainment_words = res.getStringArray(R.array.entertainment_words);
	    	int arr_length = entertainment_words.length;
		    int word_index = (int)(Math.random() * arr_length);
		    word_pair = entertainment_words[word_index].split(",");
	    }
		else if ((category.equals("Science")) || (category_num == 3)){
			String[] science_words = res.getStringArray(R.array.science_words);
	    	int arr_length = science_words.length;
		    int word_index = (int)(Math.random() * arr_length);
		    word_pair = science_words[word_index].split(",");    	
		}
		else if ((category.equals("Objects")) || (category_num == 4)){
			String[] objects_words = res.getStringArray(R.array.objects_words);
	    	int arr_length = objects_words.length;
		    int word_index = (int)(Math.random() * arr_length);
		    word_pair = objects_words[word_index].split(",");
		}
	    
	    //Setting up words
	    int randomnum = (int)(Math.random() * 2);
	    word_spy = word_pair[randomnum];
	    if (word_spy == word_pair[0]){
	    	word_civilian = word_pair[1];
	    }
	    else if (word_spy == word_pair[1]){
	    	word_civilian = word_pair[0];
	    }
	    
	    randomnum = (int)(Math.random() * num_of_players);
	    word_list[randomnum][1] = word_spy;
	    num_spy = randomnum;
	    
	    if (blank_card == true){
	    	 while (true){
	    		 randomnum = (int)(Math.random() * num_of_players);
	    		 if (randomnum != num_spy){
	    			 break;
	    		 }
	    	 }
	    	 word_list[randomnum][1] = "blank";
	    	 num_blank = randomnum;
	    }
	    
	    num_of_players_new = num_of_players;
	    
	    //buttons        
        Button CancelButton=(Button)this.findViewById(R.id.game_cancel);
        CancelButton.setOnClickListener(this); 
        Button RestartButton=(Button)this.findViewById(R.id.restart_button);
        RestartButton.setOnClickListener(this);
        Button ForgotButton=(Button)this.findViewById(R.id.forgot_button);
        ForgotButton.setOnClickListener(this);    
        Button ConfirmButton=(Button)this.findViewById(R.id.confirm_button);
        ConfirmButton.setOnClickListener(this);
        for (int i=0; i<num_of_players; i++){
	        names_button[i].setOnClickListener(this);
	        names_button[i].setOnLongClickListener(this);
        }
	}
	
	public void User_Preferences () {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	LinearLayout main_layout = (LinearLayout)this.findViewById(R.id.gamescreen_root);    	
    	
        if (user_pref.equals("Simple")){
        	main_layout.setBackgroundResource(R.drawable.background_1);   	
        }
        else if (user_pref.equals("Night")){
        	main_layout.setBackgroundResource(R.drawable.background_2);
        }
    }
	
	public void Preference_Colour_Change (TextView element, String condition) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");		
    	if (user_pref.equals("Simple") && condition.equals("none")){
    		element.setTextColor(Color.BLACK);
    		element.setPadding(0, 100, 0, 10);
    	}
    	else if (user_pref.equals("Simple") && condition.equals("popup")) {
    		element.setTextColor(Color.BLACK);
    	}
    	else if (user_pref.equals("Night")){
    		element.setTextColor(Color.WHITE);
    	}
	}
	
	@Override
	public void onClick(View thisView) {
		switch (thisView.getId()) {    		
			case R.id.confirm_button:
				boolean check = true;
				for (int i = 0; i < num_of_players; i++){			
					if (word_list[i][0] == null) {
						check = false;
						break;
					}
				}
				if (check == false){
					invalid_alert("All players must have viewed their word before continuing.");
					break;
				}
				
				status = 'g';
				View confirm_button = findViewById(R.id.confirm_button);
				confirm_button.setVisibility(View.GONE);
				instructions = (TextView)this.findViewById(R.id.instructions);
				int randomnum = (int)(Math.random() * num_of_players);
				forgot_num = randomnum;
				if (blank_card == false){
					instructions.setText("Starting with " + word_list[randomnum][0] + ", please describe your word..." + 
											"\n" + "Press and hold the name of the predicted spy");					
				}
				else {
					instructions.setText("Starting with " + word_list[randomnum][0] + ", please describe your word..." + 
							"\n" + "Press and hold the name of the predicted spy or the blank card holder");
				}
				break;
			case R.id.game_cancel:
    			Intent Homescreen = new Intent(this, MainMenu.class);
    			startActivity(Homescreen);
    			break;
			case R.id.restart_button:
				Bundle b=new Bundle();
				b.putStringArray("names", name_list);
				b.putInt("numberofplayers", num_of_players);
				b.putString("category", category);
				b.putBoolean("BlankCard", blank_card);
				
			    Intent startGame = new Intent(getApplicationContext(), GameScreen.class);	    			    
			    startGame.putExtras(b);
    			startActivity(startGame);
    			break;
			case R.id.forgot_button:
				instructions = (TextView)this.findViewById(R.id.instructions);
				instructions.setText("Click your name to check the word again");
				forgot = true;
		}
		for (int i = 0; i < num_of_players ; i++){
	    	if (thisView == names_button[i]){
	    		if ((status == 's' && word_list[i][0] == null) || forgot == true){
	    			word_list[i][0] = name_list[i];	    			
	    			word_list[i][1] = player_word(i);
	    			word_list[i][2] = "y";
	    			
	    			int[] location = new int[2];
	    			RelativeLayout gamescreen_relative = (RelativeLayout)this.findViewById(R.id.gamescreen_relative);
	    			gamescreen_relative.getLocationOnScreen(location);
	    			p = new Point();
	    			p.x = location[0];
	    			p.y = location[1];
	    			showPopup(GameScreen.this, p, word_list[i][1]);
	    			if (forgot == true){
	    				if (status == 's'){
	    					instructions.setText("Click on your name and remember your word");
	    				}
	    				else if (status == 'g'){
	    					if (blank_card == false){
	    						instructions.setText("Starting with " + word_list[forgot_num][0] + ", please describe your word..." + 
	    												"\n" + "Press and hold the name of the predicted spy");
	    					}
	    					else {
	    						instructions.setText("Starting with " + word_list[forgot_num][0] + ", please describe your word..." + 
	    								"\n" + "Press and hold the name of the predicted spy or the blank card holder");
	    					}
	    				}
	    				forgot=false;
	    			}
	    		}
	    		else if (status == 's' && word_list[i][0] != null){
	    			invalid_alert("You have checked the word already, if you have forgot the word, please press forgot");
	    		}
	    		else if (status == 'g'){
	    			break;
	    		}	    		
	    		break;
	    	}
    	}		
    }
	
	public String player_word (int i){
		if (word_list[i][1] == word_spy){
			return word_spy;
		}		
		else if (word_list[i][1] == null)
		{
			return word_civilian;
		}
		else {
			String temp = word_list[i][1];
			return temp;
		}
	}
	
	private void showPopup(final Activity context, Point p, String word_popup_string) {	
		int popupWidth;
		int popupHeight;		   
		
		RelativeLayout gamescreen_relative = (RelativeLayout)context.findViewById(R.id.gamescreen_relative);
		popupWidth = (int)Math.round(gamescreen_relative.getWidth()* 0.8);
		popupHeight = (int)Math.round(gamescreen_relative.getHeight() * 0.5);
		
		// Inflate the popup_layout.xml
		LinearLayout popup_linearlayout = (LinearLayout)context.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popup_layout = layoutInflater.inflate(R.layout.game_screen_popup, popup_linearlayout);
				
		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(popup_layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		 
		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());
		 
		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(gamescreen_relative, Gravity.TOP, 0, 50);
				
		TextView word_popup = (TextView)popup_layout.findViewById(R.id.word_show);		
		word_popup.setText(word_popup_string);
		word_popup.setFocusable(true);
		Preference_Colour_Change (word_popup, "popup");
		
		TextView popup_continue = (TextView)popup_layout.findViewById(R.id.popup_continue);    	
    	Preference_Colour_Change (popup_continue, "none");
		
		TextView instructions = (TextView)context.findViewById(R.id.instructions);
		instructions.setVisibility(View.GONE);
		
		// Getting a reference to Close button, and close the popup when clicked.
		word_popup.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
				popup.dismiss();				
		   }
		   });
		popup.setOnDismissListener(new OnDismissListener() {
		    @Override
		    public void onDismiss() {    					    	
		    	TextView instructions = (TextView)context.findViewById(R.id.instructions);	
		    	instructions.setVisibility(View.VISIBLE);			
		    }
		});
	}	
		
	@Override
	public boolean onLongClick (View thisView) {
		if (status == 'g' || status == 'b' || status == 'c' ){
			for (int i = 0; i < num_of_players ; i++){
				if (thisView == names_button[i] && word_list[i][2].equals("y")){	    		
					word_list[i][2] = "n";
					if (word_list[i][1] == word_civilian){
	    				int check = 0;
	    				for (int j = 0; j < num_of_players ; j++){
	    					if (word_list[j][2] == "y"){
	    						check++;
	    					}
	    				}
	    				if (check <3 ){
	    					win_bundle();
		        			break;
	    				}
	    				else {
	    					names_button[i].setPaintFlags(names_button[i].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    					out_display(GameScreen.this, "civilian", i);
	    	    			
	    					instructions.setText(word_list[i][0] + " is a civilian");
	    				}
	    			}
	    			else if (word_list[i][1] == word_spy && blank_card == false){	    				
	    				win_bundle();
	        			break;
	    			}
	    			else if (word_list[i][1].equals("blank")) {
	    				if (word_list[num_spy][2].equals("n")) {
	    					win_bundle();
		        			break;
	    				}
	    				int check = 0;
	    				for (int j = 0; j < num_of_players ; j++){
	    					if (word_list[j][2] == "y"){
	    						check++;
	    					}
	    				}
	    				if (check <3 ){
	    					win_bundle();
		        			break;
	    				}
	    				else {
	    					//names_button[i].setText(word_list[i][0] + " b");
	    					out_display(GameScreen.this, "blank", i);
	    					instructions.setText("Congratulations! " + word_list[i][0] + " is the blank card holder");
	    					status = 'c';
	    				} 				    				
	    			}
	    			else if (word_list[i][1] == word_spy && blank_card == true){
	    				if (word_list[num_blank][2].equals("n")) {
	    					win_bundle();
		        			break;
	    				}
	    				int check = 0;
	    				for (int j = 0; j < num_of_players ; j++){
	    					if (word_list[j][2] == "y"){
	    						check++;
	    					}
	    				}
	    				if (check <3 ){
	    					win_bundle();
		        			break;
	    				}
	    				else {
	    					//names_button[i].setText(word_list[i][0] + " spy");
	    					out_display(GameScreen.this, "spy", i);
	    					instructions.setText("Congratulations! " + word_list[i][0] + " is the spy");
	    					status = 'b';
	    				}
	    			}	    			
    				Handler handler = new Handler(); 
    			    handler.postDelayed(new Runnable() { 
    			         public void run() {
    			        	num_of_players_new--;
    			        	String[] name_list_new = new String[num_of_players_new];
    			        	int k=0;
    			        	for (int j=0; j<num_of_players; j++){
    			        		if (word_list[j][2].equals("y")){
    			        			name_list_new[k]=word_list[j][0];
    			        			k++;
    			        		}
    			        	}
    			        	
    			        	int randomnum = (int)(Math.random() * num_of_players_new);
    			        	forgot_num = randomnum;
 							if (blank_card == false){
	    			        	instructions.setText("Starting with " + name_list_new[randomnum] + ", please describe your word..." + 
	 												"\n" + "Press and hold the name of the predicted spy"); 
 							}
 							else if (blank_card == true && status == 'g'){
 								instructions.setText("Starting with " + name_list_new[randomnum] + ", please describe your word..." + 
											"\n" + "Press and hold the name of the predicted spy or the blank card holder"); 
 							}
 							else if (blank_card == true && status == 'b'){
 								instructions.setText("Starting with " + name_list_new[randomnum] + ", please describe your word..." + 
											"\n" + "Press and hold the name of the blank card holder");
 							}
 							else if (blank_card == true && status == 'c'){
 								instructions.setText("Starting with " + name_list_new[randomnum] + ", please describe your word..." + 
											"\n" + "Press and hold the name of the predicted spy");
 							}
    			         } 
    			    }, 3000);
    			    return true;
	    		}	    		
	    	}
		}
		else if (status == 's'){
			return false;
		}
		return false;
	}
	
	private void out_display (final Activity context, String font, int i) {	
/*		int[] location = new int[2];
		names_button[i].getLocationOnScreen(location);
		p = new Point();
		p.x = location[0];
		p.y = location[1];
		
		int height = names_button[i].getHeight();
		
		RelativeLayout outdisplay_linear = (RelativeLayout)findViewById(R.id.names_layout);		         
		RelativeLayout.LayoutParams outdisplay_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
	    outdisplay_param.leftMargin = 0;
	    outdisplay_param.topMargin = p.y-562;	   
	    
	    if (font.equals("civilian")){
	    	ImageView civilian_font = new ImageView(this);
		    civilian_font.setImageResource(R.drawable.civilian_font);
		    outdisplay_linear.addView(civilian_font, outdisplay_param);
	    }
	    else if (font.equals("spy")){
	    	ImageView spy_font = new ImageView(this);
	    	spy_font.setImageResource(R.drawable.spy_font);
		    outdisplay_linear.addView(spy_font, outdisplay_param);
	    }
	    else if (font.equals("blank")){
	    	ImageView blank_font = new ImageView(this);
	    	blank_font.setImageResource(R.drawable.blank_font);
		    outdisplay_linear.addView(blank_font, outdisplay_param);
	    }
	    	    
		View child = getLayoutInflater().inflate(R.layout.game_screen_outdisplay, null);         
		outdisplay_linear.addView(child); */
		
		int height = names_button[i].getHeight();
		height=70;
		
		if (font.equals("civilian")){			
			Drawable civilian_font = this.getResources().getDrawable( R.drawable.civilian_font );
			double font_scale = Math.round(civilian_font.getIntrinsicHeight()/height);
			int font_width = (int)Math.round(civilian_font.getIntrinsicWidth()/font_scale);
			civilian_font.setBounds( 0, 0, font_width, height );
			names_button[i].setCompoundDrawables(civilian_font, null, null, null );
			names_button[i].setPadding(0, 0, font_width, 0);
			
			/*Drawable dr = getResources().getDrawable(R.drawable.civilian_font);
			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
			names_button[i].setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
			names_button[i].setPadding(0, 0, 60, 0);*/
	    }
	    else if (font.equals("spy")){
	    	Drawable spy_font = this.getResources().getDrawable( R.drawable.spy_font );
			double font_scale = Math.round(spy_font.getIntrinsicHeight()/height);
			int font_width = (int)Math.round(spy_font.getIntrinsicWidth()/font_scale);
			spy_font.setBounds( 0, 0, font_width, height );
			names_button[i].setCompoundDrawables(spy_font, null, null, null );
			names_button[i].setPadding(0, 0, font_width, 0);
	    }
	    else if (font.equals("blank")){
	    	Drawable blank_font = this.getResources().getDrawable( R.drawable.blank_font );
			double font_scale = Math.round(blank_font.getIntrinsicHeight()/height);
			int font_width = (int)Math.round(blank_font.getIntrinsicWidth()/font_scale);
			blank_font.setBounds( 0, 0, font_width, height );
			names_button[i].setCompoundDrawables(blank_font, null, null, null );
			names_button[i].setPadding(0, 0, font_width, 0);
	    }
	}
	
	private void win_bundle(){
		Bundle b=new Bundle();
		b.putStringArray("WordList", word_list[num_spy]);
		b.putString("word_civilian", word_civilian);		
		b.putStringArray("names", name_list);
		b.putInt("numberofplayers", num_of_players);
		b.putString("category", category);
		b.putBoolean("BlankCard", blank_card);
		if (blank_card) {			
			b.putStringArray("BlankList", word_list[num_blank]);
		}
		
		Intent Win = new Intent(this, WinScreen.class);
		Win.putExtras(b);
		startActivity(Win);
		finish();
	}
	
	public void invalid_alert(String alert_phrase) {
	       TextView displayView = new TextView(this);
	       displayView.setBackgroundColor(Color.DKGRAY);
	       displayView.setTextColor(Color.YELLOW);
	       displayView.setPadding(10,10,10,10);
	       displayView.setText(alert_phrase);
	       Toast theToast = new Toast(this);
	       theToast.setView(displayView);
	       theToast.setDuration(Toast.LENGTH_LONG);
	       theToast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
	       theToast.show();
  }
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	    .setTitle("Exit?")
	    .setMessage("Are you sure you want to end the current game?")
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) { 
	        	dialog_exit();
	        }
	     })
	    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) { 
	        	dialog.cancel();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	
	private void menu_popup(final Activity context) {	
		LinearLayout gamescreen_layout = (LinearLayout)context.findViewById(R.id.gamescreen_root);		
		int height = gamescreen_layout.getHeight();
		
		// Inflate the popup_layout.xml
		RelativeLayout menu_popup_layout = (RelativeLayout)context.findViewById(R.id.menu_layout);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popup_layout = layoutInflater.inflate(R.layout.menu_popup, menu_popup_layout);	
		        
		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(popup_layout);
		popup.setWidth(LayoutParams.MATCH_PARENT);
		popup.setHeight(height);
		popup.setFocusable(true);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String user_pref = sp.getString("background_preference","Simple");
    	
        if (user_pref.equals("Simple")){
        	popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_1));   	
        }
        else if (user_pref.equals("Night")){
        	popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_2));
        }
		 
		// Clear the default translucent background
		//popup.setBackgroundDrawable(new BitmapDrawable());
		 
		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(popup_layout, Gravity.BOTTOM, 0, 0);
		
		//Buttons
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
	        menu_popup(GameScreen.this);
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}
	
	public void dialog_exit(){
		Intent Homescreen = new Intent(this, MainMenu.class);
		startActivity(Homescreen);
	}	

}

