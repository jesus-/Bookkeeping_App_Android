package com.example.shopbookeeping;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;
import com.example.shopbookeeping.PaymentByProviderActivity.PlaceholderFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class ShowPaymentActivity extends CustomActionBarActivity implements
	AsyncTaskCompleteListener<ActionConnection>, OnItemClickListener,
	OnClickListener,OnItemSelectedListener {

	ConnectionAPI connectionAPI;
	PaymentRow payment;	
	EditText et_id;
	EditText et_date;
	Spinner s_providers;
	EditText et_amount;
	EditText et_state;
//	TextView tv_status;
	Button b_state;
	Button b_block;
	Button b_delete;
	Button b_save;
	int idToDelete = 0;
	int spinner_position=0;
	Boolean block;
	String [] providers;
	ArrayAdapter<String> adaptador;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_payment);
		Intent i = getIntent();
		user=i.getStringExtra("user");
		password=i.getStringExtra("password");
		payment=i.getParcelableExtra("payment");
		block = true;
		 et_id = (EditText) findViewById(R.id.et_id);
		 et_date = (EditText) findViewById(R.id.et_date);
		 s_providers = (Spinner) findViewById(R.id.s_providers);
		 et_amount = (EditText) findViewById(R.id.et_amount);
		 et_state = (EditText) findViewById(R.id.et_state);
		 b_state =(Button) findViewById(R.id.b_mark_state);
		 b_block = (Button) findViewById(R.id.b_block);
		 b_save= (Button) findViewById(R.id.b_save);
		 b_delete= (Button) findViewById(R.id.b_delete);
		//tv_status tv_status = (TextView)findViewById(R.id.tv_status);
		 informActivity();
		 manageButtons();
		 ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		 connectionAPI.execute(new ActionConnection(Action.GET,Table.PROVIDERS,null,""));
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}	

	}
	public void onTaskComplete(ActionConnection result) {
		
		
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = result.getGetJSON();

	   if (result.isStatus()){
		   try {
			   if(result.getTable()==Table.PAYMENTS){
			//   		tv_status.setText("Payment saved");
			   		block = true;
			   		manageButtons();
			   	
			   }else if(result.getTable()==Table.PROVIDERS){

			        providers= new String[jsonArray.length()];
			        for (int i = 0; i < jsonArray.length(); i++) {
			        	jsonObject = jsonArray.getJSONObject(i);
			        	providers[i] = jsonObject.getString("name");
			        }
			        if(adaptador==null){
			        	adaptador = new ArrayAdapter<String>(this, R.layout.spinner_item, providers);
			        s_providers.setAdapter(adaptador);
			        spinner_position = adaptador.getPosition(payment.getProvider());
					s_providers.setSelection(spinner_position);
					s_providers.setOnItemSelectedListener(this);
			        }else {adaptador.notifyDataSetChanged();}
			               
			   }
			   
			}catch (JSONException e) {
				System.out.println(e.getMessage());
		        // handle JSON parsing exceptions...
			}
			informActivity();
			   
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
//		EditText nameIntroduced = (EditText)findViewById(R.id.new_name);
//
//		String new_name = nameIntroduced.getText().toString();
//		if (new_name.trim()!=""){
//			  JSONObject jsonParam = new JSONObject();
//		         jsonParam.put("name", new_name);
//				  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password);
//				  connectionAPI.execute(new ActionConnection(Action.POST,Table.ACTIVE_PAYMENTS,jsonParam,0,0));
//		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	
		if (which ==-1){
			  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
			  connectionAPI.execute(new ActionConnection(Action.DELETE,Table.PAYMENTS,null,Integer.toString(payment.getId()),"mock"));			
			  finish();
		}
			
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void informActivity(){
		 et_id.setText(Integer.toString(payment.getId()));
		 et_date.setText(payment.getDate());
		 s_providers.setSelection(spinner_position);
		 et_amount.setText(payment.getAmount());
	    if (payment.isState()){
		   	 et_state.setText("NotPaid");
		   //  b_state.setText("Mark as paid");
		     b_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.check));

	     }
	    else{
		   	 et_state.setText("Paid");
	//	      b_state.setText("Mark as unpaid");
		     b_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross));
		   	 
	    }
	
	}
	public void onClickState(View v) throws JSONException{
		  payment.setState(!payment.isState());
		  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		  connectionAPI.execute(new ActionConnection(Action.PUT,Table.PAYMENTS,payment.getJSON(),Integer.toString(payment.getId())));
	}
	public void onClickSave(View v) throws JSONException{
		  payment.setProvider(providers[s_providers.getSelectedItemPosition()]);
		  payment.setAmount(et_amount.getText().toString());
		  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		  connectionAPI.execute(new ActionConnection(Action.PUT,Table.PAYMENTS,payment.getJSON(),Integer.toString(payment.getId())));
	}
	public void onClickBlock(View v){
		block = !block;
		manageButtons();
	}
	public void onClickDelete(View v){
		
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 alertDialogBuilder.setTitle("Delete payment");
	
			// set dialog message
			alertDialogBuilder.setMessage("Are you sure you want to delete this payment").setCancelable(false)
				.setPositiveButton("Yes",this)
				.setNegativeButton("No",this);
			// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
	
				// show it
				alertDialog.show();
	}
	public void manageButtons(){
		if (block){
			 et_id.setFocusable(false);
			 et_date.setFocusable(false);
			 s_providers.setClickable(false);
			 s_providers.setFocusable(false);
			 s_providers.setFocusableInTouchMode(false);
			 et_amount.setFocusable(false);
			 et_state.setFocusable(false);
			 b_save.setEnabled(false);
			 b_block.setText("Unlock");			 
		}else{
//			 tv_status.setText("");
			 s_providers.setClickable(true);
			 s_providers.setFocusableInTouchMode(true);
			 et_amount.setFocusableInTouchMode(true);
			 b_save.setEnabled(true);//buttons need this one for disable
			 b_block.setText("Lock");
	
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		spinner_position = arg2;
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void onClickOk(View v){
		finish();
	}
}


