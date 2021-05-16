package np.com.ranjan.rental.View.UserManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import np.com.ranjan.rental.Model.UserModel;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.Onboarding.SplashScreen;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

/**
 * This activity allows user to edit their profile.
 */
public class EditProfileActivity extends AppCompatActivity {

    //Firebase user
    FirebaseUser firebaseUser;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    //Objects and variables create
    TextInputLayout formFullName, formEmail, formPhoneNumber , formNewPassword,formReNewPassword;
    ImageView profileImageView;

    //Creating object of user model
    UserModel userModel;

    //Loading class
    private LoadingClass loadingClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Firebase storage reference setting value
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting userModel from profiled fragment which was passed with intent
        userModel = (UserModel) getIntent().getSerializableExtra("userModel");

        //Loading
        loadingClass = new LoadingClass(this);

        //Hooks
        formFullName = findViewById(R.id.update_full_name);
        formEmail = findViewById(R.id.update_email);
        formPhoneNumber = findViewById(R.id.update_phone_number);
        profileImageView = findViewById(R.id.profile_fragment_image_view);

        formNewPassword = findViewById(R.id.edit_password);
        formReNewPassword = findViewById(R.id.edit_password_re_enter);


        //calling fillData method to fill data in form
        fillData();
    }


    /**
     * Function to set name, email and phone no as well as photo in activity
     */
    public void fillData() {
        formEmail.getEditText().setText(firebaseUser.getEmail());
        formFullName.getEditText().setText(userModel.getFullName());
        formPhoneNumber.getEditText().setText(userModel.getPhoneNumber());

        //Setting profile image
        if(userModel.getProfilePicture() != null){
            Picasso.get().load(userModel.getProfilePicture()).resize(200,200).centerCrop().into(profileImageView);
        }
    }

    /**
     * Function to go back to main activity
     * @param view View
     */
    public void goBackToProfileFragment(View view) {
        finish();
    }

    /**
     * Function to update account details
     * @param view View
     */
    public void updateAccountDetails(View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        if (validateFullName() && validateEmail() && validatePhoneNumber()) {

            //Starting loading icon
            loadingClass.startLoading();

            //Updating name if it was changed.
            if (!userModel.getFullName().equals(formFullName.getEditText().getText().toString())){
                databaseReference.child("fullName").setValue(formFullName.getEditText().getText().toString()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FIRE_BASE---","Update first name failed : "+e.toString());
                    }
                });
            }

            //Updating phone no if it was changed.
            if (!userModel.getPhoneNumber().equals(formPhoneNumber.getEditText().getText().toString())){
                databaseReference.child("phoneNumber").setValue(formPhoneNumber.getEditText().getText().toString()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FIRE_BASE---","Update phone number failed : "+e.toString());
                    }
                });
            }

            //Updating email if it was changed.
            if (!firebaseUser.getEmail().equals(formEmail.getEditText().getText().toString())) {

                firebaseUser.updateEmail(formEmail.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //oldEmail = firebaseUser.getEmail();
                        Log.d("UPDATE_EMAIL", "Successfully update email.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Sign out and login to change email.", Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_EMAIL", "Email update failed: " + e.toString());
                    }
                });
            }


            //Stopping loading icon
            loadingClass.dismissLoading();

            Toast.makeText(EditProfileActivity.this, "Successfully Updated Account", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Method to change profile picture
     * @param view  View
     */
    public void changeProfilePicture(View view) {
        //Intent to open gallery to select image
        Intent openGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent,1000);
    }

    /**
     * Method that checks whether image was selected from library
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //Calling method to upload image to firebase
                uploadImageToFirebase(imageUri);
            }
        }
    }

    /**
     * Function to upload selected image to firebase
     * @param imageUri uri of image selected from gallery
     */
    private void uploadImageToFirebase(Uri imageUri) {

        final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()+".jpg");

        //start loading dialog
        loadingClass.startLoading();

        imageStorageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //Using picasso library to crop and view image in image viewer after it has been uploaded to firebase
                        Picasso.get().load(uri).resize(200,200).centerCrop().into(profileImageView);

                        //Storing url of uploaded images in database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        String finalImageUrl = uri.toString().replace(".jpg","_1000x1000.jpg");
                        databaseReference.child("profilePicture").setValue(finalImageUrl).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("P_PIC: ",e.toString());
                            }
                        });
                    }
                });

                //dismiss loading dialog
                loadingClass.dismissLoading();

                Toast.makeText(EditProfileActivity.this, "Successfully updated profile picture.", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    //Text field validators

    /**
     * @return true if full name is valid
     */
    private boolean validateFullName() {
        String value = formFullName.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            formFullName.setError("Full name cannot be left empty.");
        } else {
            formFullName.setError(null);
            formFullName.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if phone no is valid
     */
    private boolean validatePhoneNumber() {
        String value = formPhoneNumber.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            formPhoneNumber.setError("Phone number cannot be empty.");
        } else if (value.length() < 10) {
            formPhoneNumber.setError("Phone number should be 10 characters long.");
        } else if (!value.substring(0, 2).equals("98")) {
            formPhoneNumber.setError("Phone number should start with 98.");
        } else {
            formPhoneNumber.setError(null);
            formPhoneNumber.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if email is valid
     */
    private boolean validateEmail() {
        String value = formEmail.getEditText().getText().toString().trim();
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]+";
        if (value.isEmpty()) {
            formEmail.setError("Email field cannot be left empty.");
        } else if (!value.matches(pattern)) {
            formEmail.setError("Invalid email address.");
        } else {
            formEmail.setError(null);
            formEmail.setErrorEnabled(false);
            return true;
        }
        return false;
    }


    private boolean validatePassword() {
        String value = formNewPassword.getEditText().getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}";
        if (value.isEmpty()) {
            formNewPassword.setError("Password cannot be empty.");
        } else if (value.length() < 8) {
            formNewPassword.setError("Password should be at least 8 characters long.");
        } else if (!value.matches(pattern)) {
            formNewPassword.setError("Password should contain at least one special character and one number.");
        } else {
            formNewPassword.setError(null);
            formNewPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }


    /**
     * Method to logout
     * @param view View
     */
    public void accountLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Successfully signed out.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
        finish();
    }

    public void updatePassword(View view) {
        if(validatePasswords() && validateRePassport()){
            LoadingClass loadingClass = new LoadingClass(this);

            loadingClass.startLoading();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            currentUser.updatePassword(formNewPassword.getEditText().getText().toString());

            loadingClass.dismissLoading();

            Toast.makeText(this, "Successfully changed password.", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validateRePassport() {
        String value = formNewPassword.getEditText().getText().toString();
        String value2 = formReNewPassword.getEditText().getText().toString();
        if(value2.isEmpty()){
            formReNewPassword.setError("Password cannot be empty.");
        }else if(!value.equals(value2)){
            formReNewPassword.setError("Both passwords do not match.");
        }else{
            formReNewPassword.setError(null);
            formReNewPassword.setErrorEnabled(false);
            return true;
        }

        return false;
    }

    private boolean validatePasswords() {
        String value = formNewPassword.getEditText().getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}";
        if (value.isEmpty()) {
            formNewPassword.setError("Password cannot be empty.");
        } else if (value.length() < 8) {
            formNewPassword.setError("Password should be at least 8 characters long.");
        } else if (!value.matches(pattern)) {
            formNewPassword.setError("Password should contain at least one special character and one number.");
        } else {
            formNewPassword.setError(null);
            formNewPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    public void deleteAccount(View view) {

        loadingClass.startLoading();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        //https://stackoverflow.com/questions/37390864/how-to-delete-from-firebase-realtime-database
        Query query = FirebaseDatabase.getInstance()
                .getReference("Products")
                .orderByChild("owner")
                .equalTo(currentUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    productSnapshot.getRef().removeValue();
                }

                currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Successfully deleted account and your listings.", Toast.LENGTH_SHORT).show();

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Account deletion failed." + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RealTime DB:", "User item deletion unsuccessful. ", databaseError.toException());
            }
        });
    }
}