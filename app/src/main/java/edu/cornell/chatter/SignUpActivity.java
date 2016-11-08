package edu.cornell.chatter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = new Intent();
        String email = intent.getStringExtra(SignInActivity.EXTRA_EMAIL);

        mEmail = (EditText) findViewById(R.id.signup_email);
        mEmail.setText(email);
        mSignUpButton = (Button) findViewById(R.id.signup_button);

        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_button:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
        }
    }
}
