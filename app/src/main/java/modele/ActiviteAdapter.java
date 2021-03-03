package modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapan.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ActiviteAdapter extends ArrayAdapter<Activite> {

    private Context mContext;
    private int mResource;
    private boolean isDistanceMetrique = true;

    public ActiviteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Activite> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        NumberFormat formatter = new DecimalFormat("#0.00");
        Activite activite = getItem(position);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView iconSport = convertView.findViewById(R.id.iconSport);
        TextView txtNom = convertView.findViewById(R.id.textViewNom);
        TextView txtDate = convertView.findViewById(R.id.textViewDate);
        TextView txtDuree = convertView.findViewById(R.id.textViewDuree);
        TextView txtDistance = convertView.findViewById(R.id.textViewDistance);

        iconSport.setImageResource(getItem(position).getSport().getImage());
        txtNom.setText(getItem(position).getNom());
        txtDate.setText(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.CANADA_FRENCH).withZone(ZoneId.of("EST")).format(getItem(position).getDate()));
        txtDuree.setText(DateTimeFormatter.ofPattern("HH'h'mm'min'").withZone(ZoneId.of("UTC")).format(getItem(position).getDuree().addTo(Instant.ofEpochSecond(0))));
        if (isDistanceMetrique)
            txtDistance.setText(formatter.format(getItem(position).getDistanceMetrique()/1000) + "km");
        else
            txtDistance.setText(formatter.format(getItem(position).getDistanceImperiale()) + "mi");


        return convertView;
    }
}
