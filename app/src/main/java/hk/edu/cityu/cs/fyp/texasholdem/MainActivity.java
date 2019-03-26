package hk.edu.cityu.cs.fyp.texasholdem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logs)
    Button logsButton;

    public static final String TAG = "MainActivity";
    SocketHelper socketHelper;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    OutputStreamWriter outputStreamWriter;
    private SocketHelper.SocketListener socketListener;
    JSONObject jsonObject;
    String response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        logsButton.setVisibility(View.GONE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("test", "test message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // test network
        Thread thread = new Thread(Connection);
        thread.start();

    }

    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            try {
                socket = new Socket(BuildConfig.SERVER_URL, BuildConfig.SERVER_PORT);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                        1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                 * notice: inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (socket.isConnected()) {
                    String tmp = bufferedReader.readLine();
                    if (tmp != null) {
                        tmp = tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                        JSONObject message = new JSONObject(tmp);
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
            outputStreamWriter.write(jsonObject + "\n");
            outputStreamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "sent: " + e.getLocalizedMessage());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.play)
    public void onPlayClicked() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logs)
    public void onLogsClicked() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.help)
    public void onHelpClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        View v = LinearLayout.inflate(this, R.layout.alert_help, null);
        builder.setView(v);
        builder.show();

        sent(jsonObject);
    }

}
