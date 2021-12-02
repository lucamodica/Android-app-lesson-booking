package com.example.myapplication.ui.connectivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {
    private static Context context;
   private JSONObject  result ;
   CharSequence td;
    Toast t;
    EditText account,pw;
    CheckBox ch;
    int duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        result=new JSONObject();
       context= getApplicationContext();


        //TextField per l' account
        account = findViewById(R.id.account);
        // TextField per la password
        pw = findViewById(R.id.pw);
        //CheckBox
        ch =findViewById(R.id.showPsw);

        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
    public void login(View view){


        String accounts = account.getText().toString();// Stringa dell account

        String pws= pw.getText().toString();// Stringa della password
        if(accounts!=null && pws!=null&&!TextUtils.isEmpty( accounts) && !TextUtils.isEmpty( pws)){
            String url= "http://10.0.2.2:8080/demo_war_exploded/login?action=auth&account="+accounts+"&password="+pws;

            SendReq(context,url,1);
            result =getResult();
          if(result!=null){
              try {
                  String status = result.getString("result");
                    switch (status){
                        case("success"):
                            td="login avvenuto con successo";// testo
                            duration = Toast.LENGTH_LONG;//durata
                            t=  Toast.makeText(login.context,td,duration);
                            t.show();
                            String d=result.getJSONObject("user").getString("account");
                            String c=result.getJSONObject("user").getString("role");
                            String b=result.getJSONObject("user").getString("password");
                            System.out.println(d+b+c);
                            Intent inte = new Intent(this, selectingparams.class);

                            // aggiungo stringa in piu (es risultato)
                            setResult(login.RESULT_OK, inte);
                            startActivity(inte);
                            break;
                        case("invalid_credentials"):
                             td="username o password errati!";// testo
                          duration = Toast.LENGTH_LONG;//durata
                           t=  Toast.makeText(login.context,td,duration);
                            t.show();
                            break;
                        case("illegal_credentials"):
                            td="Nessun account trovato!";// testo
                           duration = Toast.LENGTH_LONG;//durata
                            t=  Toast.makeText(login.context,td,duration);
                            t.show();
                                 break;
                    }
              }catch(JSONException ed){
                ed.printStackTrace();

              }
          }
          else{
              td="login fallito controlla la connessione internet";// testo
             duration = Toast.LENGTH_LONG;//durata
              t=  Toast.makeText(login.context,td,duration);
              t.show();
          }

        }else{

            td="Inserire username e password";// testo
          duration = Toast.LENGTH_LONG;//durata
            t=  Toast.makeText(login.context,td,duration);
            t.show();
        }
    }
    public void SendReq(Context ctx, String url, int method) {
        RequestQueue requestQueue;
        NetworkActivity objNetworkActivity = new NetworkActivity(ctx);
// Instantiate the cache
        Cache cache = objNetworkActivity.cache;  // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
// Start the queue
        requestQueue.start();
        Response.Listener<JSONObject> re=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setResult(response);
                System.out.println(getResult().toString());

            }
        } ;

        Response.ErrorListener rel = error -> {
            System.err.println("TomCat non trovato");
            System.err.println(error.toString());
        };

        JsonObjectRequest result = new JsonObjectRequest(method, url, null, re, rel);
        requestQueue.add(result);

    }
    public void setResult(JSONObject jsonresult) {
        this.result = jsonresult;
    }

    public JSONObject getResult() {
        return result;
    }

}