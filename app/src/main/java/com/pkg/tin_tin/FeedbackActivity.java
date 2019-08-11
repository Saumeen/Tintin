package com.pkg.tin_tin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {
    EditText subject,description;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        subject = findViewById(R.id.subject);
        description = findViewById(R.id.desc);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "tintinapplication@gmail.com" });
                Email.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
                Email.putExtra(Intent.EXTRA_TEXT,description.getText() + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });
    }
}
