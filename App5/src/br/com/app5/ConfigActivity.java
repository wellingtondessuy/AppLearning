package br.com.app5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ConfigActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private Button btnData;
	private CheckBox chkFull;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		btnBack = (Button) findViewById(R.id.config_btn_back);
		btnData = (Button) findViewById(R.id.config_btn_data);
		chkFull = (CheckBox) findViewById(R.id.checkbox_fullscreen);
		
		//Adiciona ao botão o evento de click.
		btnBack.setOnClickListener(this);
		btnData.setOnClickListener(this);
		
		prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Boolean fullscreen = prefs.getBoolean("FULLSCREEN", false);
		
		if(fullscreen == true) chkFull.setChecked(true);
		toggleFullscreen (fullscreen);
		
	}
	
	@Override
	public void onClick (View v) {
		//Se for o botão voltar
		if(v.getId() == R.id.config_btn_back) {
			
			startActivity(new Intent (this, MainActivity.class));
			finish();
			
		}else if(v.getId() == R.id.config_btn_data) {
			
			Boolean fullscreen = prefs.getBoolean("FULLSCREEN", false);
			Toast.makeText(this, "Cancelar" + fullscreen, Toast.LENGTH_LONG).show();
	
		}
	}
	
	private void toggleFullscreen (boolean fullscreen)
	{
	    WindowManager.LayoutParams attrs = getWindow().getAttributes();
	    if (fullscreen)
	    {
	        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	    }
	    else
	    {
	        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
	    }
	    getWindow().setAttributes(attrs);
	}
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean fullscreen = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	    
	        case R.id.checkbox_fullscreen:
	        	
	            if (fullscreen){
		    		Editor ed = prefs.edit();
		    		ed.putBoolean("FULLSCREEN", true);
		    		ed.commit();
		    		toggleFullscreen(fullscreen);
	            }else{
	            	Editor ed = prefs.edit();
		    		ed.putBoolean("FULLSCREEN", false);
		    		ed.commit();
		    		toggleFullscreen(fullscreen);
	            }
	            	
	            break;
	            
	    }
	}
	
	
}
