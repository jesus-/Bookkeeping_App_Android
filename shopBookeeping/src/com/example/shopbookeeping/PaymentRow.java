package com.example.shopbookeeping;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


public class PaymentRow implements Parcelable{
	  private int id;
	  private  String date;
	  private  String provider;
	  private  String amount;
	  private  boolean state;
	  
	  public PaymentRow(Parcel in) {
	    
	        readFromParcel(in);
	    }
	  
	public PaymentRow(int id, String date, String provider, String amount,
			boolean state) {
		super();
		this.id = id;
		this.date = date;
		this.provider = provider;
		this.amount = amount;
		this.state = state;
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
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(date);
		arg0.writeString(provider);
		arg0.writeString(amount);
		arg0.writeByte((byte) (state ? 1 : 0)); 
	}
    private void readFromParcel(Parcel in) {
    	 id = in.readInt();
    	date = in.readString();
    	provider =in.readString();
    	amount =in.readString();
    	state = in.readByte() != 0;    
    }	  
    public JSONObject getJSON() throws JSONException{
    	JSONObject jsonParam = new JSONObject();
        jsonParam.put("id",id );
        jsonParam.put("date",date );
        jsonParam.put("provider", provider);
        jsonParam.put("amount", amount );
        jsonParam.put("state",state );
        return jsonParam;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
    	public PaymentRow createFromParcel(Parcel in) {
    		return new PaymentRow(in); }  
    	public PaymentRow[] newArray(int size) 
    	{ return new PaymentRow[size]; } 
    	};
	
}
