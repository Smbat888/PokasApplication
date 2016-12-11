package am.instigatemobile.pokasapp.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import am.instigatemobile.pokasapp.R;
import am.instigatemobile.pokasapp.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        initPageUI(rootView);
        return rootView;
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Successfully registered",Toast.LENGTH_LONG).show();
                            startHomeActivity();
                        }else{
                            Toast.makeText(getActivity(),"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void initPageUI(View rootView) {
        editTextEmail = (EditText) rootView.findViewById(R.id.edit_text_email_reg);
        editTextPassword = (EditText) rootView.findViewById(R.id.edit_text_password_reg);
        Button buttonRegister = (Button) rootView.findViewById(R.id.button_register_reg);
        buttonRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register_reg: {
                registerUser();
                break;
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
