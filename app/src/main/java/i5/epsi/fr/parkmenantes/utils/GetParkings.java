package i5.epsi.fr.parkmenantes.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import i5.epsi.fr.parkmenantes.model.Parking;

/**
 * Created by klarhant on 01/02/2016.
 */
public class GetParkings extends AsyncTask<String, Void, List<Parking>> {

    public List<Parking> parkingList;

    @Override
    protected List<Parking> doInBackground(String... params) {
        parkingList = new ArrayList<>();
        //String URL_CALL = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content?filter={%22CATEGORIE%22:{%22$eq%22:1001}}/?format=json&THEME=10";
        String URL_CALL = "http://data.nantes.fr/api/getDisponibiliteParkingsPublics/1.0/0TZIF9VWW3BTCBC/?output=json";
        this.parkingList = getDisponibiliteParking(URL_CALL);

        String URL_LOCATION_DATA = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content";
        this.parkingList = getPositionParking(URL_LOCATION_DATA);

        return parkingList;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }


    public List<Parking> getDisponibiliteParking(String URL_CALL) {
        String res = getDataFrom(URL_CALL);

        try {
            Log.d("res", res);
            JSONObject jsonResp = new JSONObject(res);
            JSONObject opendata = jsonResp.getJSONObject("opendata");
            JSONObject answer = opendata.getJSONObject("answer");
            JSONObject data = answer.getJSONObject("data");
            JSONObject parkingGroups = data.getJSONObject("Groupes_Parking");
            JSONArray parkingGroupArray = parkingGroups.getJSONArray("Groupe_Parking");


            for (int i = 0; i < parkingGroupArray.length(); i++) {
                Parking park = new Parking();
                try {
                    JSONObject oneObject = parkingGroupArray.getJSONObject(i);

                    park.setId(Integer.parseInt(oneObject.getString("IdObj")));
                    park.setName(oneObject.getString("Grp_nom"));
                    park.setAvailablePlaces(Integer.parseInt(oneObject.getString("Grp_disponible")));
                    park.setMaxPlaces(Integer.parseInt(oneObject.getString("Grp_exploitation")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parkingList.add(park);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parkingList;
    }

    public List<Parking> getPositionParking(String URL_LOCATION_DATA) {
        String result;

        for (int i = 0; i < parkingList.size(); i++) {
            try {
                result = getDataFrom(URL_LOCATION_DATA + "?filter={%22_IDOBJ%22:{%22$eq%22:" + parkingList.get(i).getId() + "}}/?format=json");

                Log.d("Result", result);
                JSONObject jsonResp = new JSONObject(result);
                JSONArray parkingData = jsonResp.getJSONArray("data");
                JSONObject parkingObject = parkingData.getJSONObject(0);
                JSONArray locationInf = parkingObject.getJSONArray("_l");
                parkingList.get(i).setLatitude(locationInf.getDouble(0));
                parkingList.get(i).setLongitude(locationInf.getDouble(1));
                Log.d("Latitude", "" + parkingList.get(i).getLatitude());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return parkingList;
    }

    private String getDataFrom(String fromUrl) {
        StringBuilder str = new StringBuilder();
        String line = "";

        try {
            URL url = new URL(fromUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            while ((line = r.readLine()) != null) {
                str.append(line).append("\n");
            }
            urlConnection.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return str.toString();
    }
}