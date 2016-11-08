package edu.cornell.chatter;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.provider.GoogleProvider;
import com.firebase.ui.auth.provider.TwitterProvider;
import com.firebase.ui.auth.ui.ExtraConstants;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterAuthException;

import java.util.ArrayList;

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

    @BindView(R.id.password_button)
    Button mLogInButton;

    @BindView(R.id.login_sign_up)
    TextView mSignUpLink;

    @BindView(R.id.unsafe_continue)
    Button mContinue;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mSignUpLink.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mTwitterButton.setOnClickListener(this);
        mLogInButton.setOnClickListener(this);
        mContinue.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
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
                new TwitterProvider(getApplicationContext()).startLogin(this);
                break;
            case R.id.password_button:
                if(isValidEmail(mEmail.getText().toString()) && isValidField(mPassword.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(getApplicationContext(), R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
                break;
            case R.id.unsafe_continue:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showSnackbar(int resID) {
        Snackbar.make(findViewById(R.id.sign_in_activity), resID, Snackbar.LENGTH_SHORT).show();
    }

    protected static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    protected static boolean isValidField(CharSequence target) {
        return (target != null && target.length() > 0);
    }
}
