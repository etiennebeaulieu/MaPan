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

public class NouvelleActiviteAdapter extends ArrayAdapter<Sport> {

    private Context mContext;
    private int mResource;

    public NouvelleActiviteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Sport> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        Sport sport = getItem(position);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView iconSportNouvelle = convertView.findViewById(R.id.iconSportNouvelle);
        TextView textViewNomActivite = convertView.findViewById(R.id.textViewNomActivite);

        iconSportNouvelle.setImageResource(getItem(position).getImage());
        textViewNomActivite.setText(getItem(position).getNom());

        return convertView;
    }
}
