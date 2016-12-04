package am.instigatemobile.pokasapp.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import am.instigatemobile.pokasapp.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, FirebaseAuth.AuthStateListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(this);
        initPageUI(rootView);
        return rootView;
    }

    private void initPageUI(View rootView) {
        editTextEmail = (EditText) rootView.findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) rootView.findViewById(R.id.edit_text_password);
        Button buttonSignIn = (Button) rootView.findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(this);
        Button buttonRegister = (Button) rootView.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
    }

    private void showRegisterFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new RegisterFragment();
        fragmentTransaction.replace(R.id.login_frame, fragment, LoginActivity.REGISTER);
        fragmentTransaction.commit();
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Sign in Please Wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Successfully sign in", Toast.LENGTH_LONG).show();
                            startHomeActivity();
                        } else {
                            Toast.makeText(getActivity(), "Sign in Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in: {
                loginUser();
                break;
            }
            case R.id.button_register: {
                showRegisterFragment();
                break;
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            startHomeActivity();
        }
    }
}
