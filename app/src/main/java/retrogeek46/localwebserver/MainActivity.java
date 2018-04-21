package retrogeek46.localwebserver;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    LocalServer mServer;
    public static String filePath;
    EditText t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = (EditText) findViewById(R.id.input);
    }

    @SuppressWarnings("deprecation")
    public void makeFile() {
        filePath = t.getText().toString();
        mServer = new LocalServer(new File(Environment.getExternalStorageDirectory() + "/" + filePath));
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String deviceIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        mServer.init(deviceIp);
    }

    public void startStreaming(View v) {
        makeFile();
        if (null != mServer && !mServer.isRunning())
            mServer.start();
        ((TextView) findViewById(R.id.status)).setText(mServer.getFileUrl());
    }

    public void stopStreaming(View v) {
        if (null != mServer)
            mServer.stop();
        ((TextView) findViewById(R.id.status)).setText("");
    }

    @Override
    protected void onDestroy() {
        if (null != mServer && mServer.isRunning()) {
            mServer.stop();
            mServer = null;
        }
        super.onDestroy();
    }
}
