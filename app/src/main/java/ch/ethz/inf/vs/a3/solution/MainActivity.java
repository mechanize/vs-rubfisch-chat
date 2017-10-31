package ch.ethz.inf.vs.a3.solution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.ethz.inf.vs.sajaeger.vsrubfischchat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    TextView status;
    String host = "10.0.2.2";
    String port = "4446";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText  = (EditText) findViewById(R.id.editText);
        status = (TextView) findViewById(R.id.status);

        final Button join = (Button) findViewById(R.id.join_button);
        join.setOnClickListener(this);

        final Button settings = (Button) findViewById(R.id.settings_button);
        settings.setOnClickListener(this);

        }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.join_button) {
            status.setText(R.string.processing);
            String username = editText.getText().toString();
            new RegisterManager(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    status.setText(output);
                }
            }).execute(username, host, port);

            final Intent joinIntent = new Intent();
            //startActivity(joinIntent);

        } else if (view.getId() == R.id.settings_button) {
            final Intent settingsIntent = new Intent();
            //startActivity(settingsIndent);
        }
    }
}
