package com.tiet.campusfoodadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity implements LoginFragment.OnButtonClickListener,OtpFragment.OnButtonClickListener,
        SignupFragment.OnButtonClickListener{


    private String verificationIdBySystem;
    private String phno;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private  OtpFragment otpFragment;
    private SignupFragment signupFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //to hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        // Check if user is signed in (non-null) then dont login again.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            this.phno=currentUser.getPhoneNumber();
            moveToDashBoard(true);
            return;
        }

        if(savedInstanceState==null){
            changeFragmentToLoginFragment();
        }
    }

    public void changeFragmentToLoginFragment(){
        // Create a new instance of your Fragment
        LoginFragment fragment = new LoginFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a transaction to add the Fragment to the container
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the Fragment to the container using the fragment_container FrameLayout
        fragmentTransaction.add(R.id.login_container, fragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
    @Override
    public void changeFragmentToOtpFragment(String phno) {
        this.phno="+91"+phno;
        // Create a new instance of your Fragment
        otpFragment = new OtpFragment(phno);
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a transaction to add the Fragment to the container
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Set Animations
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right,R.anim.slide_in_left,R.anim.slide_out_right);

        // Add the Fragment to the container using the fragment_container FrameLayout
        fragmentTransaction.replace(R.id.login_container, otpFragment);

        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();



        startVerification(false);
    }


    public void changeFragmentToSignupFragment(){
        signupFragment=new SignupFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a transaction to add the Fragment to the container
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Set Animations
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right,R.anim.slide_in_left,R.anim.slide_out_right);

        // Add the Fragment to the container using the fragment_container FrameLayout
        fragmentTransaction.replace(R.id.login_container, signupFragment);

//        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();

    }

    public void startVerification(boolean isResend){
        Long TIMEOUT= 30L;
        int duration=30;
        otpFragment.startTimer(duration);
        PhoneAuthOptions.Builder builder=PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phno)
                .setTimeout(TIMEOUT, TimeUnit.SECONDS)
                .setActivity(LoginPage.this)
                .setCallbacks(callbacks);

        if(isResend){
//            We are resending otp so use resending token
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendToken).build());
        }else {
//            First time sending otp so use new token
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    // Set up the callbacks for verification events
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // Auto-retrieval or instant verification of the OTP completed.
            otpFragment.hideOtpProgressBar();
            Toast.makeText(LoginPage.this, "Verification Complete", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
//             Verification failed. Handle the error.
            otpFragment.hideOtpProgressBar();
            Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//             Code sent successfully. Save the verification ID for later use.
            otpFragment.hideOtpProgressBar();
            verificationIdBySystem = verificationId;
            resendToken=token;
            Toast.makeText(LoginPage.this, "OTP Sent", Toast.LENGTH_SHORT).show();

        }
    };


    //    Sign In to your account here
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        otpFragment.showOtpProgressBar();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                             Phone number authentication is successful.
//                            otpFragment.hideOtpProgressBar();
                            moveToDashBoard(false);
                        } else {
//                            otpFragment.hideOtpProgressBar();
                            Toast.makeText(LoginPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                             Phone number authentication failed.
                        }
                    }
                });
    }


    //    When otp not auto detected verify using button
    private void verifyCode(String code) {
        if (code == null || TextUtils.isEmpty(code)||verificationIdBySystem==null) {
            Toast.makeText(LoginPage.this, "Sending otp wait", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIdBySystem, code);
            signInWithPhoneAuthCredential(credential);
        }
        catch (Exception e){
            Toast.makeText(LoginPage.this, code, Toast.LENGTH_SHORT).show();
        }
    }



    public void moveToDashBoard(boolean previousLogin) {


        db.collection("restraunts").document(phno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
//                    Task successful means that fetching data successful, not that successfully found user
                    DocumentSnapshot document= task.getResult();
                    if(document.exists()){
//                        If user already exists go to dashboard
                        Intent i=new Intent(LoginPage.this,HomePage.class);
                        startActivity(i);
                        // Apply custom animation (slide from right to left for the new activity and slide to the left for the current activity)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        otpFragment.stopTimer();
                        finish();

                    }else{
//                        User Unregistered, go to registration page if came from current time app open,else go to login fragment
                        if(previousLogin) {
//                            Logout from previous session
                            firebaseAuth.signOut();

                            Intent i=new Intent(LoginPage.this,AddRestaurant.class);
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(LoginPage.this, AddRestaurant.class);
                            startActivity(i);
                        }

                    }
                }else{
//                    Fething data unsuccessfull
                    Toast.makeText(LoginPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });





    }

    //    Verify function from otp_fragment
    @Override
    public void verify(String otp){
        verifyCode(otp);

    }
    @Override
    public void resend(){
        startVerification(true);
    }
    @Override
    public void register(String fname,String lname){
        Map<String , Object> detail=new HashMap<>();
        detail.put("firstName",fname);
        detail.put("lastName",lname);
        db.collection("restraunts").document(phno).
                set(detail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Move to dashboard page
                        Toast.makeText(LoginPage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginPage.this,AddRestaurant.class);
                        startActivity(i);
                        // Apply custom animation (slide from right to left for the new activity and slide to the left for the current activity)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        otpFragment.stopTimer();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //some error occurred
                        Toast.makeText(LoginPage.this, "Failed to register:(", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}