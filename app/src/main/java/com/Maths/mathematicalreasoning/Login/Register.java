package com.Maths.mathematicalreasoning.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Maths.mathematicalreasoning.DashBoard;
import com.Maths.mathematicalreasoning.ImpFunctions;
import com.Maths.mathematicalreasoning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class Register extends Fragment implements View.OnClickListener {
    private static String PreEmail;
    Button register;
    TextView Skip, loginPage,message;
    EditText email,password,confirmpwd,name;
    private FirebaseAuth mAuth;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    ImpFunctions impFun;

    public static Register newInstance() {
        return new Register();
    }
    public static Register newInstance(String email) {
        PreEmail=email;
        return new Register();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_page, container, false);

        progressBar=root.findViewById(R.id.progressBar);
        message=root.findViewById(R.id.message);
        register= root.findViewById(R.id.register);
        Skip =root.findViewById(R.id.Skip);
        loginPage =root.findViewById(R.id.Login);
        email =root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmpwd =root.findViewById(R.id.confirmpwd);
        name = root.findViewById(R.id.name);
        name.requestFocus();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
             setup();
            }
        });
        return root;

    }
    @SuppressLint("CommitPrefEdits")
    public void setup(){
        register.setOnClickListener(this);
        Skip.setOnClickListener(this);
        loginPage.setOnClickListener(this);

        if(PreEmail!=null){
            email.setText(PreEmail);
            message.setVisibility(View.VISIBLE);
            message.setTextColor(Color.parseColor("#4266f5"));
            message.setText("Email Not Found!!,\nPlease Register!!");
            TextWatcher mtextWatcher =new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    message.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    message.setVisibility(View.GONE);
                }
            };
            name.addTextChangedListener(mtextWatcher);
            email.addTextChangedListener(mtextWatcher);
            confirmpwd.addTextChangedListener(mtextWatcher);
            password.addTextChangedListener(mtextWatcher);
        }

        mAuth = FirebaseAuth.getInstance();
        sp=requireContext().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        impFun =new ImpFunctions(requireContext());

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                RegisterToApp();
                break;

            case R.id.Skip:
                Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                startActivity(homeintent);
                requireActivity().finish();

            case R.id.Login:
                ChangeFragment(Login.newInstance());
                break;

        }

    }

    public void  ChangeFragment(Fragment fragment){

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,  fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void RegisterToApp(){

        if(isValidInput()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!= null){

                editor.putBoolean("Login",true).commit();
                editor.putString("User_Name",user.getDisplayName()).commit();
                editor.putString("User_Email",user.getEmail()).commit();
                editor.putString("User_UID",user.getUid()).commit();

                Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                startActivity(homeintent);
                requireActivity().finish();
            }
            else if(impFun.isConnectedToInternet()){
                progressBar.setVisibility(View.VISIBLE);
                setAllDisable();
                final String Email = email.getText().toString();
                final String Name1 = name.getText().toString();
                final String Password = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //  set an Display name to  User.
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(Name1)
                                            .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAG", "User profile updated. Set Diaplay NAme");
                                                        }
                                                    }
                                                });
                                        //Setup Data
                                        editor.putBoolean("Login",true).commit();
                                        editor.putString("User_Name",Name1).commit();
                                        editor.putString("User_Email",user.getEmail()).commit();
                                        editor.putString("User_UID",user.getUid()).commit();
                                        Toast.makeText(getContext(),"Login Success!!",Toast.LENGTH_LONG).show();
                                        // Goto Dash Board Activity
                                        progressBar.setVisibility(View.INVISIBLE);
                                        setAllEnable();

                                        Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                                        startActivity(homeintent);
                                        requireActivity().finish();

                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);
                                    setAllEnable();
                                    Log.w("TAG", "createUserWithEmail:failed", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    try
                                    {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Log.d("TAG", "onComplete: weak_password");
                                        password.setError("Weak PassWord!! Use atleast 6 character password");
                                        password.requestFocus();
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Log.d("TAG", "onComplete: malformed_email");
                                        email.setError("Please Fill Correct email address!!");
                                        email.requestFocus();
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Log.d("TAG", "onComplete: exist_email");
                                        ChangeFragment( Login.newInstance(Email));
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d("TAG", "onComplete: " + e.getMessage());
                                    }
                                }
                            }
                        });
            }
            else{
                impFun.ShowToast(getLayoutInflater(),"No Internet Connection!!",
                        "Please, Connect to  Internet for Register with Math Reasoning!!");
            }
        }
    }

    public boolean isValidInput(){
        boolean valid=true;

        if( email.getText().toString().isEmpty()){
            email.setError("You can't left email field empty!!");
            email.requestFocus();
            valid=false;
        }
        else if(name.getText().toString().isEmpty()){
            name.setError("Your can't left name field empty");
            name.requestFocus();
            valid=false;
        }
        else if(password.getText().toString().isEmpty()){
            password.setError("Your can't left Password field empty");
            password.requestFocus();
            valid=false;

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please Fill Correct email address!!");
            email.requestFocus();
            valid=false;
        }
        else if (!name.getText().toString().matches("^[A-Z a-z]+$")){
            name.setError("Enter Character's and space only !!");
            name.requestFocus();
            valid=false;
        }
        else if(password.getText().length()<6){
            password.setError("Weak PassWord!! Use atleast 6 character password");
            password.requestFocus();
            valid=false;
        }
        else if(!password.getText().toString().equals(confirmpwd.getText().toString())){
            confirmpwd.setError("Password & Confirm password are not matching!!");
            confirmpwd.requestFocus();
            valid=false;

        }

        return valid;
    }

    public void setAllDisable(){
        register.setEnabled(false);
        Skip.setEnabled(false);
        loginPage.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        confirmpwd.setEnabled(false);
        name.setEnabled(false);
    }

    public void setAllEnable(){
        register.setEnabled(true);
        Skip.setEnabled(true);
        loginPage.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);
        confirmpwd.setEnabled(true);
        name.setEnabled(true);
    }
}