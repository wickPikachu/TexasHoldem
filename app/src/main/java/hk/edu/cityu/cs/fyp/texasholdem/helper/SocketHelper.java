package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketHelper {

    private final String TAG = "SocketHelper";

    private static final SocketHelper ourInstance = new SocketHelper();

    private Context context;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private SocketListener socketListener;
    private Thread thread;
    private String ipAddress;
    private int port;

    public static SocketHelper getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        this.context = context;
    }

    private SocketHelper() {
    }

    public void connectToServer(String ipAddress, int port, SocketListener socketListener) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.socketListener = socketListener;
        thread = new Thread(Connection);
        thread.start();
    }

    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            try {
                socket = new Socket(ipAddress, port);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (socket.isConnected()) {
                    String tmp = bufferedReader.readLine();
                    if (tmp != null) {
                        tmp = tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                        JSONObject message = new JSONObject(tmp);
                        socketListener.onMessageReceived(message);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "connectToServer: " + e.getLocalizedMessage());
            }

        }
    };

    public void sent(JSONObject jsonObject) {
        try {
            bufferedWriter.write(jsonObject + "\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "sent: " + e.getLocalizedMessage());
        }
    }

    public void disconnect() {
        try {
            bufferedWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "disconnect: " + e.getLocalizedMessage());
        }
    }

    public interface SocketListener {
        public void onMessageReceived(JSONObject message);
    }
}
