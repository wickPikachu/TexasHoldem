package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import hk.edu.cityu.cs.fyp.texasholdem.BuildConfig;

public class SocketHelper {

    private final String TAG = "SocketHelper";

    private static final SocketHelper ourInstance = new SocketHelper();

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public static SocketHelper getInstance() {
        return ourInstance;
    }

    private SocketHelper() {
    }

    public void connectToServer() {
        try {
            InetAddress ip = InetAddress.getByName(BuildConfig.SERVER_URL);
            socket = new Socket(ip, BuildConfig.SERVER_PORT);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (socket.isConnected()) {


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "connectToServer: " );
        }

    }

    public void sent(String data) {
        // TODO
    }

    public void disconnect() {
        try {
            bufferedWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "disconnect: ");
        }
    }
}
