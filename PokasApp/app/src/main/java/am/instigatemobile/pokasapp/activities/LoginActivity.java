package am.instigatemobile.pokasapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import am.instigatemobile.pokasapp.R;
import am.instigatemobile.pokasapp.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN = "am.instigatemobile.pokasapp.activities.LoginActivity.LOGIN";
    public static final String REGISTER = "am.instigatemobile.pokasapp.activities.LoginActivity.REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLoginFragment();
    }

    private void initLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new LoginFragment();
        fragmentTransaction.replace(R.id.login_frame, fragment, LOGIN);
        fragmentTransaction.commit();
    }
}

