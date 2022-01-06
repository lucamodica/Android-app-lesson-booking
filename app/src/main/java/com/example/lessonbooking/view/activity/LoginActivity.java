package com.example.lessonbooking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.lessonbooking.R;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.lessonbooking.connectivity.RequestManager;
import java.net.CookieHandler;
import java.net.CookieManager;


public class LoginActivity extends AppCompatActivity {

    private EditText account_field, pw_field;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Context
        ctx = getApplicationContext();

        //Account textfield
        account_field = findViewById(R.id.account);
        //Password textfield
        pw_field = findViewById(R.id.pw);

        //Setting cookie
        CookieHandler.setDefault(new CookieManager());

        //Login button
        findViewById(R.id.login_btn).setOnClickListener(v -> login("auth"));
        //Guest login button
        findViewById(R.id.guest_login_btn).setOnClickListener(v -> login("guest"));
        //CheckBox to show password in textfield
        ((CheckBox) findViewById(R.id.show_psw)).
                setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                pw_field.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance());
            }
            else {
                pw_field.setTransformationMethod(
                        PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void handleLoginResponse(JSONObject jsonResult){
        try {
            String status = jsonResult.getString("result");
            switch (status) {
                case "success":
                    String toastText = "Login avvenuto con successo! ";
                    String account = "";

                    //Intent to take the user to the MainActivity
                    Intent inte = new Intent(ctx, MainActivity.class);

                    //Retrieve the json user info and its role,
                    //to distinguish a guest from user login
                    JSONObject userLogged = jsonResult.getJSONObject("user");
                    String role = userLogged.getString("role");

                    if (!role.equals("ospite")){
                        account = userLogged.getString("account");
                        toastText += "(accesso con l'account " + account + ")";
                        System.out.println("User logged: " + account);
                    }
                    else{
                        toastText += "(accesso come ospite)";
                        System.out.println("Guest login");
                    }
                    System.out.println("Role: " + role);
                    inte.putExtra("role", role);
                    inte.putExtra("account", account);

                    Toast.makeText(ctx, toastText, Toast.LENGTH_LONG).show();
                    startActivity(inte);
                    finish();
                    break;

                case "invalid_action":
                    Toast.makeText(ctx, "Azione di login non valida!",
                            Toast.LENGTH_LONG).show();
                    break;

                case "invalid_credentials":
                    Toast.makeText(ctx, "username o password errati!",
                            Toast.LENGTH_LONG).show();
                    break;

                case "illegal_credentials":
                    Toast.makeText(ctx, "Nessun account trovato!",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
        catch (JSONException ed) {
            ed.printStackTrace();
        }
    }
    private void login(String action) {

        String url = getString(R.string.servlet_url) +
                "login?action=" + action;

        if (action.equals("auth")) {
            String account = account_field.getText().toString();
            String pw = pw_field.getText().toString();

            if (!(TextUtils.isEmpty(account) || TextUtils.isEmpty(pw))) {
                url += "&account=" + account +
                        "&password=" + pw;
            } else {
                Toast.makeText(ctx, "Inserire username e password",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        String urlReq = url;
        RequestManager.getInstance(ctx).makeRequest(Request.Method.POST,
                urlReq,
                (Response.Listener<JSONObject>) this::handleLoginResponse
        );

    }
}