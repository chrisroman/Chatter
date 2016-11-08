package edu.cornell.chatter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.provider.GoogleProvider;
import com.firebase.ui.auth.provider.TwitterProvider;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterAuthException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 20;
    private static String TAG = "SignInActivity";
    public final static String EXTRA_EMAIL = "edu.cornell.chatter.Email";

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.google_button)
    Button mGoogleButton;

    @BindView(R.id.twitter_button)
    Button mTwitterButton;

    @BindView(R.id.login_sign_up)
    TextView mSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mSignUpLink.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mTwitterButton.setOnClickListener(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            startActivity(new Intent(this, HomeActivity.class)
                            .putExtra("my_token", idpResponse.getIdpToken()));
            return;
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            showSnackbar(R.string.no_internet_connection);
            return;
        }

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_sign_up:
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.putExtra(EXTRA_EMAIL, mEmail.getText().toString());
                startActivity(intent);
                break;
            case R.id.google_button:
                GoogleProvider gp = new GoogleProvider(this, new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
                gp.startLogin(this);
                break;
            case R.id.twitter_button:
                TwitterProvider tp = new TwitterProvider(getApplicationContext());
                if(tp != null) {
                    try { tp.startLogin(this); }
                    catch(TwitterAuthException tae) { tp.failure(tae); }
                }
                break;

        }
    }

    private void showSnackbar(int resID) {
        Snackbar.make(findViewById(R.id.sign_in_activity), resID, Snackbar.LENGTH_SHORT).show();
    }
}
