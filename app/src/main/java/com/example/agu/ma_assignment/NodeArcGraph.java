package com.example.agu.ma_assignment;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

import javax.security.auth.login.LoginException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NodeArcGraph extends AppCompatActivity {
    private static final String TAG = "NodeArcGraph";

    NodeArcGenerator v;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = new NodeArcGenerator(this);
        setContentView(v);
        // Set back button in title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Small wait to let data be inserted in the room database
        new Thread(()->{
            try {
                Thread.sleep(2000);
                v.invalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //share button clicked
    public void shareClicked(View view) {
        if (!checkPermission()) { //if both permissions are granted, it would return 0, hence !checkPermission
            Uri shareUri = getBitmapUri(getBitmap(v));
            shareGraph(shareUri);
        }
        else {
            if (checkPermission()) { //if either or both permissions not set...
                requestPermissionAndContinue(); //ask user for permission
            } else { //otherwise, when set run as intended
                Uri shareUri = getBitmapUri(getBitmap(v));
                shareGraph(shareUri);
            }
        }
    }

    /* Load custom menu that includes the share button up top */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_share, menu);
        return true;
    }

    /* Detect which button was pressed in the top bar in case there were several*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_actionbar_btn:

                if (!checkPermission()) { //if both permissions are granted, it would return 0, hence !checkPermission
                    Uri shareUri = getBitmapUri(getBitmap(v));
                    shareGraph(shareUri);
                }
                else {
                    if (checkPermission()) { //if either or both permissions not set...
                        requestPermissionAndContinue(); //ask user for permission
                    } else { //otherwise, when set run as intended
                        Uri shareUri = getBitmapUri(getBitmap(v));
                        shareGraph(shareUri);
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Class extended in order to be able and share photos properly in SDK versions 24 and higher */
    public class GenericFileProvider extends FileProvider {}

    /* Function used to convert the View into a Bitmap that can be shared */
    public Bitmap getBitmap(View v){
        Bitmap viewToBmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Bitmap.Config.ARGB_8888); //make a bitmap the size of the view
        Canvas canvas = new Canvas(viewToBmap); // now we have a new canvas that contains our view in bitmap format
        canvas.drawColor(Color.WHITE); //give it a background colour before sharing
        v.draw(canvas);
        //return the bitmap generated
        return viewToBmap;
    }

    /* Using a bitmap, save it temporarily using FileOutputStream and get its URI so it can be accessed later */
    private Uri getBitmapUri(Bitmap nodeGraph) {
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "graph_tobe_shared.png"); //name by which we save the bitmap
            FileOutputStream stream = new FileOutputStream(file); //save the bitmap
            nodeGraph.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            /*
            DO NOT USE: uri = Uri.fromFile(file); not usable for SDK version >= 24
             */
            uri = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider", file); //retrieve uri where bitmap was stored
        } catch (IOException e) {
            Log.d(TAG, "Sharing error: " + e.getMessage());
        }
        return uri; //uri where the bitmap is stored
    }

    /* Function that fires the share intent in order to share the Bitmap */
    private void shareGraph(Uri uri){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri); //add the bitmap we saved to the intent through its uri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png"); //tell android what we're sending in the intent
        startActivity(intent);
    }

    /* Make sure the required permissions are set for the application */
    private boolean checkPermission() {
        /* if either read or write permissions are not granted, this will return 1*/
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    /*function used to ask for permissions if any are missing
    * code adapted from: https://stackoverflow.com/questions/47688354/java-io-filenotfoundexception-storage-emulated-0-downloadedfilem-jpg-permissi/47688482
    * */
    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                // set properties of the permission request pop-up window
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission required!");
                alertBuilder.setMessage("Read and write permissions are needed before sharing files");
                alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(NodeArcGraph.this, new String[]{WRITE_EXTERNAL_STORAGE
                        , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(NodeArcGraph.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            Uri shareUri = getBitmapUri(getBitmap(v));
            shareGraph(shareUri);
        }
    }

    /*Function used to retry the operation once the pop-up window has been acted on. If permissions granted, then launch share
    * function adapted from: https://stackoverflow.com/questions/47688354/java-io-filenotfoundexception-storage-emulated-0-downloadedfilem-jpg-permissi/47688482 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true; //permissions now granted by default, but check again
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) { //if either permission still not granted, false
                        flag = false;
                    }
                }
                if (flag) { //if permissions now granted, then launch share action
                    Uri shareUri = getBitmapUri(getBitmap(v));
                    shareGraph(shareUri);
                } else { finish(); }
            } else { finish(); }
        } else { super.onRequestPermissionsResult(requestCode, permissions, grantResults); }
    }
}
