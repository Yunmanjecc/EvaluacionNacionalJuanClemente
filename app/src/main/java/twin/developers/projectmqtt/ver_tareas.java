package twin.developers.projectmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class ver_tareas extends AppCompatActivity implements ValueEventListener {
    TableLayout tableTareas;
    DatabaseReference tareasRef;
    List<Tarea> tareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tareas);

        tableTareas = findViewById(R.id.tableTareas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tareasRef = database.getReference("tarea"); // Obtener la referencia a la lista de tareas

        // Inicializar la lista de tareas
        tareas = new ArrayList<>();

        // Configurar el ValueEventListener
        tareasRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Limpiar la lista de tareas
        tareas.clear();

        // Añadir las tareas a la lista
        for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {
            String usuario = tareaSnapshot.child("usuario").getValue(String.class);
            String tarea = tareaSnapshot.child("tarea").getValue(String.class);
            String prioridad = tareaSnapshot.child("prioridad").getValue(String.class);
            String fechaLimite = tareaSnapshot.child("fecha_limite").getValue(String.class);

            // Crear una nueva instancia de Tarea y agregarla a la lista
            Tarea tareaObj = new Tarea(usuario, tarea, prioridad, fechaLimite);
            tareas.add(tareaObj);
        }

        // Referencia a TableLayout
        TableLayout tablaTareas = findViewById(R.id.tableTareas);

        // Agregar las filas a la tabla
        for (Tarea tarea : tareas) {
            // Crea una nueva fila para cada campo de la tarea y lo añade a la tabla
            TableRow filaUsuario = new TableRow(this);
            TextView usuarioText = new TextView(this);
            usuarioText.setText("Usuario: " + tarea.getUsuario());
            filaUsuario.addView(usuarioText);
            tablaTareas.addView(filaUsuario);

            TableRow filaTarea = new TableRow(this);
            TextView tareaText = new TextView(this);
            tareaText.setText("Tarea: " + tarea.getTarea());
            filaTarea.addView(tareaText);
            tablaTareas.addView(filaTarea);

            TableRow filaPrioridad = new TableRow(this);
            TextView prioridadText = new TextView(this);
            prioridadText.setText("Prioridad: " + tarea.getPrioridad());
            filaPrioridad.addView(prioridadText);
            tablaTareas.addView(filaPrioridad);

            TableRow filaFechaLimite = new TableRow(this);
            TextView fechaLimiteText = new TextView(this);
            fechaLimiteText.setText("Fecha límite: " + tarea.getFechaLimite());
            filaFechaLimite.addView(fechaLimiteText);
            tablaTareas.addView(filaFechaLimite);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Manejar el error
    }
}