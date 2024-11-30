package com.example.ballzalapha;

import static com.example.ballzalapha.FBRef.refStamp;
import static com.example.ballzalapha.FBRef.storageRef;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PicFromCamera extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1021;
    private  ImageView iv;
    private Uri uri;
    private static final int REQUEST_STAMP_CAPTURE = 201;
    private StorageReference refImg;
    private Bitmap imageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_from_camera);
        iv = findViewById(R.id.cameraIV);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
          }

    /**
     * takeStamp method
     * <p> Taking a photo by camera to upload to Firebase Storage
     * </p>
     *
     * @param view the view that triggered the method
     */
    public void take_pic(View view) {
        Intent takePicIntent = new Intent();
        takePicIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, REQUEST_STAMP_CAPTURE);
        }
    }

    /**
     * onRequestPermissionsResult method
     * <p> Method triggered by other result of permissions request
     * </p>
     *
     * @param requestCode the request code triggered the activity
     * @param permissions the array of permissions granted
     * @param grantResults the array of permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data_back) {
        super.onActivityResult(requestCode, resultCode, data_back);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_STAMP_CAPTURE) {
                // Upload camera thumbnail image file
                    Bundle extras = data_back.getExtras();
                    if (extras != null) {
                        uri = data_back.getData();
                        refImg = refStamp.child(uri.getLastPathSegment() + ".png");
                        imageBitmap = (Bitmap) extras.get("data");
                        iv.setImageBitmap(imageBitmap);
                    }
            }
        }
    }

    public void upload_camera_to_storege(View view) {
        final ProgressDialog pd;
        pd = ProgressDialog.show(this, "Upload image", "Uploading...", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        refImg.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(PicFromCamera.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(PicFromCamera.this, "Upload failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.AddUser){
            Intent si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(id == R.id.NFC){
            Intent si = new Intent(this,NFC_Reading.class);
            startActivity(si);
        }
        else if(id == R.id.Location){
            Intent si = new Intent(this,Locition_Tracking.class);
            startActivity(si);
        }
        else if(id == R.id.PicGallery){
            Intent si = new Intent(this,PicFromGallery.class);
            startActivity(si);
        }

        return true;
    }

}