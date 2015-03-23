package com.perso.fab.myfirstapp.displaymessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.perso.fab.myfirstapp.R;
import com.perso.fab.myfirstapp.main.MainActivity;


public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the intent to load the context of the activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        setContentView(R.layout.activity_display_message);

        // Create the text view
        TextView textView = (TextView) findViewById(R.id.displayed_message);
        textView.setTextSize(40);
        textView.setText(message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
