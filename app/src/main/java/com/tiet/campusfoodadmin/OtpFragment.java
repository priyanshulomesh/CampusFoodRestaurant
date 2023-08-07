package com.tiet.campusfoodadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;

public class OtpFragment extends Fragment {
    private String phno;
    private String verificationId;

    private FirebaseAuth firebaseAuth;
    private View rootView;
    private static final int TIMER_DURATION = 60; // Timer duration in seconds
    private CountDownTimer countDownTimer;
    Button resendOtp;
    ProgressBar otpProgressBar;
    public OtpFragment(String phno){
        this.phno=phno;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.otp_fragment,container,false);
        firebaseAuth=FirebaseAuth.getInstance();
        PinView otpBox=rootView.findViewById(R.id.otpBox);
        otpProgressBar=rootView.findViewById(R.id.otp_progres_bar);
        resendOtp =rootView.findViewById(R.id.resend_otp);
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendOtp.isClickable()){
                    buttonClickListener.resend();
                }
            }
        });



        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button verify= rootView.findViewById(R.id.verify_otp);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode = otpBox.getText().toString().trim();
                if (!TextUtils.isEmpty(otpCode)) {
                    if(buttonClickListener!=null)buttonClickListener.verify(otpCode);
                }
            }
        });

//        startVerification();



        return rootView;
    }



    //   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
    // Define the interface for the callback,so that abstract method is declared in activity_login ,for making call from fragment to parent
    public interface OnButtonClickListener {
        void verify(String otp);
        void resend();

    }

    private OnButtonClickListener buttonClickListener;//object of above interface

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//         Check if the hosting Activity implements the interface
        if (context instanceof LoginFragment.OnButtonClickListener) {
//            check if object is instantiated from parent(activity login),then copy object value of the interface from parent to fragment(current)
            buttonClickListener = (OnButtonClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OtpFragment.OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the listener to avoid potential memory leaks
        buttonClickListener = null;
    }

    //    --------------------------------------------------------Timer-------------------------------------------------------------------------
    public void startTimer(int durationInSeconds) {
        countDownTimer = new CountDownTimer(durationInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the UI with the remaining time
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                updateTimerUI(secondsRemaining);
            }

            @Override
            public void onFinish() {
                // Timer finished, update the UI accordingly (e.g., enable the resend button)
                updateTimerUI(0);
            }
        };
        countDownTimer.start();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void updateTimerUI(int secondsRemaining) {

        if (secondsRemaining > 0) {
            // Timer still running, disable the resend button and show the remaining time
            resendOtp.setEnabled(false);
            resendOtp.setText("Resend in "+secondsRemaining+" seconds");
        } else {
            // Timer finished, enable the resend button and hide the remaining time
            stopTimer();
            resendOtp.setEnabled(true);
            resendOtp.setText("Resend");
        }
    }

    public void showOtpProgressBar(){
        if(otpProgressBar.getVisibility()==View.GONE) otpProgressBar.setVisibility(View.VISIBLE);
    }
    public void hideOtpProgressBar(){
        if(otpProgressBar.getVisibility()==View.VISIBLE)otpProgressBar.setVisibility(View.GONE);
    }
}