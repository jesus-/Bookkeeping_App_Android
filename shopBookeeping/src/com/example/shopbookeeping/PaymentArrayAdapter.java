package com.example.shopbookeeping;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentArrayAdapter extends ArrayAdapter<PaymentRow> {
  List <PaymentRow> payments;
  NumberFormat currencyFormat;
  Context context;

	 
  public PaymentArrayAdapter(Context context, List <PaymentRow> payments) {
    super(context, R.layout.payment_row, payments);
    this.payments = payments;
    this.context = context;
	 Locale locale = new Locale("es","ES");
	 currencyFormat = NumberFormat.getCurrencyInstance(locale);    
    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.payment_row, parent, false);
    
    TextView tvdate = (TextView) rowView.findViewById(R.id.first_column);
    TextView tvprovider =	(TextView) rowView.findViewById(R.id.second_column);	
    TextView tvstate = (TextView) rowView.findViewById(R.id.third_column);
    TextView tvamount =(TextView) rowView.findViewById(R.id.fourth_column);
    
    PaymentRow paymentRow = payments.get(position);
    tvdate.setText(paymentRow.getDate());
    tvprovider.setText(paymentRow.getProvider());
    tvamount.setText(getFormat(paymentRow.getAmount()));
    if (paymentRow.isState())
    	tvstate.setText("NotPaid");
    else
    	tvstate.setText("Paid");

    return rowView;
  }
	public String getFormat(String a){
		return currencyFormat.format(Double.valueOf(a));

	}
} 