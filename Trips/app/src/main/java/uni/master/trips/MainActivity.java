package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements CategoriesFragment.OnCategoriesInteractionListener, SitesByTypeFragment.OnSiteByTypeInteractionListener, SiteDetailsFragment.OnDetailsInteractionListener{

    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set action bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();

        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        MenuInflater inflater = getMenuInflater();
        if (firebaseAuth.getCurrentUser() != null) {
            inflater.inflate(R.menu.toolbar_logged_in, menu);
        } else {
            inflater.inflate(R.menu.toolbar_logged_out, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // switch between available action bar buttons
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, MySitesActivity.class);
                Bundle b = new Bundle();
                b.putString("email", firebaseAuth.getCurrentUser().getEmail());
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetailsInteraction() {

    }

    @Override
    public void onSiteByTypeInteraction() {

    }

    @Override
    public void onCategoriesInteraction() {

    }
}
