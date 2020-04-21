package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Feedback extends AppCompatActivity {

    private EditText toEditText;
    private EditText subjectEditText;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toEditText = findViewById(R.id.toEditText);
        toEditText.setText("foodfromhome@gmail.com");
        subjectEditText = findViewById(R.id.subjectEditText);
        message = findViewById(R.id.messageEditText);

        Button send = findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    public void sendMail(){
        String recipient = toEditText.getText().toString();
        String subject = subjectEditText.getText().toString();
        String feedback = message.getText().toString();
        String[] to = new String[1];
        to[0] = recipient;


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,to);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,feedback);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an e-mail client"));
    }
}



