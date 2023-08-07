package com.tiet.campusfoodadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class YourOrders extends AppCompatActivity {
    List<YourOrdersRVModel> yourOrdersRVModelList;
    RecyclerView yourOrderRV;
    yourOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        yourOrderRV=findViewById(R.id.your_orders_rv);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(YourOrders.this,1);
        yourOrderRV.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder=new AlertDialog.Builder(YourOrders.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();

        yourOrdersRVModelList=new ArrayList<>();
        adapter=new yourOrderAdapter(yourOrdersRVModelList);
        yourOrderRV.setAdapter(adapter);

        dialog.show();
        readDataFromFireBase(dialog);
        dialog.dismiss();

    }
    public void readDataFromFireBase(AlertDialog dialog)
    {
        dialog.show();
        FirebaseFirestore.getInstance().collection("DishDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    yourOrdersRVModelList.clear();
                    for(QueryDocumentSnapshot document:task.getResult())
                    {
                        YourOrdersRVModel yourOrdersRVModel=document.toObject(YourOrdersRVModel.class);
//                        dishDetailStorage.setKey(document.getKey());
                        yourOrdersRVModelList.add(yourOrdersRVModel);
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(YourOrders.this, "Fetching failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}