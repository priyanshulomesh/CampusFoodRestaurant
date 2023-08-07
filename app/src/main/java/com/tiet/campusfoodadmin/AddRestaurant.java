package com.tiet.campusfoodadmin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AddRestaurant extends AppCompatActivity {
EditText resName,resLoc;
Button resBtn;
ImageView resImage;
FirebaseStorage storage;
FirebaseAuth auth;
String phno;

Uri uri;
FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        resName = findViewById(R.id.res_name);
        resLoc = findViewById(R.id.res_loc);
        resBtn = findViewById(R.id.add_res);
        resImage = findViewById(R.id.upload_res_image);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        phno=auth.getCurrentUser().getPhoneNumber();
        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDb();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    uri = data.getData();
                    resImage.setImageURI(uri);
                } else {
                    Toast.makeText(AddRestaurant.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        resImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }

    private void addToDb() {
        Map<String, Object> mp=new HashMap<>();
        mp.put("name",resName.getText());
        mp.put("location",resLoc.getText());
                db.collection("restraunts").document(phno).set(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddRestaurant.this, "uploading", Toast.LENGTH_SHORT).show();
//                        uploadImage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddRestaurant.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void uploadImage() {
//        StorageReference reference = storage.getReference().child("restraunts/res"+phno+"/banner.jpg");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Intent i=new Intent(AddRestaurant.this, HomePage.class);
//                i.putExtra("name",resName.getText());
//                i.putExtra("loc",resLoc.getText());
//                dialog.dismiss();
//                startActivity(i);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AddRestaurant.this, "failed to upload image", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}