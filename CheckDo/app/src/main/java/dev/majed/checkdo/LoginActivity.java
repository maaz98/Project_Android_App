package dev.majed.checkdo;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    Button mEmailSignInButton;
    Button forgotPassWord;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private String emailStr;
    private String passwordStr;
    public static Context ctxx;
    ProgressDialog pDialog;
    public static  String EMAIL;
    public static  String PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        ctxx=getApplicationContext();
        MainData mainData = new MainData(getApplicationContext());

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("data");
         mDatabaseReference.child("email").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
               EMAIL =  dataSnapshot.getValue().toString();
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

         mDatabaseReference.child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PASSWORD =  dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
 /*       final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                emailStr = dataSnapshot.child("email").getValue().toString();
                passwordStr = dataSnapshot.child("password").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        emailStr = EMAIL;
        passwordStr = PASSWORD;
        // Configure SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build Sign in
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
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
                pDialog.show();
               signIn();
              //  logInWithGoogle();
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
                    Mail sm = new Mail(v.getContext(), email, "Password Recovery", "Your Password is : "+Password,emailStr,passwordStr);

                     sm.execute();
                }


            }

        });

      /* mEmailView.setText("a@q.com");
        mPasswordView.setText("123456789");
        mEmailSignInButton.performClick();
*/
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //  showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            saveLoggedInData(user.getDisplayName(),user.getEmail());
                            redirectAfterLogin();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        pDialog.dismiss();
                    }


                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private  void signOut() {
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }



    private void updateUI(FirebaseUser user) {
        //   hideProgressDialog();
      /*  if (user != null) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }







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
        intent.putExtra("keyTo","firstOpen");
        startActivity(intent);
    }

}

