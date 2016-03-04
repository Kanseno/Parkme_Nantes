package i5.epsi.fr.parkmenantes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import i5.epsi.fr.parkmenantes.model.Parking;

/**
 * Created by klarhant on 04/03/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<Parking> {

    public CustomArrayAdapter(Context context, int resource, List<Parking> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_adapter, parent);
        }
        Parking item = getItem(position);

        TextView itemParkingName = (TextView) convertView.findViewById(R.id.itemParkingName);
        itemParkingName.setText(item.getName());
        TextView itemParkingNbPlaces = (TextView) convertView.findViewById(R.id.itemParkingNbPlaces);
        itemParkingNbPlaces.setText(item.getAvailablePlaces()+" / "+item.getMaxPlaces());


        return convertView;
    }
}
