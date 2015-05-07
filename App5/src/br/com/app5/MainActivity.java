package br.com.app5;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//SQLite--------------
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//--------------------
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//PAREI SEM CONSEGUIR VISUALIZAR OS DADOS DO DB
//--------------------

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText edtName;
	private EditText edtAge;
	private Button btnConfirm;
	private Button btnRegister;
	private Button btnRefresh;
	
	private Button btnPlay;
	private Button btnPause;
	private Button btnStop;
	private MediaPlayer mediaPlayer;
	
	private ListView MostraDados;
	private TextView txtDados;
	private ListView stringListView;
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences prefs;
	
	//SQLite
	
	String db_name = "Cadastro";
	SQLiteDatabase db = null;
	Cursor cursor;
	
	//--------------
	//Testando ListView
	ListView listView;
	
    //-------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Testando listView
		listView = (ListView) findViewById(R.id.list);
		
		/*String[] values = new String[] { "Android List View", 
                 "Adapter implementation",
                 "Simple List View In Android",
                 "Create List View Android", 
                 "Android Example", 
                 "List View Source Code", 
                 "List View Array Adapter", 
                 "Android Example List View" 
                };
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, values);
		
		listView.setAdapter(adapter);*/
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			 
             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
               
              // ListView Clicked item index
              int itemPosition     = position;
              
              // ListView Clicked item value
              String  itemValue    = (String) listView.getItemAtPosition(position);
                 
               // Show Alert 
               Toast.makeText(getApplicationContext(),
                 "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                 .show();
            
             }

        });
		
		//----------------------------
		
		edtName = (EditText) findViewById(R.id.main_edit_name);
		edtAge = (EditText) findViewById(R.id.main_edit_age);
		btnConfirm = (Button) findViewById(R.id.main_btn_confirm);
		btnRegister = (Button) findViewById(R.id.main_btn_register);
		btnRefresh = (Button) findViewById(R.id.main_btn_refresh);
		
		btnPlay = (Button) findViewById(R.id.main_btn_play);
		btnPause = (Button) findViewById(R.id.main_btn_pause);
		btnStop = (Button) findViewById(R.id.main_btn_stop);
		mediaPlayer = MediaPlayer.create(this, R.raw.beep_11);

		
		txtDados = (TextView) findViewById(R.id.main_out_text);
		
		//Adiciona ao botão o evento de click.
		btnConfirm.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		
		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		
		Boolean fullscreen = prefs.getBoolean("FULLSCREEN", false);
		
		toggleFullscreen (fullscreen);
		
		Editor ed = prefs.edit();
		ed.putString("TEXTO", "Batatinha...");
		ed.commit();		
		
		CriaBanco();
		
		
	}
	
	//SQLite---------------
	
	public void CriaBanco () {
		
		try {
			
			db = openOrCreateDatabase(db_name, MODE_WORLD_READABLE, null);
			String sql = "CREATE TABLE IF NOT EXISTS tabCadastroPessoa (_id INTEGER PRIMARY KEY, nomepessoa VARCHAR(40), telefonepessoa VARCHAR(20))";
			db.execSQL(sql);
			MensagemAlerta("Banco de Dados", "Banco criado com sucesso!!");
		
		} catch (Exception erro) {
			
			MensagemAlerta("Banco de Dados", "Erro ao criar o banco de dados!! " + erro);
		
		}
		
		finally {
			
			db.close();
			
		}
		
	}
	
	public void GravaDado () {
		
		try {
			
			db = openOrCreateDatabase(db_name, MODE_WORLD_WRITEABLE, null);
			String sql = "INSERT INTO tabCadastroPessoa (nomepessoa, telefonepessoa) VALUES ('"+ edtName.getText().toString() +"', '" + edtAge.getText().toString() + "')";
			db.execSQL(sql);
			MensagemAlerta("Banco de Dados", "Registro gravado com sucesso!!");
			
		} catch (Exception erro) {
			
			MensagemAlerta("Banco de Dados", "Erro ao gravar no banco de dados!! " + erro);
		
		}
		
		finally {
			
			db.close();
			
		}
		
	}
	
	private boolean VerificaRegistro () {
		
		try {
			
			db = openOrCreateDatabase(db_name, MODE_WORLD_READABLE, null);
			cursor = db.rawQuery("SELECT * FROM tabCadastroPessoa", null);
			
			if (cursor.getCount() != 0) {
				
				cursor.moveToFirst();
				return true;
				
			} else return false;
			
		} catch (Exception erro) {
			
			MensagemAlerta("Banco de Dados", "Não foi possível verificar os dados!");
			return false;
			
		}
		
		finally {
			
			db.close();
			
		}

	}
	
	public void CarregaDado () {

		Boolean go = true;
		
		
		if (VerificaRegistro()) {
			
			ArrayList<String> values = new ArrayList<String>();
			
			//String[] values = new String[5];
			//Integer i = 0;
			String data;
			
			db = openOrCreateDatabase(db_name, MODE_WORLD_READABLE, null);
			cursor = db.rawQuery("SELECT * FROM tabCadastroPessoa", null);
			cursor.moveToFirst();

			try {
				
				while(go){
					
					data = "\nNome: " + cursor.getString(cursor.getColumnIndex("nomepessoa")) + "\nTelefone: " + cursor.getString(cursor.getColumnIndex("telefonepessoa"));
					
					if (cursor.isLast()) go = false;
					else cursor.moveToNext();
				
					values.add(data);
					
					//values[i] = data;
					//i++;
					
				}
				
				//txtDados.setText( data );
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			              android.R.layout.simple_list_item_1, android.R.id.text1, values);
				
				listView.setAdapter(adapter);
				
			} catch (Exception erro) {
				
				MensagemAlerta("Banco de Dados", "Oops! Ocorreu um erro ao recuperar os dados. \n" + erro);
			
			}
			
			finally {
				
				db.close();
				
			}
			
		} else {
			
			MensagemAlerta("Banco de Dados", "Você não possui cadastros!");
			
		}
		
	}
	
	public void MensagemAlerta (String TituloAlerta, String MensagemAlerta) {
		
		AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
		message.setTitle(TituloAlerta);
		message.setMessage(MensagemAlerta);
		message.setNeutralButton("OK", null);
		message.show();
		
	}
	//---------------------
	@Override
	 public void onAttachedToWindow() {
	  // TODO Auto-generated method stub
	     super.onAttachedToWindow();
	 }
	
	//-------
	
	/**
	 * Evento de click chamado através do xml.
	 * @param view
	 */
	public void confirm(View view) {
		Toast.makeText(this, "Blablabla", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		//Se for o botão confirmar
		if(v.getId() == R.id.main_btn_confirm) {
			Intent intent = new Intent (this, ConfigActivity.class);
			intent.putExtra("name", edtName.getText().toString());
			startActivity(intent);
			if(edtName.getText().toString() != "" && edtAge.getText().toString() != ""){ 
				Toast.makeText(this, "Seu nome é " + edtName.getText().toString() + " e você tem " + edtAge.getText().toString() + " anos.", Toast.LENGTH_LONG).show();	
			}
		} else if (v.getId() == R.id.main_btn_register) {
			//Intent intent = new Intent (this, UserAddActivity.class);
			//startActivity(intent);
			GravaDado();
		} else if (v.getId() == R.id.main_btn_refresh) {
			
			//MensagemAlerta("Banco de Dados", "Resposta sobre existência de dados no DB: " + VerificaRegistro());
			CarregaDado();
			
		} else if (v.getId() == R.id.main_btn_play) {
			
			mediaPlayer.start();
			
		} else if (v.getId() == R.id.main_btn_pause) {
			
			mediaPlayer.pause();
			
		} else if (v.getId() == R.id.main_btn_stop) {
			
			//mediaPlayer.stop();
			Intent intent = new Intent (this, PlayActivity.class);
			startActivity(intent);
			
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_config:
	        	Intent intent = new Intent (this, ConfigActivity.class);
				startActivity(intent);
	            return true;
	        case R.id.action_add:
	        	GravaDado();
	        	return true;
	        case R.id.action_refresh:
	        	CarregaDado();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
