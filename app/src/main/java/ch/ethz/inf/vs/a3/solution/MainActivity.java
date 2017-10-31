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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        final Button join = (Button) findViewById(R.id.join_button);
        final Intent joinIntent = new Intent();//this, null);
        join.setOnClickListener(this);

        final Button settings = (Button) findViewById(R.id.settings_button);
        final Intent settingsIntent = new Intent();//this, null);
        settings.setOnClickListener(this);

        }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.join_button) {
            Log.d("##Main", "Join clicked");
            TextView status = (TextView) findViewById(R.id.status);
            status.setText(R.string.processing);
            Log.d("##Main", "Status text set to processing");
            String username = editText.getText().toString();
            String msg = ""; //TODO
            String ip = "127.0.1.1";
            String port = "4446";
            new RegisterManager().execute(username, ip, port);
            status.setText(msg);
            //startActivity(joinIntent);
        }
    }
}
