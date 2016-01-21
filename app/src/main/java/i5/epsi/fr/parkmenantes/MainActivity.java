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
import java.util.ArrayList;
import java.util.List;

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

            //String URL_CALL = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content?filter={%22CATEGORIE%22:{%22$eq%22:1001}}/?format=json&THEME=10";
            String URL_CALL = "http://data.nantes.fr/api/getDisponibiliteParkingsPublics/1.0/39W9VSNCSASEOGV/?output=json";
            URL url = null;
            String result = null;
            List<Parking> ParkingList = new ArrayList<Parking>();

            try {
                url = new URL(URL_CALL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();



                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();

                String line = null;
                while ((line = r.readLine()) != null)
                {
                    total.append(line + "\n");
                }
                result = total.toString();
                Log.d("result",result);
                JSONObject jsonResp = new JSONObject(result);
                JSONObject opendata = jsonResp.getJSONObject("opendata");
                JSONObject answer = opendata.getJSONObject("answer");
                JSONObject data = answer.getJSONObject("data");
                JSONObject parkingGroups = data.getJSONObject("Groupes_Parking");
                JSONArray parkingGroupArray = parkingGroups.getJSONArray("Groupe_Parking");



                for (int i=0; i < parkingGroupArray.length(); i++)
                {
                    Parking park = new Parking();
                    try {
                        JSONObject oneObject = parkingGroupArray.getJSONObject(i);

                        park.setId(Integer.parseInt(oneObject.getString("IdObj")));
                        park.setName(oneObject.getString("Grp_nom"));
                        park.setAvailablePlaces(Integer.parseInt(oneObject.getString("Grp_disponible")));

                    } catch (JSONException e) {
                        // Oops
                    }

                    ParkingList.add(park);

                }
                        urlConnection.disconnect();



            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            for(int i = 0 ; i < ParkingList.size();i++){
                String URL_LOCATION_DATA = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content?filter={%22_IDOBJ%22:{%22$eq%22:" + ParkingList.get(i).getId() + "}}/?format=json";
                try {
                    url = new URL(URL_LOCATION_DATA);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();

                    String line = null;
                    while ((line = r.readLine()) != null) {
                        total.append(line + "\n");
                    }
                    result = total.toString();
                   // Log.d("Result", result);
                    JSONObject jsonResp = new JSONObject(result);
                    JSONArray parkingData = jsonResp.getJSONArray("data");
                    JSONObject parkingObject = parkingData.getJSONObject(0);
                    JSONArray locationInf = parkingObject.getJSONArray("_l");
                    ParkingList.get(i).setLatitude(locationInf.getDouble(0));
                    ParkingList.get(i).setLongitude(locationInf.getDouble(1));
                    Log.d("Latitude", "" + ParkingList.get(i).getLatitude());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    e.printStackTrace();
                }
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
