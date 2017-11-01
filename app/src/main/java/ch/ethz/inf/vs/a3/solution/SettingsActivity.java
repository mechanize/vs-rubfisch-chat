package ch.ethz.inf.vs.a3.solution;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ch.ethz.inf.vs.a3.vsrubfischchat.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = "##SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();

        EditText serverAddressV = (EditText) findViewById(R.id.editServerAddress);
        serverAddressV.setText(intent.getStringExtra("address"));

        EditText portV = (EditText) findViewById(R.id.editPort);
        portV.setText(intent.getStringExtra("port"));

        Button save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_button) {
            EditText portV = (EditText) findViewById(R.id.editPort);
            EditText serverAddressV = (EditText) findViewById(R.id.editServerAddress);
            Log.d(TAG, "parsed edits");
            Intent returnIntent = new Intent();
            returnIntent.putExtra("port", portV.getText().toString());
            Log.d(TAG, "filled extras");
            returnIntent.putExtra("address", serverAddressV.getText().toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
