package ch.ethz.inf.vs.a3.solution.message;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Message{
    public JSONObject JMsg;
    public String Msg;
    public String header;
    public String username;
    public String uuid;
    public String timestamp;
    public String type;
    public String body;
    public Message(String Mg) throws JSONException {
        JMsg = new JSONObject(Mg);
        Msg = Mg;
        header = JMsg.getString("header");
        JSONObject Jheader = new JSONObject(header);
        username = Jheader.getString("username");
        uuid = Jheader.getString("uuid");
        timestamp = Jheader.getString("timestamp");
        type = Jheader.getString("type");
        body = JMsg.getString("body");
        JSONObject Jbody = new JSONObject(body);

    }
    public Message(String u, String uid, String ts, String ty, String bo){
        username = u;
        uuid = uid;
        timestamp = ts;
        type = ty;
        body = bo;
        this.makeJSON();
    }
    public Message(String u, String uid, String ts, String ty){
        username = u;
        uuid = uid;
        timestamp = ts;
        type = ty;
        body = "";
        this.makeJSON();
    }

    private void makeJSON() {
        try {
            JMsg = new JSONObject()
                    .put("header", new JSONObject()
                            .put("username", username)
                            .put("uuid", uuid)
                            .put("timestamp", timestamp)
                            .put("type", type))
                    .put("body", "{" + body + "}");
            //header = JMsg.getString("header");
            Msg = JMsg.toString();
        } catch (JSONException e) {
            Log.d("##Message", e.getMessage());
            e.printStackTrace();
        }
    }

    /*public void create(){
        Msg =
                "{\n" +
                        "\t\"header\": {\n" +
                        "\t\t\"username\": \""+username+"\",\n" +
                        "\t\t\"uuid\": \""+uuid+"\",\n" +
                        "\t\t\"timestamp\": \""+timestamp+"\",\n" +
                        "\t\t\"type\": \""+type+"\"\n" +
                        "\t},\n" +
                        "\t\"body\": {"+body+"}\n" + //body strings need \" added in them but not used anyway
                        "}";
    }*/
}