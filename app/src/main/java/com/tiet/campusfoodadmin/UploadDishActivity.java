package com.tiet.campusfoodadmin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadDishActivity extends AppCompatActivity {

    ImageView imageView;
    Button addButton;
    EditText dishName, dishDescription, dishPrice;
    ActivityResultLauncher<String> launcher;
    FirebaseStorage storage;
    FirebaseFirestore db;
    String imageURL;
    Uri uri;
    String dishname ;
    String dishdescription;
    String dishprice,phno;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dish);

        imageView = findViewById(R.id.upload_dish_image);
        addButton = findViewById(R.id.add_dish);
        dishName = findViewById(R.id.dish_name);
        dishDescription = findViewById(R.id.dish_description);
        dishPrice = findViewById(R.id.dish_price);
        storage = FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();
        phno= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        //gallery intent to pick image and set to image view
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    uri = data.getData();
                    imageView.setImageURI(uri);
                } else {
                    Toast.makeText(UploadDishActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //launch gallery intent - photo picker
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //add to the database and storage
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishname= dishName.getText().toString();
                dishprice= dishPrice.getText().toString();
                dishdescription= dishDescription.getText().toString();
                if(dishname.isEmpty())
                {
                    dishName.setError("This Field is required");
                    return;
                }
                if(dishdescription.isEmpty())
                {
                    dishDescription.setError("This Field is required");
                    return;
                }
                if(dishprice.isEmpty())
                {
                    dishPrice.setError("This Field is required");
                    return;
                }


                StorageReference reference = storage.getReference().child("restraunts/res"+phno+"/food"+make_key(dishname)+".jpg");

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadDishActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlImage = uriTask.getResult();
                        imageURL = urlImage.toString();
                        uploadData();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void uploadData() {

        Map<String,Object> mp=new HashMap<>();
        mp.put("name",dishname);
        mp.put("description",dishdescription);
        mp.put("price",dishprice);
        mp.put("image",imageURL);

        db.collection("restraunts/"+phno+"/food").document(make_key(dishname)).set(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadDishActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDishActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String make_key(String name) {
        name=name.toLowerCase();
        String ans="";
        for(int i=0;i<name.length();++i){
            if(name.charAt(i)!=' ')ans+=name.charAt(i);
            else ans+="_";
        }
        return ans;
    }
}
//                Intent intent=new Intent(MainActivity.this,ListedDishes.class);
//                intent.putExtra("intentdata",);
//                startActivity(intent);


//                StorageReference dishnameReference = reference.child("dishName");
//                byte[] dishName = dishname.getBytes();
//                dishnameReference.putBytes(dishName).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        flag[1]=true;
//                    }
//                });
//
//
//                StorageReference dishdescriptionReference = reference.child("dishDescription");
//                byte[] dishDescription = dishdescription.getBytes();
//                dishdescriptionReference.putBytes(dishDescription)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        flag[2]=true;
//                    }
//                });
//
//
//                StorageReference dishpriceReference = reference.child("dishPrice");
//                byte[] dishPrice = dishprice.getBytes();
//                dishpriceReference.putBytes(dishPrice)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                flag[3]=true;
//                            }
//                        });
////                boolean all=true;
////                for(int i=0;i<4;++i)
////                {
////                    if(flag[i]==false)
////                    {
////                        all=false;
////                    }
////                }
////                Toast.makeText(MainActivity.this, "uploaded"+ all, Toast.LENGTH_SHORT).show();
////                if(all) {
////                    Toast.makeText(MainActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
////                }
//            }
//        });



//    }
//}