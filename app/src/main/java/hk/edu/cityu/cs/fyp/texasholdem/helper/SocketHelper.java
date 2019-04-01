package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
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

    private Handler handler;
    private HandlerThread handlerThread;

    private Context context;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private SocketListener socketListener;
    private String ipAddress;
    private int port;

    public static SocketHelper getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        this.context = context;
        handlerThread = new HandlerThread("SocketThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    private SocketHelper() {
    }

    public void connectToServer(String ipAddress, int port, SocketListener socketListener) {
        if (socket == null || socket.isClosed()) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.socketListener = socketListener;
            handler.post(Connection);
        } else {
            Log.d(TAG, "connectToServer: socket already connect to " + this.ipAddress + ":" + this.port);
        }
    }

    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            try {
                socket = new Socket(ipAddress, port);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                recvThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                socketListener.onError(e.getLocalizedMessage());
                Log.d(TAG, "connectToServer: " + e.getLocalizedMessage());
            }

        }
    };

    private Thread recvThread = new Thread(() -> {
        try {
            String msg;
            while (socket.isConnected() && (msg = bufferedReader.readLine()) != null) {
                Log.d(TAG, "recvThread: msg: " + msg);
                msg = msg.substring(msg.indexOf("{"), msg.lastIndexOf("}") + 1);
                JSONObject message = new JSONObject(msg);
                socketListener.onResponse(message);
            }
        } catch (Exception e) {
            Log.e(TAG, "recvThread: " + e.getLocalizedMessage());
        }
    });

    public void sent(JSONObject jsonObject) {
        handler.post(() -> {
            try {
                bufferedWriter.write(jsonObject + "\n");
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "sent: " + e.getLocalizedMessage());
            }
        });
    }

    public void disconnect() {
        handler.post(() -> {
            try {
                bufferedWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "disconnect: " + e.getLocalizedMessage());
            }
        });
    }

    public interface SocketListener {
        public void onResponse(JSONObject message);

        public void onError(String errorMsg);
    }
}
