package dev.majed.checkdo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    Button mEmailSignInButton;
    Button forgotPassWord;

    static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        // Configure SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build Sign in
        LoginActivity.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

         mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        final Button mSignUpButton = (Button) findViewById(R.id.register);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logInWithGoogle();
            }
        });
        forgotPassWord=(Button)findViewById(R.id.forgotPassword);
        forgotPassWord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                String email=mEmailView.getText().toString();
                HashMap<String, User> userHashMap = RegisterActivity.readUsersMap(getApplicationContext());
                if (!userHashMap.containsKey(email)) {
                    Toast.makeText(getApplicationContext(), "Sorry, you are not registered. Please create an account first !!!", Toast.LENGTH_SHORT).show();

                } else {
                    mPasswordView.setText("");
                  String Password=  userHashMap.get(email).getPassword();

                    Toast.makeText(LoginActivity.this, "Your password has been sent to your registered Email.", Toast.LENGTH_SHORT).show();
                    Mail sm = new Mail(v.getContext(), email, "Password Recovery", "Your Password is : "+Password);

                     sm.execute();
                }


            }

        });

    }

    final int GOOGLE_SIGN_IN = 119;
    private void logInWithGoogle() {
        Log.d("CHECKDO", "In log in with google method");


        //  Prompts the user to select a Google account to sign in with
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(LoginActivity.mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CHECKDO", "Got the activity result");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {
        // if sign-in completed successfully.
        if (result.isSuccess()) {
            Log.i("CHECKDO", "Google Sign in completed successfully");
            GoogleSignInAccount account = result.getSignInAccount();
            saveLoggedInData(account.getDisplayName(), account.getEmail());
            redirectAfterLogin();
        } else {
            Toast.makeText(this, "Failed signing in...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            login();
        }
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    public void login() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        HashMap<String, User> userHashMap = RegisterActivity.readUsersMap(getApplicationContext());
        if (!userHashMap.containsKey(email)) {
            Toast.makeText(getApplicationContext(), "Sorry, you are not registered. Please create an account first !!!", Toast.LENGTH_SHORT).show();
        } else if (!userHashMap.get(email).getPassword().equals(password)){
            Toast.makeText(getApplicationContext(), "Incorrect Password!!!", Toast.LENGTH_SHORT).show();
        } else {
            saveLoggedInData(userHashMap.get(email).getName(), email);
            redirectAfterLogin();
        }
    }

    private void saveLoggedInData(String name, String email) {
        SharedPreferences.Editor editor = getSharedPreferences("CHECKDO", MODE_PRIVATE).edit();
        editor.putString("loggedUser", name);
        editor.putString("loggedEmail", email);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void redirectAfterLogin() {
        Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect to Google.", Toast.LENGTH_SHORT).show();
        Log.d("CHECKDO", "Failed connection to google.");
    }

}

