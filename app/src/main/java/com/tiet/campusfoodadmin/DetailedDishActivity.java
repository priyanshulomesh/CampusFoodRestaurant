package com.tiet.campusfoodadmin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailedDishActivity extends AppCompatActivity {

    TextView detailDishName,detailDishDescription,detailDishPrice;
    ImageView detailDishImage;

    FloatingActionButton deleteDish;
    String key="";
    String imageUrl="";
    FirebaseFirestore db;
    String phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_dish);

        //hooks
        detailDishName=findViewById(R.id.detail_dish_name);
        detailDishPrice=findViewById(R.id.detail_dish_price);
        detailDishDescription=findViewById(R.id.detail_dish_description);
        detailDishImage=findViewById(R.id.detail_dish_image);
        deleteDish=findViewById(R.id.delete_dish_button);
        db=FirebaseFirestore.getInstance();
        phno= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        Bundle bundle=getIntent().getExtras();//from adapter
        if(bundle!=null)
        {
            detailDishName.setText(bundle.getString("Name"));
            detailDishDescription.setText(bundle.getString("Description"));
            detailDishPrice.setText(bundle.getString("Price"));
            key=bundle.getString("Name");
            imageUrl= bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailDishImage);
        }

        deleteDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDishFromFireBase(key);
            }
        });
    }
    private void deleteDishFromFireBase(String key)
    {
        StorageReference storage=FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("restraunts/"+phno+"/food").whereEqualTo("name",key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()&&!task.getResult().isEmpty())
                        {
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID= documentSnapshot.getId();
                            db.collection("restraunts/"+phno+"/food").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DetailedDishActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailedDishActivity.this, "NOt deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(DetailedDishActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}