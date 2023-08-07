package com.tiet.campusfoodadmin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {
    private View rootView;
    private EditText firstName,lastName;
    private Button register;
    private ProgressBar signupProgressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sign_up_fragment, container, false);

        signupProgressBar=rootView.findViewById(R.id.signup_progres_bar);
        signupProgressBar.setVisibility(View.GONE);
        firstName=rootView.findViewById(R.id.first_name);
        lastName=rootView.findViewById(R.id.last_name);
        register=rootView.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=firstName.getText().toString();
                if(fname.length()==0){
                    firstName.setError("First Name is required");
                    return;
                }
                String lname=lastName.getText().toString();
                if(lname.length()==0){
                    lastName.setError("First Name is required");
                    return;
                }
                if(buttonClickListener!=null)buttonClickListener.register(fname,lname);
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
        void register(String fName,String lName);

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

    public void showSignupProgressBar(){
        if(signupProgressBar.getVisibility()==View.GONE) signupProgressBar.setVisibility(View.VISIBLE);
    }
    public void hideSignupProgressBar(){
        if(signupProgressBar.getVisibility()==View.VISIBLE)signupProgressBar.setVisibility(View.GONE);
    }
}