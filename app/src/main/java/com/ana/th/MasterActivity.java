package com.ana.th;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MasterActivity extends AppCompatActivity {
    /**
     * Creates the content view and toolbar, sets up the drawer layout and the
     * action bar toggle, and sets up the navigation view.
     * @param savedInstanceState    Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //TODO onbackPressed será dirigido a la actividad principal.
    // DUDA!!!!!: SE PODRÍA HACER UN SWITCH CASE, PARA QUE ONBACKPRESSED RETROCEDA A
    // UNA ACTIVIDAD ESPECIFICA( NO SIENDO SIEMPRE ESTA MainActivity), DEPENDIENDO DE LA ACTIVIDAD
    // EN LA QUE SE ESTÁ?
    /**
     * Handles the Back button: closes the nav drawer.
     */
    @Override
    public void onBackPressed() {
                super.onBackPressed();
                Intent intent = new Intent(this, BluetoothActivity.class);
                startActivity(intent);
        }

    /**
     * Inflates the options menu.
     * @param menu  Menu to inflate
     * @return      Returns true if menu is inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a toast message.
     * @param message  Message to display in toast
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}