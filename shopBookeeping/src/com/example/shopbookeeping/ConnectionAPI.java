package com.example.shopbookeeping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.shopbookeeping.ActionConnection.Action;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ConnectionAPI extends AsyncTask<ActionConnection,Void,ActionConnection>{
    private AsyncTaskCompleteListener<ActionConnection> callback;
	final static String URLServer ="http://boiling-fortress-1506.herokuapp.com/"; 
	//final static String URLServer ="http://localhost:8080"; 
//	final static  String URLServer ="http://192.168.1.93:3000"; 
	 ActionConnection information = null;
	 ArrayList<HttpURLConnection> connection = null;
     OutputStreamWriter wr = null;
     BufferedReader rd  = null;
     StringBuilder sb = null;
     String line = null;
     URL serverAddress = null;
     String userPassword = null;
     /** progress dialog to show user that the backup is processing. */
     private ProgressDialog dialog;
     /** application context. */
     Context context;
     
	ConnectionAPI(AsyncTaskCompleteListener<ActionConnection> cb, String user, String password,Context context){
        this.callback = cb;
        userPassword =  Base64.encodeToString((user+":"+password).getBytes(),false);
        this.context = context;
        dialog = new ProgressDialog(context);
        connection = new ArrayList<HttpURLConnection>();
	}

    protected void onPreExecute() {
    	if(dialog!=null){
    		this.dialog.setMessage("Progress start");
    		this.dialog.show();
    	}
    }
    protected void onPostExecute(ActionConnection result) {
    	if (dialog!=null && dialog.isShowing()) {
    		try{
    			dialog.dismiss();
    		 } catch (Exception e) {e.printStackTrace();}
        }
        callback.onTaskComplete(result);
    }  
	protected ActionConnection doInBackground(ActionConnection... params) {
		 information = params[0];	    
	     try {
	    	 information.setStatus(false);

	         if (information.getAction().equals(Action.GET)){

	        	 information.setGetJSON(getJson());
			          
	         }else if(information.getAction().equals(Action.POST)){
	        	 information.setGetJSON(postJson());
	         }else if (information.getAction().equals(Action.DELETE)){
	        		 information.setGetJSON(deleteJson());
	         }else if (information.getAction().equals(Action.PUT)){
	        	 information.setGetJSON(putJson());
	         }
	         
//	      } catch (MalformedURLException e) {
//	         e.printStackTrace();
//	         System.out.println(e.getMessage());
//	         information.setStatus(false);
//	      } catch (ProtocolException e) {
//	        e.printStackTrace();
//	         System.out.println(e.getMessage());
//	         information.setStatus(false);
//
//	      } catch (IOException e) {
//	          e.printStackTrace();
//		         System.out.println(e.getMessage());
//		         information.setStatus(false);		         
	      }
	     	catch (Exception e) {
	     	 e.printStackTrace();
	         System.out.println(e.getMessage());
	         information.setStatus(false);
	         
	      }	     
	      finally
	      {
	    	  for (HttpURLConnection connec:connection)
	    		  connec.disconnect();
	          rd = null;
	          sb = null;
	          wr = null;
	          connection = null;
	      }
	  
	     return information;
	}
	

	private JSONArray getJson()throws Exception{
	     rd  = null;
	     StringBuilder sb = null;
	     String line = null;
	     URL serverAddress = null;	   
	     JSONArray response= null;
	     HttpURLConnection connection_get = null;
	     if (information.getOnGet()!= ""){
	         serverAddress = new URL(new URL(URLServer),"/"+information.getOnGet());  
	    	 
	     }else{
		     if (information.getParameter()!= ""){
		    	 String encode = URLEncoder.encode(information.getParameter(), "UTF-8");
		         serverAddress = new URL(new URL(URLServer),"/"+information.getTableString()+"/"+encode);  
		     	 System.out.println(serverAddress.getPath());
		     }
		     else
		    	 serverAddress = new URL(new URL(URLServer),"/"+information.getTableString());  
	     }
	     connection_get= (HttpURLConnection)serverAddress.openConnection();
	     connection_get.setRequestMethod("GET");
	     connection_get.setReadTimeout(10000);
	     connection_get.setRequestProperty("Authorization", "Basic "+userPassword);	
	     connection_get.setRequestProperty("Accept", "application/json");
	     connection_get.connect();
         int status = connection_get.getResponseCode();
         if( status >= 200 && status <300 ){
        	information.setStatus(true); 
         }
         String message = connection_get.getResponseMessage();
         rd  = new BufferedReader(new InputStreamReader(connection_get.getInputStream()));
         sb = new StringBuilder();        
         while ((line = rd.readLine()) != null)
         {
             sb.append(line + '\n');
         }
         Object json = new JSONTokener(sb.toString()).nextValue();
         if (json instanceof JSONArray)
        	 response  = new JSONArray(sb.toString());	 
         else
        	 response= new JSONArray().put(new JSONObject(sb.toString()));
         
         connection.add(connection_get);
       
		return response;
	}
	private JSONArray postJson()throws Exception{
		
		HttpURLConnection connection_post = null;
	    OutputStreamWriter wr = null;
	    BufferedReader rd  = null;
	    StringBuilder sb = null;
	    String line = null;
	    URL serverAddress = null;
	    
        serverAddress = new URL(new URL(URLServer),"/"+information.getTableString());      
        connection_post = (HttpURLConnection)serverAddress.openConnection();
        connection_post.setRequestMethod("POST");
        connection_post.setReadTimeout(10000);
        connection_post.setRequestProperty("Authorization", "Basic "+userPassword);	
        connection_post.setRequestProperty("Accept", "application/json");		
        connection_post.setRequestProperty("Content-Type","application/json");   
        connection_post.setDoOutput(true);
        connection_post.connect();
        wr = new OutputStreamWriter(connection_post.getOutputStream());
        wr.write(information.getPostJSON().toString());
        wr.flush();
	    int status = connection_post.getResponseCode();
	    String message = connection_post.getResponseMessage();
	    connection.add(connection_post);
	    
	    if (status >=200 && status <300){
	    	 return getJson();
	    }    
	    
		return null;
	}	
	private JSONArray deleteJson()throws Exception{
		
		HttpURLConnection connection_delete = null;
	    URL serverAddress = null;
	    
        serverAddress = new URL(new URL(URLServer),"/"+information.getTableString()+"/"+information.getParameter());      
        connection_delete = (HttpURLConnection)serverAddress.openConnection();
        connection_delete.setRequestMethod("DELETE");
        connection_delete.setReadTimeout(10000);
        connection_delete.setRequestProperty("Authorization", "Basic "+userPassword);	
        connection_delete.setRequestProperty("Accept", "application/json");		
        connection_delete.setRequestProperty("Content-Type","application/json");   
//        connection.setDoOutput(true);
        connection_delete.connect();
        
	    int status = connection_delete.getResponseCode();
	    String message = connection_delete.getResponseMessage();
	    connection.add(connection_delete);
	    
	    if (status >=200 && status <300 && information.getOnGet().compareTo("mock")!=0){
	    	 return getJson();
	    }else if (status >=200 && status <300 && information.getOnGet().compareTo("mock")==0){
	    	information.setStatus(true); 
	    	return new JSONArray();
	    }
		return null;
	}
	private JSONArray putJson()throws Exception{
		
		HttpURLConnection connection_put = null;
	    OutputStreamWriter wr = null;
	    BufferedReader rd  = null;
	    StringBuilder sb = null;
	    String line = null;
	    URL serverAddress = null;
	    
	     if (information.getParameter()!= ""){
	    	 String encode = URLEncoder.encode(information.getParameter(), "UTF-8");
	         serverAddress = new URL(new URL(URLServer),"/"+information.getTableString()+"/"+encode);  
	     	 System.out.println(serverAddress.getPath());
	     }
	     else
	    	 serverAddress = new URL(new URL(URLServer),"/"+information.getTableString());    
	    connection_put = (HttpURLConnection)serverAddress.openConnection();
	    connection_put.setRequestMethod("PUT");
	    connection_put.setReadTimeout(10000);
	    connection_put.setRequestProperty("Authorization", "Basic "+userPassword);	
	    connection_put.setRequestProperty("Accept", "application/json");		
	    connection_put.setRequestProperty("Content-Type","application/json");   
	    connection_put.setDoOutput(true);
	    connection_put.connect();
        wr = new OutputStreamWriter(connection_put.getOutputStream());
        wr.write(information.getPostJSON().toString());
        wr.flush();
	    int status = connection_put.getResponseCode();
	    String message = connection_put.getResponseMessage();
	    
	    connection.add(connection_put);
	    
	    if (status >=200 && status <300){
	    	 return getJson();
	    }    
	    
	    
		return null;
	}	


}


