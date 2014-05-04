package com.example.shopbookeeping;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActionConnection {
	enum Action {GET,POST,DELETE,PUT}
	enum Table {EXPENSES,PAYMENTS,WORKING_DAYS,NAME_EXPENSES,PROVIDERS,ACTIVE_PAYMENTS,PAYMENTS_BY_PROVIDER,
		EXPENSES_BY_NAME,WORKING_DAYS_BY_DATE,PAYMENTS_BY_DAY,EXPENSES_BY_DAY;
		public String GetURL(){
			return this.toString().toLowerCase();
		}
	}
	private Action  action;
	private Table table;
	private JSONObject postJSON;
	private JSONArray getJSON;
	private boolean status;
//	private int id; //for deletes, id to delete
	String parameter; //parameter (id, provider etc)
	String onGet; //when you make a post, what url do you want to get.
	private int numberPayments; // for payments
	
	public ActionConnection(Action action,Table table,JSONObject postJSON, String parameter,String onGet) {
		super();
		this.action = action;
		this.table = table;
		this.postJSON = postJSON;
		this.parameter = parameter;
		this.onGet = onGet;
	}
	public ActionConnection(Action action,Table table,JSONObject postJSON, String parameter) {
		super();
		this.action = action;
		this.table = table;
		this.postJSON = postJSON;
		this.parameter = parameter;
		this.onGet = "";
	}
	
	public String getOnGet() {
		return onGet;
	}
	public void setOnGet(String onGet) {
		this.onGet = onGet;
	}
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public JSONArray getGetJSON() {
		return getJSON;
	}

	public void setGetJSON(JSONArray getJSON) {
		this.getJSON = getJSON;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Table getTable() {
		return table;
	}
	public String getTableString(){
		return table.name().toLowerCase();
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public JSONObject getPostJSON() {
		return postJSON;
	}
	public void setPostJSON(JSONObject postJSON) {
		this.postJSON = postJSON;
	}

	public int getNumberPayments() {
		return numberPayments;
	}
	public void setNumberPayments(int numberPayments) {
		this.numberPayments = numberPayments;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}


	

}
