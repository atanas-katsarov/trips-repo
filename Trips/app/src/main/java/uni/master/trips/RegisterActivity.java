package uni.master.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText passwordRepeat;
    Button register;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);
        register = findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onBtnRegisterClick(View v) {
        if (password.getText().toString().length() < 6) {
            Toast.makeText(RegisterActivity.this, "The password is too short", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordRepeat.getText().toString().equals(password.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registred", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_LONG).show();
        }
    }
}
