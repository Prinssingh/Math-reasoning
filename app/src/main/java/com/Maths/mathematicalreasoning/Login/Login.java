package com.Maths.mathematicalreasoning.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Maths.mathematicalreasoning.DashBoard;
import com.Maths.mathematicalreasoning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class Login extends Fragment implements View.OnClickListener {
    Button LoginBtn;
    TextView Skip,forget,register;
    EditText email,password;
    private FirebaseAuth mAuth;
    private static String PreEmail;
    SharedPreferences sp;
    SharedPreferences.Editor editor;



    public static Login newInstance() {
        return new Login();
    }
    public static Login newInstance(String email) {
        PreEmail=email;
        return new Login();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                      @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.login_page, container, false);

        register= root.findViewById(R.id.register);
        register.setOnClickListener(this);

        forget =root.findViewById(R.id.forget);
        forget.setOnClickListener(this);

        Skip =root.findViewById(R.id.Skip);
        Skip.setOnClickListener(this);

        LoginBtn =root.findViewById(R.id.Login);
        LoginBtn.setOnClickListener(this);

        email =root.findViewById(R.id.name);
        password = root.findViewById(R.id.confirmpwd);

        if(PreEmail!=null){
            email.setText(PreEmail);
        }

        mAuth = FirebaseAuth.getInstance();
        sp=requireContext().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        return root;

}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                ChangeFragment(Register.newInstance());
                 break;

            case R.id.Skip:
                Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                startActivity(homeintent);
                requireActivity().finish();

            case R.id.forget:
                ChangeFragment(ForgetPassword.newInstance());
                break;

            case R.id.Login:
                // Make Validations
                //if(TextUtils.isEmpty(email.getText().toString()))
                LoginToApp();
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

    public void LoginToApp(){
        String EmailId=email.getText().toString();
        String Password =password.getText().toString();

        mAuth.signInWithEmailAndPassword(EmailId,Password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.w("Sign in Success", task.getException());
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("Register", "Success :");
                    Log.d("UID",""+user.getUid());

                    //Setup Data
                    editor.putBoolean("Login",true).commit();
                    editor.putString("User_Name",user.getDisplayName()).commit();
                    editor.putString("User_Email",user.getEmail()).commit();
                    editor.putString("User_UID",user.getUid()).commit();
                    editor.putBoolean("Sync_Periodically",true).commit();

                    Toast.makeText(getContext(),"Login Success!!",Toast.LENGTH_LONG).show();

                    // Goto Dash Board Activity

                    Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                    startActivity(homeintent);
                    requireActivity().finish();

                } else {
                    Log.w("Sign in failed", task.getException());
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }catch (FirebaseAuthInvalidUserException e){
                        //Email Not found
                        Toast.makeText(getContext(),"Email Not Found try Login!!",Toast.LENGTH_LONG).show();


                    }catch (FirebaseAuthInvalidCredentialsException e){
                        //Password miss Match
                        Toast.makeText(getContext(),"Password Miss , try Reset Password",Toast.LENGTH_LONG).show();

                    }catch (Exception e) {
                        Toast.makeText(getContext(),"Exception"+e,Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


    }

}