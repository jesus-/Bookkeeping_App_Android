package com.example.shopbookeeping;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements OnDateChangeListener,AsyncTaskCompleteListener<ActionConnection>,OnClickListener{
	
	String user = "prueba";
	String password = "prueba";
	CalendarView cv_calendar;
	SimpleDateFormat dateformat;
	WorkingDayRow workingDay;
	TextView tv_date;
	TextView tv_sale;
	TextView tv_cash;
	TextView tv_change;
	long date_selected;
	Dialog dialog;
	boolean day_exist;
	NumberFormat currencyFormat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		day_exist=false;
	    cv_calendar = (CalendarView)findViewById(R.id.cv_calendar);

	    cv_calendar.setOnDateChangeListener(this);
		tv_date=(TextView)findViewById(R.id.day_date);
		tv_sale=(TextView)findViewById(R.id.tv_sale_day);
		tv_cash=(TextView)findViewById(R.id.tv_cash_day);
		tv_change=(TextView)findViewById(R.id.tv_change_day);
	    Date now = new Date();
	    dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	    date_selected=now.getTime();
	    cv_calendar.setDate(now.getTime());
	    
		tv_date.setText("Day: "+dateformat.format(new Date(date_selected)));

		requestWorkingDay(date_selected);
		 Locale locale = new Locale("es","ES");
		 currencyFormat = NumberFormat.getCurrencyInstance(locale);



		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	    ViewServer.get(this).addWindow(this);

	}
	
	public void onTaskComplete(ActionConnection result) {
	   JSONArray jsonArray = result.getGetJSON();
	   JSONObject jsonObject = new JSONObject();
	   int id=0;
	   String sale ="",cash="",change_coins="",date="";
	   if (result.isStatus()){
		   try {
		   		if (jsonArray.length() ==0){
		   			day_exist = false;
		   		}
		   		else{
		   			day_exist = true;
		        	jsonObject = jsonArray.getJSONObject(0);
		        	id=jsonObject.getInt("id");
		        	sale = jsonObject.getString("sale");
		        	cash = jsonObject.getString("cash");
		        	date = jsonObject.getString("day_date");
		        	change_coins= jsonObject.getString("change_coins");
		        	workingDay = new WorkingDayRow(id,date,sale,cash,change_coins);
		        	informLayaout();
		   }
			}catch (JSONException e) {
				System.out.println(e.getMessage());
		        // handle JSON parsing exceptions...
			}
	
			   
	   }
	   else{ new NotConexionDialog(this).showNoConexionDialog();}
	  }	
	public void onSelectedDayChange(CalendarView view,
		int year, int month, int dayOfMonth) {
		long date_selected_now = cv_calendar.getDate();
		if (date_selected!=date_selected_now){	
			requestWorkingDay(date_selected_now);
			date_selected=date_selected_now;
			tv_date.setText("Day: "+dateformat.format(new Date(date_selected)));
		}
	
	
	}
	public void requestWorkingDay(Long day_long){
		String day = dateformat.format(new Date(day_long));
	//	tv_date.setText("");
		tv_sale.setText(""); 
		tv_cash.setText("");
		tv_change.setText("");
		ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		connectionAPI.execute(new ActionConnection(Action.GET,Table.WORKING_DAYS_BY_DATE,null,day));	
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
		Intent nextScreen;
	    switch (item.getItemId()) {
	    
	    case R.id.action_providers:	    	
			  nextScreen = new Intent(getApplicationContext(), ProviderActivity.class);
			 System.out.println("setting params");
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);	
	    	break;
	    case R.id.action_expenses: 	
			  nextScreen = new Intent(getApplicationContext(), NameExpensesActivity.class);
			 System.out.println("setting params");
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);		    	
	    	
	    	break;
	    case R.id.action_active_payments: 	
			  nextScreen = new Intent(getApplicationContext(), ActivePaymentsActivity.class);
			 System.out.println("setting params");
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);		    	
	    	
	    	break;
	    default:
	    	break;
	    	
	    }

		return true;
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void onClickGoDay(View v){
		Intent nextScreen;
		nextScreen = new Intent(getApplicationContext(), PaymentsExpensesByDayActivity.class);
		System.out.println("setting params");
        nextScreen.putExtra("user", user);
        nextScreen.putExtra("password", password);
        nextScreen.putExtra("date", dateformat.format(new Date(date_selected)));
        startActivity(nextScreen);				
		
		
	}
	public void informLayaout(){
//		tv_date.setText(workingDay.getDate());
		tv_sale.setText(getFormat(workingDay.getSale())); 
		tv_cash.setText(getFormat(workingDay.getCash()));
		tv_change.setText(getFormat(workingDay.getChange_coins()));

	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    ViewServer.get(this).setFocusedWindow(this);

	}

	@Override
	protected void onDestroy() {

	    super.onDestroy();
	    ViewServer.get(this).removeWindow(this);
	}	
	public void onClickAddDay(View v){
		dialog = new Dialog(this);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_working_day);
		dialog.setTitle("Add expense");
		Button dialog_b_cancel = (Button) dialog.findViewById(R.id.b_cancel_working_day);
		dialog_b_cancel.setOnClickListener(this);
		Button dialog_b_save = (Button) dialog.findViewById(R.id.b_save_working_day);
		dialog_b_save.setOnClickListener(this);
		
		dialog.show();		
	}
	
	@Override
	public void onClick(View v) {
		String date =dateformat.format(new Date(date_selected));
		try{
		if(dialog!= null) dialog.dismiss();
		if (v.getId()==R.id.b_cancel_working_day){
			System.out.println("cancel");
		}else if(v.getId()==R.id.b_save_working_day ){
				EditText tv_sale_i = (EditText)dialog.findViewById(R.id.et_sale);
				EditText tv_cash_i = (EditText)dialog.findViewById(R.id.et_cash);
				EditText tv_change_i = (EditText)dialog.findViewById(R.id.et_change);
				String sale_i = tv_sale_i.getText().toString();
				sale_i=	sale_i.compareTo("")==0? "0.0":sale_i;
				String cash_i = tv_cash_i.getText().toString();
				cash_i	= cash_i.compareTo("")==0? "0.0":cash_i;
				String change_i = tv_change_i.getText().toString();
				change_i= change_i.compareTo("")==0? "0.0":change_i;
				  JSONObject jsonParam = new JSONObject();
			         jsonParam.put("sale", sale_i);
			         jsonParam.put("cash", cash_i );
			         jsonParam.put("change_coins", change_i );
			         jsonParam.put("day_date",date);
					 ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
			         if(day_exist){
			        	 connectionAPI.execute(new ActionConnection(Action.PUT,Table.WORKING_DAYS,jsonParam,
			        			 Integer.toString(workingDay.getId()),"working_days_by_date/"+date));
			         }else{
			        	 connectionAPI.execute(new ActionConnection(Action.POST,Table.WORKING_DAYS,jsonParam,"","working_days_by_date/"+date));
			         }
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	public String getFormat(String a){
		return currencyFormat.format(Double.valueOf(a));

		//return String.format("%1$,.2f", Double.valueOf(a));		
	}
}
