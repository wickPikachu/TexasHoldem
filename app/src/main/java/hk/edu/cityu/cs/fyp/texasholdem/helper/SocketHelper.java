package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.BuildConfig;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;

public class SocketHelper {

    private final String TAG = "SocketHelper";

    private static final SocketHelper ourInstance = new SocketHelper();

    private Handler handler;
    private HandlerThread handlerThread;

    private Context context;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String ipAddress = BuildConfig.SERVER_URL;
    private int port = BuildConfig.SERVER_PORT;
    private ArrayList<SocketListener> socketListeners = new ArrayList<>();

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

    public void connectToServer(SocketListener socketListener) {
        if (socket == null || socket.isClosed()) {
            handler.post(() -> {
                try {
                    InetAddress inetAddress = InetAddress.getByName(ipAddress);
                    socket = new Socket(inetAddress, port);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    recvThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + e.getLocalizedMessage());
                }
            });
        } else {
            Log.d(TAG, "connectToServer: socket already connect to " + this.ipAddress + ":" + this.port);
        }
        if (!socketListeners.contains(socketListener))
            socketListeners.add(socketListener);
    }

    private Thread recvThread = new Thread(() -> {
        try {
            String msg;
            while (socket.isConnected() && (msg = bufferedReader.readLine()) != null) {
                Log.d(TAG, "recvThread: msg: " + msg);
                if (msg.equals(""))
                    continue;
                try {
                    JSONObject message = new JSONObject(msg);
                    for (SocketListener socketListener : socketListeners) {
                        socketListener.onResponse(message);
                    }
                } catch (JSONException je) {
                    for (SocketListener socketListener : socketListeners) {
                        socketListener.onError(je.getLocalizedMessage());
                    }
                    Log.e(TAG, "recvThread: JSONException: " + je.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "recvThread: " + e.getLocalizedMessage());
            clearSocketListeners();
        }
    });

    public void sent(JSONObject jsonObject) {
        handler.post(() -> {
            try {
                bufferedWriter.write(jsonObject.toString() + "\n");
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "sent: " + e.getLocalizedMessage());
            }
        });
    }

    public void uploadGameLog(GameLog gameLog) {
        handler.post(() -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.Json.KEY_ACTION, 1);
                jsonObject.put(Constants.Json.KEY_DATA, gameLog.getResult());
                jsonObject.put(Constants.Json.KEY_TYPE, gameLog.getAiPlayer());
                jsonObject.put(Constants.Json.KEY_UUID, gameLog.getUuid());
                Log.d(TAG, "uploadGameLog: " + jsonObject.toString());
                bufferedWriter.write(jsonObject.toString() + "\n");
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "uploadGameLog: " + e.getLocalizedMessage());
            }
        });
    }

    public void uploadGameLogs(List<GameLog> gameLogs) {
        handler.post(() -> {
            try {
                for (GameLog gameLog : gameLogs) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.Json.KEY_ACTION, 1);
                    jsonObject.put(Constants.Json.KEY_DATA, gameLog.getResult());
                    jsonObject.put(Constants.Json.KEY_TYPE, gameLog.getAiPlayer());
                    jsonObject.put(Constants.Json.KEY_UUID, gameLog.getUuid());
                    Log.d(TAG, "uploadGameLogs: " + jsonObject.toString());
                    bufferedWriter.write(jsonObject.toString() + "\n");
                    bufferedWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "uploadGameLogs: " + e.getLocalizedMessage());
            }
        });
    }

    public void clearSocketListeners() {
        socketListeners.clear();
    }

    public void addSocketListener(SocketListener socketListener) {
        if (socketListener == null) {
            return;
        }
        socketListeners.add(socketListener);
    }

    public void disconnect() {
        handler.post(() -> {
            try {
                bufferedWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "disconnect: " + e.getLocalizedMessage());
            }
        });
    }

    public interface SocketListener {
        public void onResponse(JSONObject jsonObject);

        public void onError(String errorMsg);
    }
}
