package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class OTP extends AppCompatActivity {

    private EditText otpEditText;
    private TextView error;
    int OTP;
    private String orderID,name,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getDeliveryInfo();

        Bundle bundle = getIntent().getExtras();
        OTP = bundle.getInt("OTP");
        otpEditText = findViewById(R.id.otpEditText);
        error = findViewById(R.id.error);


    }

    public void submit(View view){

        if(Integer.parseInt(otpEditText.getText().toString()) == OTP)
        {
            Intent intent = new Intent(getApplicationContext(),BookingConfirmation.class);
            intent.putExtra("Info",orderID+" "+name+" "+phone);
            startActivity(intent);
            Toast.makeText(OTP.this, "Your order is Successfully Placed", Toast.LENGTH_SHORT).show();

        }
        else
        {
            error.setText("Wrong OTP");

        }


    }

    public void getDeliveryInfo(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {

                    if(objects.size()>0)
                    {
                        for(ParseObject object:objects)
                        {
                            orderID = object.getObjectId();
                            name = object.getString("DeliveryPerson");
                            phone = object.getString("DeliveryPhone");
                            break;
                        }

                    }

                }
                else
                {
                    Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
