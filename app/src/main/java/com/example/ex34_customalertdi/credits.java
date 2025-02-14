package com.example.ex34_customalertdi;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_credits);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creditsmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain) {
            finish();
        }
        return true;
    }
}
