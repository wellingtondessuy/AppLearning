package br.com.app5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//SQLite--------------
import android.database.Cursor;


//PAREI SEM CONSEGUIR VISUALIZAR OS DADOS DO DB
//--------------------

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText edtName;
	private EditText edtAge;
	private Button btnConfirm;
	private Button btnRegister;
	private Button btnRefresh;
	private ListView MostraDados;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences prefs;
	
	//SQLite
	
	public static final String KEY_NOMEPESSOA = "nomepessoa";
	
	String db_name = "Cadastro";
	SQLiteDatabase db = null;
	
	Cursor cursor;
	SimpleCursorAdapter AdapterLista;
	
	//--------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		edtName = (EditText) findViewById(R.id.main_edit_name);
		edtAge = (EditText) findViewById(R.id.main_edit_age);
		btnConfirm = (Button) findViewById(R.id.main_btn_confirm);
		btnRegister = (Button) findViewById(R.id.main_btn_register);
		btnRefresh = (Button) findViewById(R.id.main_btn_refresh);
		
		//Adiciona ao botão o evento de click.
		btnConfirm.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		
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
			String sql = "CREATE TABLE IF NOT EXISTS tabCadastroPessoa (_id INTEGER PRIMARY KEY, nomepessoa TEXT, telefonepessoa TEXT)";
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
			
			db = openOrCreateDatabase(db_name, MODE_WORLD_READABLE, null);
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
		
		MostraDados = (ListView) findViewById(R.id.main_out_text);
		
		if (VerificaRegistro()) {
			
			String [] Coluna = new String[] {"nomepessoa"};
			
			AdapterLista = new SimpleCursorAdapter(this, R.layout.activity_main, cursor, Coluna, new int[] {R.id.main_out_text});
			MostraDados.setAdapter(AdapterLista);
			
			
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
			
			CarregaDado();
			
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
