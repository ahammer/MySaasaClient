package com.mysaasa.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.mysassa.R;
import com.mysassa.api.model.ContactInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Adam on 4/1/2015.
 */
public class ActivityAddress extends Activity {
    EditText name, email, homePhone, mobilePhone, address1, address2, city, province, country, postal;
    Button save;
    private ContactInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        String title = getIntent().getExtras().getString("title");
        info = (ContactInfo)getIntent().getExtras().getSerializable("info");
        if (title != null) setTitle(title);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        homePhone = (EditText)findViewById(R.id.homePhone);
        mobilePhone = (EditText)findViewById(R.id.mobilePhone);
        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        city = (EditText)findViewById(R.id.city);
        province = (EditText)findViewById(R.id.province);
        country = (EditText)findViewById(R.id.country);
        postal = (EditText)findViewById(R.id.postal);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent result = new Intent();
                    result.putExtra("resultAddress", createContactInfoFromFields());
                    ActivityAddress.this.setResult(Activity.RESULT_OK, result);
                    ActivityAddress.this.finish();
                }
            }
        });
        bindData();
    }

    private ContactInfo createContactInfoFromFields() {
        return new ContactInfo(0,name.getText().toString(),email.getText().toString(),country.getText().toString(),city.getText().toString(),province.getText().toString(),address1.getText().toString(),address2.getText().toString(),postal.getText().toString(),homePhone.getText().toString(),mobilePhone.getText().toString());
    }

    private boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(name.getText())) {
            validated = false;
            name.setError("Name Required");
        }

        if (TextUtils.isEmpty(email.getText())) {
            validated = false;
            email.setError("Email Required");
        }

        return validated;
    }

    private void bindData() {
        name.setText(info.name);
        email.setText(info.email);
        homePhone.setText(info.homePhone);
        mobilePhone.setText(info.mobilePhone);
        address1.setText(info.address1);
        address2.setText(info.address2);
        city.setText(info.city);
        province.setText(info.province);
        country.setText(info.country);
        postal.setText(info.postal);
    }


    public static void startActivity(Activity context, String title, ContactInfo info, int request_code) {
        checkNotNull(context);
        checkNotNull(title);
        checkNotNull(info);

        Intent intent = new Intent(context, ActivityAddress.class);
        intent.putExtra("title", title);
        intent.putExtra("info", info);

        context.startActivityForResult(intent,request_code);

    }


}
