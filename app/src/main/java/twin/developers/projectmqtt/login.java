package twin.developers.projectmqtt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    Button registrar, ingresar;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrar = findViewById(R.id.bttnRegistrar);
        ingresar = findViewById(R.id.bttnIngresar);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // La creación del usuario fue exitosa
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(login.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // La creación del usuario falló
                                        Toast.makeText(login.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // La creación del usuario fue exitosa
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent1 = new Intent(login.this, pantalla_principal.class);
                                        intent1.putExtra("username", user.getEmail());
                                        startActivity(intent1);

                                        Intent intent2 = new Intent(login.this, agregar_tarea.class);
                                        intent2.putExtra("username", user.getEmail());
                                        startActivity(intent2);
                                    } else {
                                        // La creación del usuario falló
                                        Toast.makeText(login.this, "Error al iniciar la sesión", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}