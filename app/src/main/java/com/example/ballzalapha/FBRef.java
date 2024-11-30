package com.example.ballzalapha;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBRef{
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();

    public static StorageReference refST = storage.getReference();
    public static StorageReference refStamp = refST.child("Stamps");


}
