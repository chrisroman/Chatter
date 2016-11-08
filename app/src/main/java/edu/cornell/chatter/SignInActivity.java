package edu.cornell.chatter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "SignInActivity";
    public final static String EXTRA_EMAIL = "edu.cornell.chatter.Email";
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        TextView signUpLink = (TextView) findViewById(R.id.sign_up);
        signUpLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.putExtra(EXTRA_EMAIL, mEmail.getText().toString());
                startActivity(intent);
        }
    }
}
