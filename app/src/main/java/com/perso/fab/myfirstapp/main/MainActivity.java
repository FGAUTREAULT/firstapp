package com.perso.fab.myfirstapp.main;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.perso.fab.myfirstapp.R;
import com.perso.fab.myfirstapp.displaymessage.DisplayMessageActivity;
import com.perso.fab.myfirstapp.fragment.FragmentActivity;
import com.perso.fab.myfirstapp.search.SearchActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements OnFragmentInteractionListener {

    public static final String EXTRA_MESSAGE = "com.perso.fab.myfirstapp.MESSAGE";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            EditText editText = (EditText) findViewById(R.id.edit_message);
            String message = savedInstanceState.getString("A");
            editText.setText(message);
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            FirstFragment firstFragment = FirstFragment.newInstance("Fragment first");

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment, "first fragment").commit();
        }

        // Setting the drawer
        mPlanetTitles = getResources().getStringArray(R.array.activity_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //TODO multiple choice
            Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
            startActivity(intent);
        }
    }

    public void sendMessage(View clickedView) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        //Open the home screen
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
    }

    public void replaceFragment(View clickedView) {
        // Create fragment and give it an argument specifying the article it should show
        FirstFragment newFragment = FirstFragment.newInstance("Fragment replace");
//        Bundle args = new Bundle();
//        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        savedInstanceState.putString("A", message);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = savedInstanceState.getString("A");
        editText.setText(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentClicked(String title) {
        Log.d("Main", title);
        SecondFragment frag = (SecondFragment)
                getSupportFragmentManager().findFragmentByTag("second fragment");


        if (frag != null) {
            Log.d("Main", "la");
            // If article frag is available, we're in two-pane layout...
            // Call a method in the ArticleFragment to update its content
            // Map point based on address
            startExternalActivity();
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            SecondFragment newFragment = SecondFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment, "second fragment");
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    private void startExternalActivity() {
        //        Open maps
//        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
//        // Or map point based on latitude/longitude
//        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
//
//        PackageManager packageManager = getPackageManager();
//        List activities = packageManager.queryIntentActivities(mapIntent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//
//        if (activities.size() > 0) startActivity(mapIntent);

        //Get an image as a result
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Make sure app exist
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_IMG.jpg";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES + "\\MyFirstAppImages");

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File storageDir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
//        File image = File.createFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );

        File image = new File(storageDir, imageFileName);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SecondFragment frag = (SecondFragment)
                getSupportFragmentManager().findFragmentByTag("second fragment");

        if (frag != null) {
            //Get back the image
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        // Error occurred while creating the File
                        e.printStackTrace();
                    }

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");

                    if (photoFile != null & bmp != null) {
                        saveImageFile(photoFile, bmp);

                        if (photoFile.exists()) {
                            ImageView mImageView = (ImageView) frag.getView().findViewById(R.id.imageViewer);
                            mImageView.setImageBitmap(decodeBitmapInSampleSize(photoFile, mImageView.getWidth(), mImageView.getHeight()));
                        }
                    }
                }
            }
        }
    }

    private Bitmap decodeBitmapInSampleSize(File photoFile, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void saveImageFile(File photoFile, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(photoFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
