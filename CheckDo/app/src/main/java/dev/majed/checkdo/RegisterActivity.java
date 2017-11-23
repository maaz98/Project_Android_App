package dev.majed.checkdo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements Serializable {


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;


    private static HashMap<String, User> userMap;
    private static String email;
    private String password;
    private String name;

    private String emailStr;
    private String passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView = (EditText) findViewById(R.id.name);

        email = "";
        password = "";
        name = "";
        userMap = null;
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    if (!alreadyExists()) {
                        registerUser();
                    }
                }
            }
        });
    emailStr = config.EMAIL;
    passwordStr = config.PASSWORD;
    }

        /*final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                emailStr = dataSnapshot.child("email").getValue().toString();
                passwordStr = dataSnapshot.child("password").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private boolean validateInput() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);

        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString();
        name = mNameView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !LoginActivity.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!LoginActivity.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    private boolean alreadyExists() {
        if (userMap == null) {
            userMap = readUsersMap(getApplicationContext());
        }
        Log.i("dee", userMap.toString());

        if (userMap.containsKey(email)) {
            Toast.makeText(this, "User Already Registered!!", Toast.LENGTH_LONG).show();
            Log.i("dee", "User already registered");
            return true;
        } else {
            return false;
        }
    }

    public static HashMap<String, User> readUsersMap(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("CHECKDO", MODE_PRIVATE);

        try {
            String userMapRawString = preferences.getString("usersMap", "");
            if (userMapRawString.equals("")) {
                return new HashMap<>();
            }

            byte[] rawUserMap = Base64.decode(userMapRawString, Base64.DEFAULT);

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(rawUserMap);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            return (HashMap<String, User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e("CHECKDO", e.getMessage());
            return null;
        }
    }


    public static void storeUserMap(HashMap<String, User> userHashMap, Context mContext) {



        SharedPreferences preferences = mContext.getSharedPreferences("CHECKDO", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(userHashMap);
            String userMapRaw = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            editor.putString("usersMap", userMapRaw);
            editor.apply();
            Log.i("dee", "User map raw: " + userMapRaw);
        } catch (IOException e) {
            Log.e("CHECKDO", e.getMessage());
        }
    }

    private void registerUser() {
        userMap.put(email, new User(name, email, password));
        storeUserMap(userMap, getApplicationContext());
        Log.i("dee", "User registered successfully with: " + name + ", email: " + email + "password: " + password);
        Toast.makeText(getApplicationContext(), "User registered successfully!!!", Toast.LENGTH_SHORT).show();
        sendWelcomeMail(email);
       finish();
    }

    private void sendWelcomeMail(String emailId) {

        Mail sm = new Mail(this, emailId, "welcome", "Welcome mail",emailStr,passwordStr);

        //Executing sendmail to send email
        sm.execute();
    }
}