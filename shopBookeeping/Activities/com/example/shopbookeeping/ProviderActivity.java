package com.example.shopbookeeping;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ProviderActivity extends CustomActionBarActivity implements AsyncTaskCompleteListener<ActionConnection>,OnItemLongClickListener, OnItemClickListener,OnClickListener, DialogInterface.OnClickListener{

	ConnectionAPI connectionAPI;
	int[] intID=null; // Array with the ID showed in the listview
	ArrayList<String> stringName;//List with the name showed in the listview	
	int idToDelete = 0;
	EditText name;
	Dialog dialog;
	ArrayAdapter<String> adapter;
	ListView lv_providers;
	int provider_delete=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider);
		Intent i = getIntent();
		user=i.getStringExtra("user");
		password=i.getStringExtra("password");
		lv_providers = (ListView) findViewById(R.id.lv_providers);
		stringName = new ArrayList<String>();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		  connectionAPI.execute(new ActionConnection(Action.GET,Table.PROVIDERS,null,""));
//		  System.out.print(asd);

	}
	public void onTaskComplete(ActionConnection result) {
	   JSONArray jsonArray = result.getGetJSON();
	   JSONObject jsonObject = new JSONObject();
	   
//		   System.out.println(result.toString());
	   if (result.isStatus()){
			   try {
				   stringName.clear();
			        intID = new int[jsonArray.length()];
			        for (int i = 0; i < jsonArray.length(); i++) {
			        	jsonObject = jsonArray.getJSONObject(i);
			        	intID[i] =jsonObject.getInt("id");
			        	stringName.add( jsonObject.getString("name"));
			        }
				}catch (JSONException e) {
					System.out.println(e.getMessage());
			        // handle JSON parsing exceptions...
				}
			   if (adapter==null){
				   adapter = new ArrayAdapter<String>(this, R.layout.name_row_listview,R.id.first_column, stringName); 
				   lv_providers.setAdapter(adapter);
				   lv_providers.setOnItemClickListener(this); 		
				   lv_providers.setOnItemLongClickListener(this);
			   }else{ adapter.notifyDataSetChanged();}
		
			   
	   }else{ new NotConexionDialog(this).showNoConexionDialog();}
	  }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_, container,
					false);
			return rootView;
		}
	}
	public void onBtnClickedSaveProvider(View v) throws JSONException{

	    dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_add_element);
		dialog.setTitle("Add provider");
		name = (EditText)dialog.findViewById(R.id.et_name);
		Button dialog_b_cancel = (Button) dialog.findViewById(R.id.b_cancel_element);
		dialog_b_cancel.setOnClickListener(this);
		Button dialog_b_save = (Button) dialog.findViewById(R.id.b_save_element);
		dialog_b_save.setOnClickListener(this);
		

		dialog.show();

	}
	public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
	   Intent nextScreen = new Intent(getApplicationContext(), PaymentByProviderActivity.class);
       nextScreen.putExtra("user", user);
       nextScreen.putExtra("password", password);
       nextScreen.putExtra("provider", stringName.get(position));
       startActivity(nextScreen);	
	}


	@Override
	public void onClick(View v) {
		try{
		if(dialog!= null){ dialog.dismiss(); dialog=null;}
		if (v.getId()==R.id.b_cancel_element){
			System.out.println("cancel");
		}else if(v.getId()==R.id.b_save_element){
			String name_intserted = name.getText().toString();
			if (true){//comprobar que los datos introducidos son correctos
				  JSONObject jsonParam = new JSONObject();
			         jsonParam.put("name", name_intserted);
					 ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
					 connectionAPI.execute(new ActionConnection(Action.POST,Table.PROVIDERS,jsonParam,""));

		
			}
		}
		}catch( Exception e){e.printStackTrace();}
		
	}
	
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {		
		provider_delete = arg2;
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 alertDialogBuilder.setTitle("Delete provider");
	
			// set dialog message
			alertDialogBuilder.setMessage("Are you sure you want to delete this payment").setCancelable(false)
				.setPositiveButton("Yes",this)
				.setNegativeButton("No",this);
			// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
	
				// show it
				alertDialog.show();
			return false;
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if (which ==-1){
			  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
			  connectionAPI.execute(new ActionConnection(Action.DELETE,Table.PROVIDERS,null,Integer.toString(intID[provider_delete ]),Table.PROVIDERS.GetURL()));			
 	}		
		
	}

	public void onBackPressed() {
		 Intent next_screen = new Intent(this,MainActivity.class);
        next_screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(next_screen);	
	}			 
		
}


