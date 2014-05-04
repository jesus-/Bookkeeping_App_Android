package com.example.shopbookeeping;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExpenseArrayAdapter extends ArrayAdapter<ExpenseRow>{
	  List <ExpenseRow> expenses;
	  Context context;	
	  NumberFormat currencyFormat;
	 public ExpenseArrayAdapter(Context context, List <ExpenseRow> expenses) {
		    super(context, R.layout.payment_row, expenses);
		    this.expenses = expenses;
		    this.context = context;
			 Locale locale = new Locale("es","ES");
			 currencyFormat = NumberFormat.getCurrencyInstance(locale);		    
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
		    View rowView = inflater.inflate(R.layout.expense_row, parent, false);
		    
		    TextView tvdate = (TextView) rowView.findViewById(R.id.first_column);
		    TextView tvname =	(TextView) rowView.findViewById(R.id.second_column);	
		    TextView tvamount = (TextView) rowView.findViewById(R.id.third_column);
		    
		    ExpenseRow expenseRow = expenses.get(position);
		    tvdate.setText(expenseRow.getDate());
		    tvname.setText(expenseRow.getName_expense());
		    tvamount.setText(getFormat(expenseRow.getAmount()));

		    return rowView;
		  }
			public String getFormat(String a){
				return currencyFormat.format(Double.valueOf(a));

				//return String.format("%1$,.2f", Double.valueOf(a));		
			}

}
