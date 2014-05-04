package com.example.shopbookeeping;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;
import com.example.shopbookeeping.ActivePaymentsActivity.PlaceholderFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ActionBar.LayoutParams;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class PaymentsExpensesByDayActivity extends ActionBarActivity implements AsyncTaskCompleteListener<ActionConnection>, OnItemClickListener,OnClickListener{



	List <PaymentRow> payments;
	List <ExpenseRow> expenses;
	ListView lv_payments_by_day;
	ListView lv_expenses_by_day;
	String user;
	String password;
	String date;
	boolean user_leave;
	PaymentArrayAdapter adapterPayment;
	ExpenseArrayAdapter adapterExpense;
	String []name_expenses;
	String []providers;
	Dialog dialog;	
	EditText amount;
	Spinner spinner;
	CheckBox state;
	double total_amount_payments;
	double total_amount_expenses;
	TextView tv_amount_payments;
	TextView tv_amount_expenses;
	static final String amount_message_payments = "Total payments: ";
	static final String amount_message_expenses = "Total expenses: ";
	NumberFormat currencyFormat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		total_amount_payments=0;
		total_amount_expenses=0;
		user_leave =false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payments_expenses_by_day);
		Intent i = getIntent();
		user=i.getStringExtra("user");
		password=i.getStringExtra("password");
		date=i.getStringExtra("date");
		payments = new ArrayList<PaymentRow>();
		expenses = new ArrayList<ExpenseRow>();
		lv_payments_by_day = (ListView) findViewById(R.id.lv_payments_by_day);
		lv_expenses_by_day = (ListView) findViewById(R.id.lv_expenses_by_day);
		tv_amount_payments = (TextView) findViewById(R.id.tv_amount_payments);
		tv_amount_expenses = (TextView) findViewById(R.id.tv_amount_expenses);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		  ConnectionAPI connectionPayment = new ConnectionAPI(this,user,password,this);
		  connectionPayment.execute(new ActionConnection(Action.GET,Table.PAYMENTS_BY_DAY,null,date));
		  ConnectionAPI connectionExpense = new ConnectionAPI(this,user,password,this);
		  connectionExpense.execute(new ActionConnection(Action.GET,Table.EXPENSES_BY_DAY,null,date));
		  ConnectionAPI connectionName_expenses = new ConnectionAPI(this,user,password,this);
		  connectionName_expenses.execute(new ActionConnection(Action.GET,Table.NAME_EXPENSES,null,""));
		  ConnectionAPI connectionName_providers = new ConnectionAPI(this,user,password,this);
		  connectionName_providers.execute(new ActionConnection(Action.GET,Table.PROVIDERS,null,""));		  
		  Locale locale = new Locale("es","ES");
		  currencyFormat = NumberFormat.getCurrencyInstance(locale);

		  
	}
	public void onTaskComplete(ActionConnection result) {
	   JSONArray jsonArray = result.getGetJSON();
	   JSONObject jsonObject = new JSONObject();
	   int id=0;
	   String amount ="",provider="",date="",name="";
	   boolean state = true;
	   if (result.isStatus()){
			   try {
				   if(result.getTable()==Table.PAYMENTS_BY_DAY ||result.getTable()==Table.PAYMENTS){
					   	payments.clear();
					   	total_amount_payments=0;
				        for (int i = 0; i < jsonArray.length(); i++) {
				        	jsonObject = jsonArray.getJSONObject(i);
				        	id=jsonObject.getInt("id");
				        	amount = jsonObject.getString("amount");
				        	date = jsonObject.getString("day_date");
				        	provider= jsonObject.getString("provider");
				        	state= jsonObject.getBoolean("state");
				    		total_amount_payments+= Double.valueOf(amount);

				        	payments.add(new PaymentRow(id,date,provider,amount,state));
				        }
						tv_amount_payments.setText(amount_message_payments+getFormat(Double.toString(total_amount_payments)));

				        if (adapterPayment==null){
				        	  adapterPayment=new PaymentArrayAdapter(this,payments);
							  lv_payments_by_day.setAdapter(adapterPayment);
							  lv_payments_by_day.setOnItemClickListener(this); 	

				        }else{
							 adapterPayment.notifyDataSetChanged();
				        }
	
				    }else if(result.getTable()==Table.EXPENSES_BY_DAY || result.getTable()==Table.EXPENSES){
				    	expenses.clear();
					   	total_amount_expenses=0;
				    	  for (int i = 0; i < jsonArray.length(); i++) {
					        	jsonObject = jsonArray.getJSONObject(i);
					        	id=jsonObject.getInt("id");
					        	amount = jsonObject.getString("amount");
					        	date = jsonObject.getString("day_date");
					        	name= jsonObject.getString("name_expense");
					    		total_amount_expenses+= Double.valueOf(amount);
					        	
					        	expenses.add(new ExpenseRow(id,date,name,amount));
					        }
				    	  tv_amount_expenses.setText(amount_message_expenses+getFormat(Double.toString(total_amount_expenses)));
				    	  	if(adapterExpense ==null){
				    	  		adapterExpense =  new ExpenseArrayAdapter(this,expenses);
				    	  		lv_expenses_by_day.setAdapter(adapterExpense);
								   lv_expenses_by_day.setOnItemClickListener(this); 
				    	  	}else{adapterExpense.notifyDataSetChanged();}
				    	
				   }else if(result.getTable()==Table.NAME_EXPENSES){
				        name_expenses= new String[jsonArray.length()];
				        for (int i = 0; i < jsonArray.length(); i++) {
				        	jsonObject = jsonArray.getJSONObject(i);
				        	name_expenses[i] = jsonObject.getString("name");
				        }
				        
				   }else if(result.getTable()==Table.PROVIDERS){
				        providers= new String[jsonArray.length()];
				        for (int i = 0; i < jsonArray.length(); i++) {
				        	jsonObject = jsonArray.getJSONObject(i);
				        	providers[i] = jsonObject.getString("name");
				        }
				   }
				}catch (JSONException e) {
					System.out.println(e.getMessage());
			        // handle JSON parsing exceptions...
				}		
			   
	   }else{ 
		   new NotConexionDialog(this).showNoConexionDialog();

	   }

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
//	public void onBtnClickedSaveProvider(View v) throws JSONException{
//		EditText nameIntroduced = (EditText)findViewById(R.id.new_name);
//
//		String new_name = nameIntroduced.getText().toString();
//		if (new_name.trim()!=""){
//			  JSONObject jsonParam = new JSONObject();
//		         jsonParam.put("name", new_name);
//				  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password);
//				  connectionAPI.execute(new ActionConnection(Action.POST,Table.ACTIVE_PAYMENTS,jsonParam,""));
//		}
//
//	}

	public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
		user_leave =true;
		Intent next_screen;
		switch (parent.getId()) {
        case R.id.lv_payments_by_day:
    		 next_screen = new Intent(this,ShowPaymentActivity.class);
    		next_screen.putExtra("payment",payments.get(position));
            next_screen.putExtra("user", user);
            next_screen.putExtra("password", password);
            startActivity(next_screen);	
        	break;
        case R.id.lv_expenses_by_day:
    		 next_screen = new Intent(this,ShowExpenseActivity.class);
    		next_screen.putExtra("expense",expenses.get(position));
            next_screen.putExtra("user", user);
            next_screen.putExtra("password", password);
            startActivity(next_screen);	
            break;
    }
	}
	
	public void onResume(){
		
		    super.onResume();
		    if (user_leave){
				ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
				connectionAPI.execute(new ActionConnection(Action.GET,Table.PAYMENTS_BY_DAY,null,date));
				ConnectionAPI connectionAPI1 = new ConnectionAPI(this,user,password,this);
				connectionAPI1.execute(new ActionConnection(Action.GET,Table.EXPENSES_BY_DAY,null,date));	
		}
	}
	public void onClickAddPayment(View v){

	    dialog = new Dialog(this);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_payment);
//		dialog.setTitle("Add payment");
		spinner = (Spinner)dialog.findViewById(R.id.s_providers);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.spinner_item, providers);		
		spinner.setAdapter(adp);
		spinner.setFocusable(true);
		spinner.setFocusableInTouchMode(true);
		amount = (EditText)dialog.findViewById(R.id.et_amount);
		state = (CheckBox)dialog.findViewById(R.id.cb_paid);
		Button dialog_b_cancel = (Button) dialog.findViewById(R.id.b_cancel_payment);
		dialog_b_cancel.setOnClickListener(this);
		Button dialog_b_save = (Button) dialog.findViewById(R.id.b_save_payment);
		dialog_b_save.setOnClickListener(this);
		

		dialog.show();
		
//		
	}
	public void onClickAddExpense(View v){	
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_add_expense);
		dialog.setTitle("Add expense");
		spinner = (Spinner)dialog.findViewById(R.id.s_expenses);
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,  R.layout.spinner_item, name_expenses);		
		spinner.setAdapter(adp);
		spinner.setFocusable(true);
		spinner.setFocusableInTouchMode(true);
		amount = (EditText)dialog.findViewById(R.id.et_amount);
		Button dialog_b_cancel = (Button) dialog.findViewById(R.id.b_cancel_expense);
		dialog_b_cancel.setOnClickListener(this);
		Button dialog_b_save = (Button) dialog.findViewById(R.id.b_save_expense);
		dialog_b_save.setOnClickListener(this);
		

		dialog.show();		
	}
	
	@Override
	public void onClick(View v) {
		try{
		if(dialog!= null) dialog.dismiss();
		if (v.getId()==R.id.b_cancel_payment ||v.getId()==R.id.b_cancel_expense){

			System.out.println("cancel");
		}else if(v.getId()==R.id.b_save_payment && amount.getText().toString()!=""){
			String amount_intserted = amount.getText().toString();
			String provider_inserted =new String( providers[spinner.getSelectedItemPosition()].trim().getBytes("UTF-8"),"UTF-8");
			boolean state_inserted = state.isChecked();
			if (true){//comprobar que los datos introducidos son correctos
				  JSONObject jsonParam = new JSONObject();
			         jsonParam.put("amount", amount_intserted);
			         jsonParam.put("provider", provider_inserted);
			         jsonParam.put("state", state_inserted);
			         jsonParam.put("day_date",date);
//			         System.out.println(jsonParam);
//			         payments.clear();				
//			         adapterPayment.notifyDataSetChanged();
					 ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
					 connectionAPI.execute(new ActionConnection(Action.POST,Table.PAYMENTS,jsonParam,"","payments_by_day/"+date));

			}}else if(v.getId()==R.id.b_save_expense && amount.getText().toString()!=""){
				String amount_intserted = amount.getText().toString();
				String expense_inserted =new String( name_expenses[spinner.getSelectedItemPosition()].trim().getBytes("UTF-8"),"UTF-8");
				if (true){//comprobar que los datos introducidos son correctos
					  JSONObject jsonParam = new JSONObject();
				         jsonParam.put("amount", amount_intserted);
				         jsonParam.put("name_expense", expense_inserted);
				         jsonParam.put("day_date",date);
						 ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
						 connectionAPI.execute(new ActionConnection(Action.POST,Table.EXPENSES,jsonParam,"","expenses_by_day/"+date));

				}}
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public String getFormat(String a){
		return currencyFormat.format(Double.valueOf(a));

		//return String.format("%1$,.2f", Double.valueOf(a));		
	}

}
