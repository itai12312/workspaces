package com.thenewboston.travis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.google.ads.AdRequest;
//import com.google.ads.AdView;


public class startingPoint extends Activity {
    /** Called when the activity is first created. */
	int counter;
	Button add, sub;
	TextView display;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        counter = 0;
        add = (Button) findViewById(R.id.bAddOne);
        sub = (Button) findViewById(R.id.bSubOne);
        display = (TextView) findViewById(R.id.tvNumber);
            
        //AdView ad = (AdView)findViewById(R.id.ad);
        //ad.loadAd(new AdRequest());
        
        add.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				counter++;
				display.setText("Your total is " + counter);
			}
		});
        sub.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				counter--;
				display.setText("Your total is " + counter);
			}
		});
    }
}