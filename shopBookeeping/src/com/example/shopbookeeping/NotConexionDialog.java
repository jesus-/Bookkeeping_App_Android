package com.example.shopbookeeping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class NotConexionDialog {
	
	Context context;
	static boolean running=false;
	
	public  NotConexionDialog(Context context){
		this.context = context;
	}
	
	public void showNoConexionDialog(){
		if(!running){
			running=true;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("There is a problem with your internet conexion, I am sorry")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   	if (context.getClass().getSimpleName().compareTo("MainActivity")!=0){
			        		 Intent next_screen = new Intent(context,MainActivity.class);
	
			                 next_screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			                 context.startActivity(next_screen);	
			        	   	}
			        	   	running =false;
			        	   	dialog.dismiss();

			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}
