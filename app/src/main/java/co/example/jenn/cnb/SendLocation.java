package co.example.jenn.cnb;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;



public class SendLocation extends Activity {
    Button sendButton;
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);

        sendButton = (Button) findViewById(R.id.sendButton);

        // show location button click events
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // create class object
                gps = new GPSTracker(SendLocation.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    JSONObject jsonobj = new JSONObject();
                    try {
                        jsonobj.put("lat", latitude);
                        jsonobj.put("long", longitude);
                        jsonobj.put("pass", "un1c0rn5");

                    }
                    catch (JSONException ex) {

                        ex.printStackTrace();
                    }
                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppostreq = new HttpPost("http://www.coffeenbeer.com/move");
                        StringEntity se = new StringEntity(jsonobj.toString());
                        se.setContentType("application/json;charset=UTF-8");
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                        httppostreq.setEntity(se);
                        HttpResponse httpresponse = httpclient.execute(httppostreq);

                        HttpEntity resEntity = httpresponse.getEntity();
                        Log.i("RESPONSE", EntityUtils.toString(resEntity));
                        if (resEntity != null) {
                            Log.i("RESPONSE", EntityUtils.toString(resEntity));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }

}

