package com.example.shopbookeeping;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class CustomActionBarActivity  extends ActionBarActivity{
	String user;
	String password;
	

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent nextScreen;
	    switch (item.getItemId()) {
	    case R.id.action_settings:	    	
			  nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
	         startActivity(nextScreen);	
	    	break;
	    	
	    case R.id.action_providers:	    	
			  nextScreen = new Intent(getApplicationContext(), ProviderActivity.class);
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);	
	    	break;
	    case R.id.action_expenses: 	
			  nextScreen = new Intent(getApplicationContext(), NameExpensesActivity.class);
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);		    	
	    	
	    	break;
	    case R.id.action_active_payments: 	
			  nextScreen = new Intent(getApplicationContext(), ActivePaymentsActivity.class);
 	         nextScreen.putExtra("user", user);
 	         nextScreen.putExtra("password", password);
	         startActivity(nextScreen);		    	
	    	
	    	break;
	    default:
	    	break;
	    	
	    }

		return true;
	}
}
