package uni.master.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uni.master.trips.entities.User;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText passwordRepeat;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);
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
                        firebase = FirebaseDatabase.getInstance().getReference();
                        User user = new User(username.getText().toString());
                        firebase.child("Users").setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //Problem with saving the data
                                if (databaseError != null) {
                                    Toast.makeText(RegisterActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    //Data uploaded successfully on the server
                                    Intent categoryPage = new Intent(RegisterActivity.this, CategoriesActivity.class);
                                    finish();
                                    Toast.makeText(RegisterActivity.this, "User registred", Toast.LENGTH_LONG).show();
                                    startActivity(categoryPage);
                                }
                            }
                        });
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
