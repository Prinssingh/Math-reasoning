package com.Maths.mathematicalreasoning.Login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.Maths.mathematicalreasoning.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Login extends Fragment implements View.OnClickListener {
    Button LoginBtn;
    TextView Skip,forget,register,message;
    EditText email,password;
    private FirebaseAuth mAuth;
    private static String PreEmail;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    ImpFunctions impFun;


    public static Login newInstance() {
        return new Login();
    }
    public static Login newInstance(String email) {
        PreEmail=email;
        return new Login();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                      @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.login_page, container, false);

        progressBar=root.findViewById(R.id.progressBar);
        register= root.findViewById(R.id.register);
        forget =root.findViewById(R.id.forget);
        Skip =root.findViewById(R.id.Skip);
        LoginBtn =root.findViewById(R.id.Login);
        email =root.findViewById(R.id.email);
        email.requestFocus();
        password = root.findViewById(R.id.password);
        message=root.findViewById(R.id.message);

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
        forget.setOnClickListener(this);
        Skip.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);

        if(PreEmail!=null){
        email.setText(PreEmail);
        password.requestFocus();
        message.setVisibility(View.VISIBLE);
        message.setTextColor(Color.parseColor("#4266f5"));
        message.setText("Email already Registered,\nPlease try Login Here!");
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
        password.addTextChangedListener(mtextWatcher);
        email.addTextChangedListener(mtextWatcher);
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

    public void LoginToApp(){
        final String EmailId=email.getText().toString();
        String Password =password.getText().toString();
        if(isValidInput() && impFun.isConnectedToInternet()){
            progressBar.setVisibility(View.VISIBLE);
            setAllDisable();
            mAuth.signInWithEmailAndPassword(EmailId,Password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();

                                //Setup Data
                                assert user != null;
                                editor.putBoolean("Login",true).commit();
                                editor.putString("User_Name",user.getDisplayName()).commit();
                                editor.putString("User_Email",user.getEmail()).commit();
                                editor.putString("User_UID",user.getUid()).commit();
                                progressBar.setVisibility(View.INVISIBLE);
                                setAllEnable();

                                //Collect previous progress(Levels)

                                GetMyProgress();

                                // Goto Dash Board Activity
                                Intent homeintent = new Intent(requireActivity(), DashBoard.class);
                                startActivity(homeintent);
                                requireActivity().finish();

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                setAllEnable();
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                }catch (FirebaseAuthInvalidUserException e){
                                    //Email Not Found
                                    ChangeFragment(Register.newInstance(EmailId));

                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    //Password miss Match
                                    password.setError("Password dose't Matched!!");

                                }catch (Exception e) {
                                    Toast.makeText(getContext(),"Exception"+e,Toast.LENGTH_LONG).show();
                                }


                            }
                        }
                    });

        }
        else {
            impFun.ShowToast(getLayoutInflater(), "No Internet Connection!!",
                    "Please, Connect to  Internet for Register with Math Reasoning!!");
        }

    }

    public void  ChangeFragment(Fragment fragment){

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,  fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void GetMyProgress(){
        try{
            DatabaseReference mdbRef;

            final ProgressDialog progressDialog1 =new ProgressDialog(getContext());
            progressDialog1.setMessage("Getting Your Progress.. !!");
            progressDialog1.show();

            String key =sp.getString("User_UID","");
            mdbRef = FirebaseDatabase.getInstance().getReference().child("Users/"+key);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserData user=dataSnapshot.getValue(UserData.class);

                    try {
                        assert user != null;
                        editor.putString("User_Name", user.getName()).commit();
                        editor.putString("User_Email", user.getEmail()).commit();
                        editor.putInt("CompletedLevels", user.getLevel()).commit();
                        editor.putInt("Hint",user.getHint()).commit();
                        editor.putInt("Solution",user.getSolution()).commit();
                        editor.putLong("DT",user.getDT()).commit();
                        progressDialog1.setMessage("Done Getting Progress.. !!");
                        progressDialog1.hide();
                    }catch (Exception ignored){}

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog1.setMessage("No Progress Found");
                    progressDialog1.hide();


                }
            };
            mdbRef.addValueEventListener(postListener);

        }
        catch (Exception ignored){}

    }

    public boolean isValidInput(){
        boolean valid=true;
        if( email.getText().toString().isEmpty()){
            email.setError("Empty!!");
            email.requestFocus();
            valid=false;
        }
        else if(password.getText().toString().isEmpty()){
            password.setError("Empty!!");
            password.requestFocus();
            valid=false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError(" Email malformed!!");
            email.requestFocus();
            valid=false;
        }
        else if(password.getText().length()<6){
            password.setError("Weak PassWord!!");
            password.requestFocus();
            valid=false;
        }

        return valid;
    }

    public void setAllDisable(){
        LoginBtn.setEnabled(false);
        Skip.setEnabled(false);
        forget.setEnabled(false);
        register.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);

    }

    public void setAllEnable(){
        LoginBtn.setEnabled(true);
        Skip.setEnabled(true);
        forget.setEnabled(true);
        register.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);
    }


}