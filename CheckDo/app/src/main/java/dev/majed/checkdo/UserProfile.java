package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserProfile extends AppCompatActivity {

    TextView NameText;              //declaring widgets
    TextView emailText;
    EditText password;
    Button changePassWord;
    Button DeleteAccount;
    Button Logout;
    ImageView imageView;
    private static HashMap<String, User> userMap;
    private String email;
    private String name;
    private String authType;
    String ImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        userMap=null;
        password=(EditText)findViewById(R.id.editText);
        NameText=(TextView)findViewById(R.id.Name);
        emailText=(TextView)findViewById(R.id.email);
        changePassWord=(Button)findViewById(R.id.password_button);
        DeleteAccount=(Button)findViewById(R.id.Delete_account);
        Logout=(Button)findViewById(R.id.logout_button);
        imageView=(ImageView)findViewById(R.id.imageView);
        email=getIntent().getStringExtra("email");          //fetching intent extras
        name=getIntent().getStringExtra("name");
        NameText.setText(name);
        emailText.setText(email);
        try {
            addImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(LoginActivity.mGoogleApiClient.isConnected()){
            // make update password and delete account invisible if the user is logged in through Gmail.
            changePassWord.setVisibility(View.INVISIBLE);
            DeleteAccount.setVisibility(View.INVISIBLE);

        }

        if (userMap == null) {
            userMap = readUsersMap(getApplicationContext());
        }
        changePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(changePassWord.getText()=="Update Password"){

                    String Newpassword= password.getText().toString();
                    password.setVisibility(View.INVISIBLE);
                    password.setText("");
                    changePassWord.setText("Change Password");
                    userMap.get(email).setPassword(Newpassword);        //saving updated password
                    Toast.makeText(UserProfile.this, "Password Updated successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    password.setVisibility(View.VISIBLE);
                    changePassWord.setText("Update Password");
                }
                storeUserMap(userMap, getApplicationContext());
            }
        });

        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMap.remove(email);
                storeUserMap(userMap, getApplicationContext());                     //Deleting user Account
                logout();
            }
        });
    Logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logout();
        }
    });


    }

    private void addImage() throws IOException {
        ImageUrl="";
        if(email.contains("@gmail.com")){
        String url = "http://picasaweb.google.com/data/entry/api/user/"+email.replace("@gmail.com","")+"?alt=json";
         String data = getJSON(url,100000);
        try {if(data==null){}
            else{
            JSONObject jsonObject = new JSONObject(data);
           JSONObject object = jsonObject.getJSONObject("entry").getJSONObject("gphoto$thumbnail");
            ImageUrl = object.getString("$t");

        }} catch (JSONException e) {
            e.printStackTrace();
        }

         if(ImageUrl!="") {
            Picasso.with(this).load(ImageUrl).into(imageView);
        }
    }}

    private void logout() {
        if(LoginActivity.mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(LoginActivity.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });}
        SharedPreferences.Editor editor = getSharedPreferences("CHECKDO", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        //finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
//method to read user information from phone
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
            return (HashMap<String, User>)objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            Log.e("CHECKDO", e.getMessage());
            return null;
        }
}
//method to write/update user information to phone

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
        }
        catch (IOException e) {
            Log.e("CHECKDO", e.getMessage());
        }
    }
    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
