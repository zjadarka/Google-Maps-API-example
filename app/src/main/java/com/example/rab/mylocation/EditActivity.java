package com.example.rab.mylocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Rab on 2016-09-25.
 */

public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editactivity);

        // Get info from intent
        final LatLng latLng = (LatLng) getIntent().getParcelableExtra("location");

        final EditText title = (EditText) findViewById(R.id.title);
        Button button = (Button) findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MarkerOptions marker = new MarkerOptions().position(latLng);
                String snippet = "a";
                if (title.getText() != null) {
                    snippet = title.getText().toString();
                    //marker.snippet(title.getText().toString());

                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", latLng);
                resultIntent.putExtra("snippet", snippet);
                setResult(Activity.RESULT_OK, resultIntent);
                //Toast.makeText(EditActivity.this, snippet, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
