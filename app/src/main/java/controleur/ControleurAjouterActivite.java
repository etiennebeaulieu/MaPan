package controleur;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mapan.R;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

public class ControleurAjouterActivite extends AppCompatActivity
{

    private EditText editTextNom;
    private Spinner spinnerSport;
    private Instant date;
    private EditText editTextDuree;
    private EditText editTextDistance;
    private Button boutonDate;

    private DatePickerFragment datePicker;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ajouter_activite);
        editTextNom = findViewById(R.id.editTextNom);
        spinnerSport = (Spinner) findViewById(R.id.spinnerSport);
        editTextDuree = findViewById(R.id.editTextDuree);
        editTextDistance = findViewById(R.id.editTextDistance);
        boutonDate = findViewById(R.id.boutonDate);

        datePicker = new DatePickerFragment();
    }

    public String getNom(){
        return this.editTextNom.getText().toString();
    }

    public void setDate(Date date){
        this.date = date.toInstant();
    }

    private void choisirDate(View view){
        Dialog dialog = datePicker.getDialog();

        dialog.show();

    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        private final Calendar c = Calendar.getInstance();
        private int year;
        private int month;
        private int day;
        private Date date;
        private Dialog dialog;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            dialog = new DatePickerDialog(getActivity(),this, year, month, day);
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = new Date(year,month,dayOfMonth);

            setDate(date);
        }

        public Dialog getDialog(){
            return dialog;
        }
    }
}
