package twin.developers.projectmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class pantalla_principal extends AppCompatActivity {

    TextView username;
    Button agregarTarea, verTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        username = findViewById(R.id.txtViewUsername);
        agregarTarea = findViewById(R.id.bttnAgregarTarea);
        verTareas = findViewById(R.id.bttnVerTareas);

        String nombre = getIntent().getStringExtra("username");
        username.setText(nombre);

        agregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pantalla_principal.this, agregar_tarea.class);
                startActivity(intent);
            }
        });

        verTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pantalla_principal.this, ver_tareas.class);
                startActivity(intent);
            }
        });
    }
}