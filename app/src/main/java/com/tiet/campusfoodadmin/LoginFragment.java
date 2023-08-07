package com.tiet.campusfoodadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.login_fragment,container,false);
        EditText phno=rootView.findViewById(R.id.phone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button getOtp= rootView.findViewById(R.id.get_otp);
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(phno.getText()).length()!=10) {
                    phno.setError("Enter a valid phone number");
                    return;
                }
                changeToOtpFragment(String.valueOf(phno.getText()));
            }
        });


        return rootView;
    }

    //   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
//   ----------------------------------------- --------------IMPORTANT--------------------------------------------------------------------------------
    // Define the interface for the callback,so that abstract method is declared in activity_login ,for making call from fragment to parent
    public interface OnButtonClickListener {
        void changeFragmentToOtpFragment(String phno);


    }

    private OnButtonClickListener buttonClickListener;//object of above interface

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Check if the hosting Activity implements the interface
        if (context instanceof OnButtonClickListener) {
//            check if object is instantiated from parent(activity login)
            buttonClickListener = (OnButtonClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SignupFragment.OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the listener to avoid potential memory leaks
        buttonClickListener = null;
    }

    // Inside your Fragment, whenever getotp is clicked, this calls the interface method implemented in parent class having frame layout
    private void changeToOtpFragment(String phno){
        if(buttonClickListener!=null)buttonClickListener.changeFragmentToOtpFragment(phno);
    }

}