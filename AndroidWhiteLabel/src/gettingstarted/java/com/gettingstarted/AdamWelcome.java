package com.gettingstarted;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysassa.whitelabel.R;

/**
 * Created by Adam on 1/12/2015.
 */
public class AdamWelcome extends Fragment {
    public AdamWelcome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(com.mysassa.R.layout.fragment_welcome, container, false);
        vg.findViewById(R.id.link_adamhammer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.adamhammer.ca"));
                startActivity(i);
            }
        });

        vg.findViewById(R.id.link_platform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.simpleplatform.ca"));
                startActivity(i);
            }
        });

        vg.findViewById(R.id.link_hosting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://hosting.metalrain.ca"));
                startActivity(i);
            }
        });

        return vg;

    }
}
