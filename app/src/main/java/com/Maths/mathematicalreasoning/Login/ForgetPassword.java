
package com.Maths.mathematicalreasoning.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Maths.mathematicalreasoning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;



public class ForgetPassword extends Fragment implements View.OnClickListener {
    EditText email;
    Button   ResetPassword;
    private FirebaseAuth mAuth;

    public static ForgetPassword newInstance() {
        return new ForgetPassword();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.password_reset_page, container, false);

        email=root.findViewById(R.id.confirmpwd);
        ResetPassword =root.findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        return root;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.register){
            mAuth.sendPasswordResetEmail(email.getText().toString())
            .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Email send",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Email send Failed"+task.getException(),Toast.LENGTH_LONG).show();

                    }

                }
            });

        }
    }
}