package com.perso.fab.myfirstapp.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.perso.fab.myfirstapp.R;

import java.io.File;
import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {

    private ArrayList<String> imagesNames = new ArrayList<String>();
    private ArrayList<String> imagesPath = new ArrayList<String>();
    private ArrayList<File> imagesFiles = new ArrayList<File>();

    private View viewContainer;
    private int positionRemoved;
    private File itemRemoved;
    private View viewRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (imagesFiles.isEmpty()) {
            prepareFiles();
        }

        ListView listview = (ListView) findViewById(R.id.listviewSearch);
        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, imagesFiles);
        listview.setAdapter(adapter);

        viewContainer = findViewById(R.id.undobar);
        Button undo = (Button) viewContainer.findViewById(R.id.undobar_button);

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewContainer.animate().cancel();
                viewContainer.setVisibility(View.GONE);
                if(itemRemoved != null){
                    addItemToList(itemRemoved, adapter, positionRemoved);
//                    viewRemoved.setAlpha(0);
                    viewRemoved.animate().alpha(0).setDuration(2000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    endUndoAnimation();
                                }
                            });
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                final File item = (File) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                removeItemFromList(item, adapter, view, position);
                            }
                        });
            }

        });

    }

    private void removeItemFromList(File item, MySimpleArrayAdapter adapter, View view, int position) {
        imagesFiles.remove(item);
        adapter.notifyDataSetChanged();
        view.setAlpha(1);

        itemRemoved = item;
        positionRemoved = position;
        viewRemoved = view;

        showUndo(viewContainer, position, item, view);
    }

    private void addItemToList(File item, MySimpleArrayAdapter adapter, int position) {
        imagesFiles.add(position, item);
        adapter.notifyDataSetChanged();
    }

    public void showUndo(final View viewContainer, final int position, File item, View view) {

        viewContainer.setVisibility(View.VISIBLE);
        viewContainer.setAlpha(1);
        viewContainer.animate().alpha(0.4f).setDuration(5000)
                .withEndAction(new Runnable() {

                    @Override
                    public void run() {
                        endUndoAnimation();
                    }
                });

    }

    private void endUndoAnimation(){
        viewContainer.setVisibility(View.GONE);
        itemRemoved = null;
        positionRemoved = -1;
        viewRemoved = null;
    }

    private void prepareFiles() {
        // path to /data/data/yourapp/app_data/imageDir
        File[] images = getDir("imageDir", Context.MODE_PRIVATE).listFiles();
        for (File image : images) {
            imagesNames.add(image.getName());
            imagesPath.add(image.getAbsolutePath());
            imagesFiles.add(image);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        return true;
//    }
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
