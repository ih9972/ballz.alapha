package com.example.ballzalapha;

import static com.example.ballzalapha.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {
    EditText EmailET;
    EditText PasswordET;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailET = findViewById(R.id.EmailET);
        PasswordET = findViewById(R.id.PasswordET);
        context = this;
    }

    public void creatUser(View view) {
        String email = EmailET.getText().toString();
        String password = PasswordET.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fiil all fildes", Toast.LENGTH_LONG).show();
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Creating user...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(context, "User created successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Exception exp = task.getException();

                                if (exp instanceof FirebaseAuthInvalidUserException)
                                    Toast.makeText(context, "Invalid email address", Toast.LENGTH_LONG).show();
                                else if (exp instanceof FirebaseAuthWeakPasswordException)
                                    Toast.makeText(context, "Password too weak", Toast.LENGTH_LONG).show();
                                else if (exp instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
                                else if (exp instanceof FirebaseAuthInvalidCredentialsException)
                                    Toast.makeText(context, "General authentication failure", Toast.LENGTH_LONG).show();
                                else if (exp instanceof FirebaseNetworkException)
                                    Toast.makeText(context, "Network error. Please check your connection", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(context, "An error occurred, please try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.PicGallery){
            Intent si = new Intent(this,PicFromGallery.class);
            startActivity(si);
        }
        else if(id == R.id.PicCamera){
            Intent si = new Intent(this,com.example.ballzalapha.PicFromCamera.class);
            startActivity(si);
        }
        else if(id == R.id.Location){
            Intent si = new Intent(this,com.example.ballzalapha.Locition_Tracking.class);
            startActivity(si);
        }
        else if(id == R.id.NFC){
            Intent si = new Intent(this,com.example.ballzalapha.NFC_Reading.class);
            startActivity(si);
        }

            return true;
    }
}