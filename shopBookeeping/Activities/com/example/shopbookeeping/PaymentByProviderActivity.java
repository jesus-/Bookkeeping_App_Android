package com.example.shopbookeeping;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.shopbookeeping.ActionConnection.Action;
import com.example.shopbookeeping.ActionConnection.Table;
import com.example.shopbookeeping.ActivePaymentsActivity.PlaceholderFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class PaymentByProviderActivity extends CustomActionBarActivity implements AsyncTaskCompleteListener<ActionConnection>, OnItemClickListener,OnClickListener {

	ConnectionAPI connectionAPI;
	List <PaymentRow> payments;
	ListView lv_payment_by_provider;
	String provider;
	PaymentArrayAdapter adapter;
	int idToDelete = 0;
	boolean user_leave;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_by_provider);
		lv_payment_by_provider = (ListView) findViewById(R.id.payment_by_provider);
		
		Intent i = getIntent();
		user=i.getStringExtra("user");
		password=i.getStringExtra("password");
		provider=i.getStringExtra("provider");
		payments = new ArrayList<PaymentRow>();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		  provider = provider.replace(" ", "_");
		  connectionAPI.execute(new ActionConnection(Action.GET,Table.PAYMENTS_BY_PROVIDER,null,provider));
		  user_leave=false;

	}
	public void onTaskComplete(ActionConnection result) {
	   JSONArray jsonArray = result.getGetJSON();
	   JSONObject jsonObject = new JSONObject();
	   int id=0;
	   String amount ="",provider="",date="";
	   boolean state = true;
//		   System.out.println(result.toString());
	   if (result.isStatus()){
			   try {
				   payments.clear();
			        for (int i = 0; i < jsonArray.length(); i++) {
			        	jsonObject = jsonArray.getJSONObject(i);
			        	id=jsonObject.getInt("id");
			        	amount = jsonObject.getString("amount");
			        	date = jsonObject.getString("day_date");
			        	provider= jsonObject.getString("provider");
			        	state= jsonObject.getBoolean("state");
			        	
			        	payments.add(new PaymentRow(id,date,provider,amount,state));
			        }
				}catch (JSONException e) {
					System.out.println(e.getMessage());
			        // handle JSON parsing exceptions...
				}
			   if (adapter==null){
				   adapter = new PaymentArrayAdapter(this,payments); 
				   lv_payment_by_provider.setAdapter(adapter);
				   lv_payment_by_provider.setOnItemClickListener(this); 			
			   }else adapter.notifyDataSetChanged();
			   
	   }else{ new NotConexionDialog(this).showNoConexionDialog();}
	   user_leave=true;
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
		user_leave = true;
		Intent next_screen = new Intent(this,ShowPaymentActivity.class);
		next_screen.putExtra("payment",payments.get(position));
        next_screen.putExtra("user", user);
        next_screen.putExtra("password", password);
        startActivity(next_screen);		
        
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	
		if (which ==-1){
			  ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);

//			  connectionAPI.execute(new ActionConnection(Action.DELETE,Table.ACTIVE_PAYMENTS,null,idToDelete,0));			
		}
			
	}
public void onResume(){
    super.onResume();
    if(user_leave){
		ConnectionAPI connectionAPI = new ConnectionAPI(this,user,password,this);
		provider = provider.replace(" ", "_");
		connectionAPI.execute(new ActionConnection(Action.GET,Table.PAYMENTS_BY_PROVIDER,null,provider));
	}
}

}
