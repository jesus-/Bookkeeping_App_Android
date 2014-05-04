package com.example.shopbookeeping;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;
import com.example.shopbookeeping.ProviderActivity.PlaceholderFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class NameExpensesActivity extends ActionBarActivity implements AsyncTaskCompleteListener<ActionConnection>, OnItemClickListener,OnClickListener,OnItemLongClickListener,DialogInterface.OnClickListener {


	ConnectionAPI connectionAPI;
	int[] intID=null; // Array with the ID showed in the listview
	ArrayList <String> stringName;//Array with the name showed in the listview
	String user;
	String password;
	int idToDelete = 0;
	ListView lv_expenses;
	ArrayAdapter<String> adapter;
	Dialog dialog;
	EditText name;
	int expense_delete=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_expenses);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		Intent i = getIntent();
		user=i.getStringExtra("user");
		password=i.getStringExtra("password");
		ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		connectionAPI.execute(new ActionConnection(Action.GET,Table.NAME_EXPENSES,null,""));
		lv_expenses = (ListView) findViewById(R.id.lv_name_expenses);
		stringName = new ArrayList<String>();
	}
	public void onTaskComplete(ActionConnection result) {
	   JSONArray jsonArray = result.getGetJSON();
	   JSONObject jsonObject = new JSONObject();
	   if (result.isStatus()){
		   try {
			   stringName.clear();
		        intID = new int[jsonArray.length()];
			   for (int i = 0; i < jsonArray.length(); i++) {
				   jsonObject = jsonArray.getJSONObject(i);
				   intID[i] =jsonObject.getInt("id");
				   stringName.add(jsonObject.getString("name"));
			   	}
				}catch (JSONException e) {
					System.out.println(e.getMessage());
			        // handle JSON parsing exceptions...
				}
		   		if(adapter == null){
		   			 adapter = new ArrayAdapter<String>(this, R.layout.name_row_listview,R.id.first_column, stringName); 
		   			lv_expenses.setAdapter(adapter);
		   			lv_expenses.setOnItemClickListener(this); 	
		   			lv_expenses.setOnItemLongClickListener(this);
		   		}else adapter.notifyDataSetChanged();			   
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

	public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
		
//		idToDelete= intID[position];
//    	 String item = (String) parent.getItemAtPosition(position);
//    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
// 		 alertDialogBuilder.setTitle("Delete provider");
//  
// 			// set dialog message
// 			alertDialogBuilder.setMessage("Are you sure you want to delete "+item).setCancelable(false)
// 				.setPositiveButton("Yes",this)
// 				.setNegativeButton("No",this);
//  
// 				// create alert dialog
// 				AlertDialog alertDialog = alertDialogBuilder.create();
//  
// 				// show it
// 				alertDialog.show();
		   Intent nextScreen = new Intent(getApplicationContext(),ExpenseByNameActivity.class);
	       nextScreen.putExtra("user", user);
	       nextScreen.putExtra("password", password);
	       nextScreen.putExtra("name_expense", stringName.get(position));
	       startActivity(nextScreen);	
	}


	public void onBtnClickedSaveExpense(View v){

	    dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_add_element);
		dialog.setTitle("Add Expense");
		name = (EditText)dialog.findViewById(R.id.et_name);
		Button dialog_b_cancel = (Button) dialog.findViewById(R.id.b_cancel_element);
		dialog_b_cancel.setOnClickListener(this);
		Button dialog_b_save = (Button) dialog.findViewById(R.id.b_save_element);
		dialog_b_save.setOnClickListener(this);
		

		dialog.show();
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
						 connectionAPI.execute(new ActionConnection(Action.POST,Table.NAME_EXPENSES,jsonParam,""));

			
				}
			}
			}catch( Exception e){e.printStackTrace();}
		
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which ==-1){
			  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
			  connectionAPI.execute(new ActionConnection(Action.DELETE,Table.NAME_EXPENSES,null,Integer.toString(intID[expense_delete ]),Table.NAME_EXPENSES.GetURL()));			
	}		
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		expense_delete = arg2;
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 alertDialogBuilder.setTitle("Delete expense");
	
			// set dialog message
			alertDialogBuilder.setMessage("Are you sure you want to delete this expense").setCancelable(false)
				.setPositiveButton("Yes",this)
				.setNegativeButton("No",this);
			// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
	
				// show it
				alertDialog.show();
			return false;		
	}
			 
}
