package i5.epsi.fr.parkmenantes;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import i5.epsi.fr.parkmenantes.i5.epsi.fr.parjmenantes.model.Parking;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetParkings().execute("");



    }

    private class GetParkings extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String URL_CALL = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content?filter={%22CATEGORIE%22:{%22$eq%22:1001}}/?format=json&THEME=10";
            URL url = null;
            String result = null;
            try {
                url = new URL(URL_CALL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();



                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;

                JSONObject jsonResp = new JSONObject(r.readLine());
                JSONArray jArray = jsonResp.getJSONArray("data");
                for (int i=0; i < jArray.length(); i++)
                {
                    Parking park = new Parking();
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        JSONArray gpsCoordonate = oneObject.getJSONArray("_l");
                            park.setLatitude(gpsCoordonate.getInt(0));
                            park.setLongitude(gpsCoordonate.getInt(1));

                        // Pulling items from the array
                        String oneObjectsItem = oneObject.getString("TELEPHONE");

                        String oneObjectsItem2 = oneObject.getString("ADRESSE");

                        park.setAddress(oneObjectsItem2);
                        park.setPhone(oneObjectsItem);
                        park.setId(oneObject.getInt("_IDOBJ"));


                    } catch (JSONException e) {
                        // Oops
                    }
                }
                        urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
