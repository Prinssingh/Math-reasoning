
package com.Maths.mathematicalreasoning.Login;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Maths.mathematicalreasoning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ForgetPassword extends Fragment implements View.OnClickListener {
    EditText email;
    Button   resetPassword;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    public static ForgetPassword newInstance() {
        return new ForgetPassword();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.password_reset_page, container, false);
        progressBar=root.findViewById(R.id.progressBar);
        email=root.findViewById(R.id.email);
        resetPassword =root.findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        return root;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.resetPassword && isValidInput()){

            progressBar.setVisibility(View.VISIBLE);
            email.setEnabled(false);
            mAuth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                email.setEnabled(true);
                                EmailsendSuccess();
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                email.setEnabled(true);
                                EmailsendFailed();
                            }

                        }
                    });

        }
    }

    public void EmailsendSuccess(){
        AlertDialog.Builder AlertSuccess;
        AlertSuccess=new AlertDialog.Builder(requireContext(),android.R.style.Theme_Translucent_NoTitleBar);

        View SuccessView= getLayoutInflater().inflate(R.layout.email_send_success,null);
        AlertSuccess.setView(SuccessView);
        Button login =SuccessView.findViewById(R.id.Login);

        final AlertDialog Success=AlertSuccess.create();
        Success.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Success.dismiss();
                ChangeFragment(Login.newInstance());
            }
        });
    }

    public void EmailsendFailed(){
        AlertDialog.Builder AlertSuccess;
        AlertSuccess=new AlertDialog.Builder(requireContext(),android.R.style.Theme_Translucent_NoTitleBar);

        View SuccessView= getLayoutInflater().inflate(R.layout.email_send_failed,null);
        AlertSuccess.setView(SuccessView);
        Button login =SuccessView.findViewById(R.id.Login);

        final AlertDialog Success=AlertSuccess.create();
        Success.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Success.dismiss();
                ChangeFragment(Login.newInstance());
            }
        });
    }

    public void  ChangeFragment(Fragment fragment){

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,  fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public boolean isValidInput(){
        boolean valid=true;
        if( email.getText().toString().isEmpty()){
            email.setError("Empty!!");
            email.requestFocus();
            valid=false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError(" Email malformed!!");
            email.requestFocus();
            valid=false;
        }

        return valid;
    }
}