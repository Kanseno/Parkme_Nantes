package i5.epsi.fr.parkmenantes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import i5.epsi.fr.parkmenantes.model.Parking;
import i5.epsi.fr.parkmenantes.utils.GetParkings;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listview = (ListView) findViewById(R.id.listViewSearch);

        String parkings = getIntent().getStringExtra("parkings");
        List<Parking> listParking = GetParkings.jsonToParking(parkings);

        CustomArrayAdapter adapter = new CustomArrayAdapter(this,
                R.layout.custom_adapter, listParking);
        listview.setAdapter(adapter);

    }

}
