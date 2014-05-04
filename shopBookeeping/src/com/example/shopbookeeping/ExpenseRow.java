package com.example.shopbookeeping;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpenseRow implements Parcelable{

	  private int id;
	  private  String date;
	  private  String name_expense;
	  private  String amount;
	  
	public ExpenseRow(Parcel in) {
		    
	        readFromParcel(in);
    }
	
	public ExpenseRow(int id, String date, String name_expense, String amount) {
		super();
		this.id = id;
		this.date = date;
		this.name_expense = name_expense;
		this.amount = amount;
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

	public String getName_expense() {
		return name_expense;
	}

	public void setName_expense(String name_expense) {
		this.name_expense = name_expense;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(date);
		arg0.writeString(name_expense);
		arg0.writeString(amount);
	}
    private void readFromParcel(Parcel in) {
    	 id = in.readInt();
    	date = in.readString();
    	name_expense =in.readString();
    	amount =in.readString();
    }	  
    public JSONObject getJSON() throws JSONException{
    	JSONObject jsonParam = new JSONObject();
        jsonParam.put("id",id );
        jsonParam.put("date",date );
        jsonParam.put("amount", amount );

        jsonParam.put("name_expense", name_expense);
        return jsonParam;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
    	public ExpenseRow createFromParcel(Parcel in) {
    		return new ExpenseRow(in); }  
    	public ExpenseRow[] newArray(int size) 
    	{ return new ExpenseRow[size]; } 
    	};
	


}
