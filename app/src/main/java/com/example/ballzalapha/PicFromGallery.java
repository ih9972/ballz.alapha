package com.example.ballzalapha;

import static com.example.ballzalapha.FBRef.storageRef;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PicFromGallery extends AppCompatActivity {
    private ImageView pic;
    public Uri imageUri;
    Context context =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_from_gallery);
        pic = findViewById(R.id.iv);

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
        else if(id == R.id.PicCamera){
            Intent si = new Intent(this,PicFromCamera.class);
            startActivity(si);
        }
        else if(id == R.id.Location){
            Intent si = new Intent(this,Locition_Tracking.class);
            startActivity(si);
        }
        else if(id == R.id.NFC){
            Intent si = new Intent(this,NFC_Reading.class);
            startActivity(si);
        }

        return true;
    }

    public void addpic(View view) {
        choosePicture();

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            imageUri = data.getData();
            uploadPicture(imageUri);
            pic.setImageURI(imageUri);
        }
    }

    private void uploadPicture(Uri uri) {
        StorageReference riversRef = storageRef.child("gallery/" + uri.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();

            }
        });
    }
}