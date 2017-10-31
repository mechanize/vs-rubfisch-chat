package ch.ethz.inf.vs.a3.solution;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.UUID;

/**
 * Created by nachtigaller on 10/31/17.
 */

public class RegisterManager extends AsyncTask<String, Void, String> {
    final private String TAG = "##RegisterManager";
    public AsyncResponse delegate = null; //returns to MainActivity

    public RegisterManager(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }
    protected String doInBackground(String... strings) {
        if (strings.length != 3) {
            Log.d(TAG, "doInBackground: Wrong number of arguments");
            return null;
        }
        String ret = "";
        try {
            ret = sendRegisterRequest(strings[0], UUID.randomUUID().toString() , strings[1], Integer.parseInt(strings[2]));
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return ret;
    }

    private byte[] getRegisterMessage(String username, String uuid) throws IOException{
        ch.ethz.inf.vs.a3.solution.message.Message msg = new ch.ethz.inf.vs.a3.solution.message.Message(
                username, uuid, "{}", "register");
        return msg.Msg.getBytes("UTF-8");
    }

    private String sendRegisterRequest(String username, String uuid, String ip, int port) throws IOException {
        String ret = "Connection timed out!";

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(10000);
        InetAddress address = InetAddress.getByName(ip);
        byte[] buf = getRegisterMessage(username, uuid);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        for (int i = 0; i < 5; i++) {
            socket.send(packet);
            byte[] recBuf = new byte[256];
            DatagramPacket getack = new DatagramPacket(recBuf, recBuf.length);
            try {
                socket.receive(getack);
                String ack = new String(getack.getData(), getack.getOffset(), getack.getLength(), "UTF-8");
                Log.d(TAG, ack);
                String recType = "";
                try {
                    ch.ethz.inf.vs.a3.solution.message.Message msg = new ch.ethz.inf.vs.a3.solution.message.Message(ack);
                    recType = msg.type;
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
                if (recType.equals("ack")) {
                    Log.d(TAG, "ack received");
                    ret = "Registration successful.";
                    i = 5;
                } else if (recType.equals("error")) {
                    Log.d(TAG, "Server returned an error");
                    ret = "Server returned an error. Please try again";
                }
            } catch (SocketTimeoutException e) {
                Log.d(TAG , "Socket timed out");
            }
        }
        return ret;
    }

    protected void onPostExecute(String ret) {
        if (ret != null) {
            delegate.processFinish(ret);
        }
    }

}
