package com.tiet.campusfoodadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    TextView resName,resLoc;
    FloatingActionButton addDish,pendingOrder,logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        resLoc=findViewById(R.id.restaurant_loc);
        resName=findViewById(R.id.restaurant_name);
        addDish=findViewById(R.id.add_dish_button);
        pendingOrder=findViewById(R.id.pending_order_button);
        logOut=findViewById(R.id.logout_button);
        resLoc.setText(getIntent().getStringExtra("loc"));
        resName.setText(getIntent().getStringExtra("name"));
        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this, ListedDishes.class);
                startActivity(intent);

            }
        });
        pendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, YourOrders.class));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, Splash.class));
                finish();
            }
        });
    }
}