package pceti.a8c1.cetitoolapp.Fragments.Horario;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pceti.a8c1.cetitoolapp.R;


public class AgregarHorarioFragment extends Fragment {

    Button bfecha,bhora, bhora2;
    TextView efecha,ehora, ehora2;
    private  int dia,mes,ano,hora,minutos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_agregar_horario, container, false);

        bfecha=(Button)view.findViewById(R.id.btn_agregar_horario);
        bhora=(Button)view.findViewById(R.id.btn_incio_horario);
        bhora2=(Button)view.findViewById(R.id.btn_fin_horario);
        ehora=(TextView)view.findViewById(R.id.tv_inicio_horario);
        ehora2=(TextView)view.findViewById(R.id.tv_fin_horario);
        efecha=(TextView)view.findViewById(R.id.tv_fecha_horario);

        bfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        efecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                hora=c.get(Calendar.HOUR_OF_DAY);
                minutos=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ehora.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,false);
                timePickerDialog.show();
            }
        });

        bhora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c= Calendar.getInstance();
                hora=c.get(Calendar.HOUR_OF_DAY);
                minutos=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ehora2.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,false);
                timePickerDialog.show();
            }
        });
        return view;
    }
}
