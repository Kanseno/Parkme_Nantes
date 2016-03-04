package i5.epsi.fr.parkmenantes.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

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
        String urlDispoParking = Constantes.URL_DISPO_PARKING;
        this.parkingList = getDisponibiliteParking(urlDispoParking);

        String urlLocationEquipement = Constantes.URL_LOCATION_EQUIPEMENT;
        this.parkingList = getPositionParking(urlLocationEquipement);

        return parkingList;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }


    public List<Parking> getDisponibiliteParking(String urlDispoParking) {
        String res = getDataFrom(urlDispoParking);

        try {
            Log.d("res", res);
            JSONObject jsonResp = new JSONObject(res);
            JSONObject opendata = jsonResp.getJSONObject(Constantes.OPENDATA);
            JSONObject answer = opendata.getJSONObject(Constantes.ANSWER);
            JSONObject data = answer.getJSONObject(Constantes.DATA);
            JSONObject parkingGroups = data.getJSONObject(Constantes.GROUPES_PARKING);
            JSONArray parkingGroupArray = parkingGroups.getJSONArray(Constantes.GROUPE_PARKING);


            for (int i = 0; i < parkingGroupArray.length(); i++) {
                Parking park = new Parking();
                try {
                    JSONObject oneObject = parkingGroupArray.getJSONObject(i);

                    park.setId(Integer.parseInt(oneObject.getString(Constantes.ID_OBJET)));
                    park.setName(oneObject.getString(Constantes.PARKING_NAME));
                    park.setAvailablePlaces(Integer.parseInt(oneObject.getString(Constantes.PARKING_PLACES_DISPO)));
                    park.setMaxPlaces(Integer.parseInt(oneObject.getString(Constantes.PARKING_MAX_PLACES)));

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

    public List<Parking> getPositionParking(String urlLocationEquipement) {
        String result;

        for (int i = 0; i < parkingList.size(); i++) {
            try {
                result = getDataFrom(urlLocationEquipement + Constantes.FILTER1 + parkingList.get(i).getId() + Constantes.FILTER2);

                Log.d("Result", result);
                JSONObject jsonResp = new JSONObject(result);
                JSONArray parkingData = jsonResp.getJSONArray(Constantes.DATA);
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
        String line;

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

   // {"latitude":47.2049704647258,"longitude":-1.54433295436521,"name":"LES FONDERIES","availablePlaces":76,"id":4319,"maxPlaces":120,"theme":0,"type":0,"zipCode":0}
    public static List<Parking> jsonToParking(String json) {
        Parking parkingTmp = new Parking();
        List<Parking> listParking = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);

            for(int i = 0; i <= jsonArray.length(); i++){
                JSONObject row = jsonArray.getJSONObject(i);

                parkingTmp.setLatitude(row.getDouble("latitude"));
                parkingTmp.setLongitude(row.getDouble("longitude"));
                parkingTmp.setName(row.getString("name"));
                parkingTmp.setAvailablePlaces(row.getInt("availablePlaces"));
                parkingTmp.setId(row.getInt("id"));
                parkingTmp.setMaxPlaces(row.getInt("maxPlaces"));

                listParking.add(parkingTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listParking;
    }
}