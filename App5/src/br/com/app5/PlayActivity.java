package br.com.app5;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class PlayActivity extends Activity {

	private TextView textview;
	private TextView textview2;
	
	private MediaPlayer mediaPlayer;
	
	boolean status = false, countdown = false; 
	
	final CounterClass timer1 = new CounterClass (75,75);
	final CounterClass timer2 = new CounterClass (1000,1000);
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		textview2 = (TextView) findViewById(R.id.play_textview2);
		textview = (TextView) findViewById(R.id.play_textview);
		mediaPlayer = MediaPlayer.create(this, R.raw.beep_07);
		
	}
	
	public void onClick(View view) {
		
		if (status) { 
			
			status = false;
			textview.setText("Play");
			//mediaPlayer.pause();
		
		} else { 
			
			status = true;
			textview.setText("Pause");
			//mediaPlayer.start();
			timer1.start();
			
		}
		
	}
	
	public class CounterClass extends CountDownTimer {

		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long arg0) {}
		
		@Override
		public void onFinish() {
			
			if (countdown) { // Timer 2 - tempo de 1000ms
				
				countdown = false;
				//mediaPlayer.start();
				timer1.start();
				textview2.setText("Final de 1000");
			
			} else { // Timer 1 - tempo de 75ms
				
				countdown = true;
				//mediaPlayer.pause();
				timer2.start();
				textview2.setText("Final de 75");
				
			}
		
		}
		
	}
	
	public void MensagemAlerta (String TituloAlerta, String MensagemAlerta) {
		
		AlertDialog.Builder message = new AlertDialog.Builder(PlayActivity.this);
		message.setTitle(TituloAlerta);
		message.setMessage(MensagemAlerta);
		message.setNeutralButton("OK", null);
		message.show();
		
	}
}
