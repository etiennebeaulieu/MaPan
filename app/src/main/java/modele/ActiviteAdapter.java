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

import java.util.ArrayList;

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

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView iconSport = convertView.findViewById(R.id.iconSport);
        TextView txtNom = convertView.findViewById(R.id.textViewNom);
        TextView txtDate = convertView.findViewById(R.id.textViewDate);
        TextView txtDuree = convertView.findViewById(R.id.textViewDuree);
        TextView txtDistance = convertView.findViewById(R.id.textViewDistance);

        iconSport.setImageResource(getItem(position).getSport().getImage());
        txtNom.setText(getItem(position).getNom());
        txtDate.setText(getItem(position).getDate().toString());
        txtDuree.setText(getItem(position).getDuree().toString());
        if (isDistanceMetrique)
            txtDistance.setText(getItem(position).getDistanceMetrique() + "km");
        else
            txtDistance.setText(getItem(position).getDistanceImperiale() + "mi");


        return convertView;
    }
}
