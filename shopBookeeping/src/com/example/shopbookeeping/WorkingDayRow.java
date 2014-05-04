package com.example.shopbookeeping;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkingDayRow implements Parcelable {
	  private int id;
	  private  String date;
	  private  String sale;
	  private  String cash;
	  private  String change_coins;
	  
	  public  WorkingDayRow(Parcel in) {
		    
	        readFromParcel(in);
	    }	  
	public WorkingDayRow(int id, String date, String sale, String cash,
			String change_coins) {
		super();
		this.id = id;
		this.date = date;
		this.sale = sale;
		this.cash = cash;
		this.change_coins = change_coins;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getChange_coins() {
		return change_coins;
	}
	public void setChange_coins(String change_coins) {
		this.change_coins = change_coins;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(date);
		arg0.writeString(sale);
		arg0.writeString(cash);
		arg0.writeString(change_coins);
	}
    private void readFromParcel(Parcel in) {
    	 id = in.readInt();
    	date = in.readString();
    	sale =in.readString();
    	cash =in.readString();
    	change_coins = in.readString();   
    }	  
    public JSONObject getJSON() throws JSONException{
    	JSONObject jsonParam = new JSONObject();
        jsonParam.put("id",id );
        jsonParam.put("date",date );
        jsonParam.put("sale", sale);
        jsonParam.put("cash", cash );
        jsonParam.put("change_coins",change_coins );
        return jsonParam;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
    	public WorkingDayRow createFromParcel(Parcel in) {
    		return new WorkingDayRow(in); }  
    	public WorkingDayRow[] newArray(int size) 
    	{ return new WorkingDayRow[size]; } 
    	};
		  
}
