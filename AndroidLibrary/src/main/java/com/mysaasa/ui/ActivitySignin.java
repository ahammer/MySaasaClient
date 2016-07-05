package com.mysaasa.ui;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mysaasa.MySaasaApplication;
import com.mysaasa.R;
import com.mysaasa.api.model.Category;
import com.mysaasa.api.responses.LoginUserResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by administrator on 2014-06-30.
 */
public class ActivitySignin extends SideNavigationCompatibleActivity {

    EditText username;
    EditText password;
    EditText password_repeat;
    CheckBox rememberMe;
    Button signin;
    Button createAccount;
    private Crouton crouton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        /*
        if (getService().authenticationManager.isSignedIn()) {
            Toast.makeText(this, "Already signed in as: " + getService().authenticationManager.getIdentifier(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        */
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password_repeat = (EditText) findViewById(R.id.password_repeat);
        signin = (Button) findViewById(R.id.button_login);
        createAccount = (Button) findViewById(R.id.button_create_account);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);

        signin.setOnClickListener(view -> getService().getAuthenticationManager().login(username.getText().toString(), password.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse
                ,this::onError));

        createAccount.setOnClickListener(view -> {
            if (crouton != null) Crouton.hide(crouton);
            if (password.getText().toString().equals(password_repeat.getText().toString())) {
                crouton = Crouton.makeText(ActivitySignin.this, "Creating account", Style.INFO);
                crouton.show();
                MySaasaApplication.getService().getAuthenticationManager().createAccount(username.getText().toString(), password.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ActivitySignin.this::handleResponse,
                        ActivitySignin.this::onError
                );
            } else {
                crouton = Crouton.makeText(ActivitySignin.this, "Passwords do not match " + password_repeat.getText().toString() + " " + password.getText().toString(), Style.ALERT);
                crouton.show();
            }
        });
    }

    private void onError(Throwable throwable) {
        Crouton.makeText(ActivitySignin.this, "Error signing in "+throwable.getMessage(),Style.ALERT).show();
    }

    private void handleResponse(LoginUserResponse loginUserResponse) {
        if (loginUserResponse.isSuccess()) {
            if (rememberMe.isChecked()) {
                MySaasaApplication.getInstance().saveCredentials(username.getText().toString(), password.getText().toString());
            }
            setResult(RESULT_OK);
            finish();
        } else {
            Crouton.makeText(ActivitySignin.this, "Got a result: "+loginUserResponse.getMessage(), Style.ALERT).show();
        }
    }

    @Override
    protected void categoryChanged(Category c) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Do nothing
        return true;
    }

}
