package com.tiet.campusfoodadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListedDishes extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView listedDishes;
    ArrayList<DishDetailStorage> dishDetailStorages;
    SearchView searchDish;

    dishAdapter adapter;
    String phno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_dishes);

        fab=findViewById(R.id.fab);
        listedDishes=findViewById(R.id.listed_dishes);//recycler view
        searchDish=findViewById(R.id.dish_search);//search bar
        searchDish.clearFocus();
        phno= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ListedDishes.this,1);
        listedDishes.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder=new AlertDialog.Builder(ListedDishes.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();


        dishDetailStorages=new ArrayList<>();
        adapter=new dishAdapter(ListedDishes.this,dishDetailStorages);
        listedDishes.setAdapter(adapter);

        dialog.show();
        readDataFromFireBase(dialog);
        dialog.dismiss();

        //search query
        searchDish.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDishData(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ListedDishes.this, UploadDishActivity.class);
                startActivity(intent);
            }
        });
    }
    public void readDataFromFireBase(AlertDialog dialog)
    {
        dialog.show();
        FirebaseFirestore.getInstance().collection("restraunts/"+phno+"/food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    dishDetailStorages.clear();
                    QuerySnapshot queryDocumentSnapshots=task.getResult();
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                    {
                        DishDetailStorage dishDetailStorage=new DishDetailStorage();
                        dishDetailStorage.setDishName(snapshot.getString("name"));
                        dishDetailStorage.setDishDescription(snapshot.getString("description"));
                        dishDetailStorage.setDishPrice(snapshot.getString("price"));
                        dishDetailStorage.setImage(snapshot.getString("image"));
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(ListedDishes.this, "Fetching Data failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void searchDishData(String text)
    {
        ArrayList<DishDetailStorage> searchList=new ArrayList<>();
        for(DishDetailStorage dishDetailStorage:dishDetailStorages)
        {
            if(dishDetailStorage.getDishName().toLowerCase().contains(text.toLowerCase()))
            {
                searchList.add(dishDetailStorage);
            }
        }
        adapter.searchDishData(searchList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlertDialog.Builder builder=new AlertDialog.Builder(ListedDishes.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();
        readDataFromFireBase(dialog);
    }
}