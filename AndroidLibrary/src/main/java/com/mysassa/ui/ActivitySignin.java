package com.mysassa.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mysassa.MySaasaAndroidApplication;
import com.mysassa.R;
import com.mysassa.api.messages.SigninMessage;
import com.mysassa.api.model.Category;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

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

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getService().login(username.getText().toString(), password.getText().toString());

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (crouton != null) Crouton.hide(crouton);

                if (password.getText().toString().equals(password_repeat.getText().toString())) {
                    crouton = Crouton.makeText(ActivitySignin.this, "Creating account", Style.INFO);
                    crouton.show();
                    getService().createUser(username.getText().toString(), password.getText().toString());
                } else {
                    crouton = Crouton.makeText(ActivitySignin.this, "Passwords do not match " + password_repeat.getText().toString() + " " + password.getText().toString(), Style.ALERT);
                    crouton.show();
                }
            }
        });

    }

    @Override
    protected void categoryChanged(Category c) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Do nothing
        return true;
    }

    private void SigninResult(final SigninMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.isSuccess()) {
                    if (rememberMe.isChecked()) {
                        MySaasaAndroidApplication.getInstance().saveCredentials(username.getText().toString(), password.getText().toString());
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Crouton.makeText(ActivitySignin.this, "Got a result: "+message.response.getMessage(), Style.ALERT).show();
                }
            }
        });
    }
}
