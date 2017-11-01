package ch.ethz.inf.vs.a3.solution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import ch.ethz.inf.vs.a3.solution.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ch.ethz.inf.vs.a3.vsrubfischchat.R;
import ch.ethz.inf.vs.a3.solution.message.MessageComparator;
import ch.ethz.inf.vs.a3.solution.queue.PriorityQueue;
import ch.ethz.inf.vs.a3.solution.udpclient.NetworkConsts;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txt;
    String user;
    String uuid;
    MessageComparator msgc;
    PriorityQueue<Message> Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getIntent().getStringExtra("Username"); //intent.putExtra("Username", username);
        uuid = getIntent().getStringExtra("UUID");
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt = (TextView) findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        msgc = new MessageComparator();
        Chat = new PriorityQueue(msgc);

        AsyncTask<Void, Void, Void> async;

        async = new AsyncTask<Void, Void, Void>(){

            DatagramSocket socket;
            InetAddress    inet_addr;
            @Override
            protected Void doInBackground (Void...arg0){
                String[] ipNums = NetworkConsts.SERVER_ADDRESS.split(".");
                byte[] ip = new byte[4];
                for (int i = 0; i < ipNums.length; i++) {
                    ip[i] = Byte.parseByte(ipNums[i]);
                }
                try {
                    inet_addr = InetAddress.getByAddress(ip);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                //Log.e("", "");
                Message getchat = new Message(user,uuid,"{}","retrieve_chat_log");
                byte[] buffer = getchat.Msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, 4096, inet_addr, NetworkConsts.UDP_PORT);
                try {
                    socket = new DatagramSocket(NetworkConsts.UDP_PORT);
                    socket.setSoTimeout(5000);
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                };
                while(true){        // recieve data until timeout
                    try {
                        socket.receive(packet);
                        Message rcvd = new Message(new String(packet.getData()));
                        if(rcvd.type.equals("message")){
                            Chat.add(rcvd);
                        }
                        System.out.println(rcvd);
                    }
                    catch (SocketTimeoutException e) {
                        // timeout exception.
                        break;

                    } catch (JSONException | IOException e) {

                    }
                }
                socket.close();
                //Log.e("", "");
                return null;
            }
        };
        async.execute();



        String Chattxt = "";
        while (Chat.peek() != null){
            Chattxt = Chat.poll().content + "\n";
        }
        txt.setText(Chattxt);
    }


}
