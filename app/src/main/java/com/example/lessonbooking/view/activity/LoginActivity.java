package com.example.lessonbooking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lessonbooking.R;
import com.example.lessonbooking.connectivity.NetworkSingleton;

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

        //CheckBox to show password in textfield
        CheckBox ch = findViewById(R.id.showPsw);
        ch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                pw_field.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance());
            }
            else {
                pw_field.setTransformationMethod(
                        PasswordTransformationMethod.getInstance());
            }
        });

        //Login button
        Button login_button = findViewById(R.id.login);
        login_button.setOnClickListener(this::login);

        /*TODO**********************************/
        //Login ospite button
        Button login_user_button = findViewById(R.id.login_ospite);
        login_user_button.setOnClickListener(this::login);
    }

    private void handleResponse(JSONObject jsonResult)
            throws JSONException {
        String status = jsonResult.getString("result");
        switch (status) {
            case ("success"):
                Toast.makeText(ctx, "login avvenuto con successo",
                        Toast.LENGTH_LONG).show();

                JSONObject userLogged = jsonResult.getJSONObject("user");
                String account = userLogged.getString("account");
                String role = userLogged.getString("role");
                String jsessionid = jsonResult.getString("id");

                System.out.println("User logged: " + account);
                System.out.println("(role: " + role + ")");
                Intent inte = new Intent(ctx, SelectingParams.class);

                // aggiungo stringa in piu (es risultato)
                setResult(LoginActivity.RESULT_OK, inte);
                inte.putExtra("account", account);
                inte.putExtra("role", role);
                inte.putExtra("jsessionid", jsessionid);
                startActivity(inte);
                break;

            case ("invalid_credentials"):
                Toast.makeText(ctx, "username o password errati!",
                        Toast.LENGTH_LONG).show();
                break;

            case ("illegal_credentials"):
                Toast.makeText(ctx, "Nessun account trovato!",
                        Toast.LENGTH_LONG).show();
                break;
        }

    }

    public void login(View v) {

        String account = account_field.getText().toString();
        String pw = pw_field.getText().toString();

        if ( !(TextUtils.isEmpty(account) || TextUtils.isEmpty(pw)) ) {
            String url = getString(R.string.servlet_url) +
                    "login?action=auth&account=" + account + "&password=" + pw;

            JsonObjectRequest jsonReq = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    response -> {
                        try {
                            handleResponse(response);
                        } catch (JSONException ed) {
                            ed.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(ctx, "Nessuna connessione trovata",
                                Toast.LENGTH_LONG).show();
                        System.err.println(error.getMessage() + ", url= " + url);
                    }
            );

            NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonReq);
        }
        else {
            Toast.makeText(ctx, "Inserire username e password",
                    Toast.LENGTH_LONG).show();
        }
        finish();
    }

}