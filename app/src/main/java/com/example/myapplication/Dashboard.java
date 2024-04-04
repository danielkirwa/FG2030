package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
private DrawerLayout drawerLayout;
NavigationView navigationView;
Button openTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbaord);

        openTransaction = findViewById(R.id.btn_open_transaction);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_account) {
                    Intent openAccountIntent = new Intent(Dashboard.this, Account.class);
                    startActivity(openAccountIntent);
                    return true;
                } else if (itemId == R.id.nav_lending) {
                    Intent openLending = new Intent(Dashboard.this, Lending.class);
                    startActivity(openLending);
                    return true;
                }
                else if (itemId == R.id.nav_saving) {
                    Intent openInvestment = new Intent(Dashboard.this, Beneficiary.class);
                    startActivity(openInvestment);
                    return true;
                }
                else if (itemId == R.id.nav_table_banking) {
                    Intent openTBanking = new Intent(Dashboard.this, TBanking.class);
                    startActivity(openTBanking);
                    return true;
                }

                return true;
            }
        });


        // open transactions
        openTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenTransaction = new Intent(Dashboard.this,Transaction.class);
                startActivity(intentOpenTransaction);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


}