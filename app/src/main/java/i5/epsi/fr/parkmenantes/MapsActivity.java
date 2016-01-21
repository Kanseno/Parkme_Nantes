package i5.epsi.fr.parkmenantes;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.concurrent.ExecutionException;

import i5.epsi.fr.parkmenantes.model.Parking;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public List<Parking> ParkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private class GetParkings extends AsyncTask<String, Void, List<Parking>> {



        @Override
        protected List<Parking> doInBackground(String... params) {

            ParkingList = new ArrayList<Parking>();
            //String URL_CALL = "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content?filter={%22CATEGORIE%22:{%22$eq%22:1001}}/?format=json&THEME=10";
            String URL_CALL = "http://data.nantes.fr/api/getDisponibiliteParkingsPublics/1.0/0TZIF9VWW3BTCBC/?output=json";
            URL url = null;
            String result = null;


            try {
                url = new URL(URL_CALL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();

                String line = null;
                while ((line = r.readLine()) != null) {
                    total.append(line + "\n");
                }
                result = total.toString();
                Log.d("result", result);
                JSONObject jsonResp = new JSONObject(result);
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

                    } catch (JSONException e) {
                        // Oops
                    }

                    ParkingList.add(park);

                }
                urlConnection.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < ParkingList.size(); i++) {
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
                    urlConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return ParkingList;
        }



        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            List<Parking> MyParkingList = new GetParkings().execute("").get();
       for(int i = 0; i < MyParkingList.size(); i++){
           Log.d("markers", MyParkingList.get(i).getName());
           LatLng pos =new LatLng(MyParkingList.get(i).getLatitude(),MyParkingList.get(i).getLongitude());

           mMap.addMarker(new MarkerOptions()
                   .position(pos)
                   .title(MyParkingList.get(i).getName())
                   .snippet("Places disponibles : " + MyParkingList.get(i).getAvailablePlaces())
           );

       }



        // Add a marker in Sydney and move the camera
        LatLng nantes = new LatLng(47.2172500, -1.5533600
        );
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nantes));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
