package i5.epsi.fr.parkmenantes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import i5.epsi.fr.parkmenantes.model.Parking;
import i5.epsi.fr.parkmenantes.utils.Constantes;
import i5.epsi.fr.parkmenantes.utils.GetParkings;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int drawable;
    private GoogleMap mMap;
    List<Parking> MyParkingList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        boolean fromSearch = false;
        mMap = googleMap;

        LatLng nantes = new LatLng(47.2172500, -1.5533600);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nantes));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        try {
            if(!fromSearch) {
                MyParkingList = new GetParkings().execute("").get();
            }
            for(int i = 0; i < MyParkingList.size(); i++){
                Log.d("markers", MyParkingList.get(i).getName());
                LatLng pos =new LatLng(MyParkingList.get(i).getLatitude(),MyParkingList.get(i).getLongitude());

                if(MyParkingList.get(i).getAvailablePlaces() < 5){
                    drawable = R.drawable.red_marker;
                } else if(MyParkingList.get(i).getAvailablePlaces() > 5 && MyParkingList.get(i).getAvailablePlaces() <= 30 ) {
                    drawable = R.drawable.orange_marker;
                } else {
                    drawable = R.drawable.green_marker;
                }
                mMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .title(MyParkingList.get(i).getName())
                                .snippet(Constantes.SNIPPET_PLACES_MAX + Constantes.COLON + MyParkingList.get(i).getMaxPlaces())
                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(drawable, Constantes.EMPTY + MyParkingList.get(i).getAvailablePlaces())))
                );
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MapsActivity.this, ScrollingActivity.class);

//                        for(int i = 0; i < MyParkingList.size(); i++) {
//                            if(MyParkingList.get(i).getName().equals(marker.getTitle())) {

                                Gson gson = new Gson();
                                String json = gson.toJson(MyParkingList);
                                intent.putExtra("parkings",json);
                                startActivity(intent);
//                            }
//                        }


                    }
                });
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }
}
