package twin.developers.projectmqtt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class agregar_tarea extends AppCompatActivity {
    private Mqtt mqttManager;
    EditText tarea;
    Spinner prioridad;
    CalendarView calendario;
    Button listo;
    DatabaseReference tareaRef;  // Referencia a la lista de tareas en Firebase
    long fechaSeleccionada;  // Variable para almacenar la fecha seleccionada

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        tarea = findViewById(R.id.txtNombreTarea);
        prioridad = findViewById(R.id.spinnerPrioridad);
        calendario = findViewById(R.id.calendarioTarea);
        listo = findViewById(R.id.bttnListo);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Conexión al broker MQTT
        mqttManager = new Mqtt(getApplicationContext());
        mqttManager.connectToMqttBroker();

        // Obtención de la referencia a la lista de tareas en Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tareaRef = database.getReference("tarea");  // Obtener la referencia a la lista de tareas

        // Crear el ArrayAdapter con las opciones de prioridad
        String[] opcionesPrioridad = new String[] {"baja", "media", "alta"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcionesPrioridad);

        // Establecer el ArrayAdapter en el Spinner
        prioridad.setAdapter(adapter);

        // Definición para el cambio de fecha en el CalendarView
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                fechaSeleccionada = c.getTimeInMillis();
            }
        });

        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recolección de los datos de entrada y creación de un nuevo objeto 'Tarea'
                String tareaStr = tarea.getText().toString();
                String prioridadStr = prioridad.getSelectedItem().toString();

                // Obtención de la fecha en milisegundos
                long fechaMilis = calendario.getDate();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaStr = sdf.format(new Date(fechaSeleccionada));

                // Crear un nuevo objeto Tarea
                Tarea nuevaTarea = new Tarea(username, tareaStr, prioridadStr, fechaStr);

                // Agregar el mensaje a la lista de tareas en Firebase
                tareaRef.push().setValue(nuevaTarea).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Mostrar un mensaje de éxito
                            Toast.makeText(agregar_tarea.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
                        } else {
                            // Mostrar un mensaje de error
                            Toast.makeText(agregar_tarea.this, "Error al agregar la tarea", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Creación de un objeto JSON para enviarlo como mensaje MQTT
                JSONObject mensaje = new JSONObject();
                try {
                    mensaje.put("Usuario", username);
                    mensaje.put("Tarea", tareaStr);
                    mensaje.put("Prioridad", prioridadStr);
                    mensaje.put("Fecha limite", fechaStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Convertir el objeto JSONObject a un array de bytes
                byte[] payload = mensaje.toString().getBytes(StandardCharsets.UTF_8);
                String payloadStr = new String(payload, StandardCharsets.UTF_8);

                // Publicar el mensaje MQTT
                mqttManager.publishMessage(payloadStr);

                /*
                // Obtener una instancia de la base de datos de Firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("iot/lab/test");

                // Agregar el mensaje a la lista de tareas en Firebase
                tareaRef.push().setValue(mensaje.toString());

                myRef.setValue(mensaje.toString());
                */

                Toast.makeText(agregar_tarea.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}