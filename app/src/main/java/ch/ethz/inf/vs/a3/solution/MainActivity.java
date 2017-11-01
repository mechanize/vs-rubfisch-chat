package ch.ethz.inf.vs.a3.solution;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.UUID;

import ch.ethz.inf.vs.a3.solution.udpclient.NetworkConsts;
import ch.ethz.inf.vs.a3.vsrubfischchat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "##MainActivity";
    EditText editText;
    TextView status;
    String host = NetworkConsts.SERVER_ADDRESS;


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
        String username;
        String uuid;
        if (view.getId() == R.id.join_button) {
            status.setText(R.string.processing);
            username = editText.getText().toString();
            uuid = UUID.randomUUID().toString();
            new RegisterManager(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    status.setText(output);
                }
            }).execute(username, host, uuid );

            final Intent joinIntent = new Intent(this, ChatActivity.class);
            joinIntent.putExtra("Username", username);
            joinIntent.putExtra("UUID", uuid);
            //startActivity(joinIntent);

        } else if (view.getId() == R.id.settings_button) {
            final Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("address", NetworkConsts.SERVER_ADDRESS);
            settingsIntent.putExtra("port", Integer.toString(NetworkConsts.UDP_PORT));
            startActivityForResult(settingsIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getStringExtra("address");
                String port = data.getStringExtra("port");
                if (address.equals("")) {
                    NetworkConsts.SERVER_ADDRESS = "10.0.2.2"; // default address
                } else {
                    NetworkConsts.SERVER_ADDRESS = data.getStringExtra("address");
                }
                if (port.equals("")) {
                    NetworkConsts.UDP_PORT = 4446; // default port
                } else {
                    try {
                        NetworkConsts.UDP_PORT = Integer.parseInt(data.getStringExtra("port"));
                    } catch (NumberFormatException e) {
                        NetworkConsts.UDP_PORT = 4446;
                    }
                }
                status.setText(R.string.settings_saved);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                status.setText(R.string.settings_canceled);
            }
        }
    }
}

/*public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
}*/
