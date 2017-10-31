package ch.ethz.inf.vs.a3.solution;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by nachtigaller on 10/31/17.
 */

public class RegisterManager extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... strings) {
        if (strings.length != 3) {
            return null;
        }
        String ret = "";
        byte[] ip = new byte[4];
        String[] ipNums = strings[1].split(".");
        for (int i = 0; i < ipNums.length; i++) {
            ip[i] = Byte.parseByte(ipNums[i]);
        }
        try {
            ret = sendRegisterRequest(strings[0], ip, Integer.parseInt(strings[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String sendRegisterRequest(String username, byte[] ip, int port) throws IOException {
        String ret = "Could not connect to Server.";
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5);
        JSONObject obj = new JSONObject();

        String dString = obj.toString();
        byte[] buf = dString.getBytes();
        InetAddress address = InetAddress.getByAddress(ip);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        for (int i = 0; i < 5; i++) {
            socket.send(packet);
            Log.d("##RegisterManager", "packet sent");
            byte[] recBuf = new byte[256];
            DatagramPacket getack = new DatagramPacket(recBuf, recBuf.length);
            socket.receive(getack);
            Log.d("##RegisterManager", "packet recieved");
            if (packet.getData().toString().equals("ACK")) {
                ret = "Registration successful.";
            }
        }
        return ret;
    }

    protected void onPostExecute(String str) {

    }

}
