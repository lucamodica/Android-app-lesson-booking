package com.example.lessonbooking.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lessonbooking.databinding.ActivityMainBinding;
import com.example.lessonbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String account, role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get params from Intent
        Bundle b = getIntent().getExtras();
        if (b.containsKey("account") && b.containsKey("role")){
            account = getIntent().getStringExtra("account");
            role = getIntent().getStringExtra("role");
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Parametri mancanti nell'Intent!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        com.example.lessonbooking.databinding.ActivityMainBinding binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_booking, R.id.navigation_catalog)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    public String getAccount() {
        return account;
    }

    public String getRole() {
        return role;
    }
}

