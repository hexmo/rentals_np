package np.com.ranjan.rental.View.UserManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import np.com.ranjan.rental.Model.UserModel;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

/**
 * Activity which handles sign up
 */
public class SignUpActivity extends AppCompatActivity {

    //Variables and Objects declaration
    TextInputLayout signUpFullName, signUpPhoneNumber, signUpEmail, signUpPassword, signUpReenterPassword;

    //For firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //Other variables
    String fireBaseUserId;

    //Loading class
    private LoadingClass loadingClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Loading
        loadingClass = new LoadingClass(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Hooks
        signUpFullName = findViewById(R.id.signup_full_name);
        signUpPhoneNumber = findViewById(R.id.signup_phone_number);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpReenterPassword = findViewById(R.id.signup_password_re_enter);

    }

    /**
     * Calls LoginActivity
     * @param view View
     */
    public void gotToLoginActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Sign up method
     * @param view View
     */
    public void signUp(View view) {
        if (validateFullName() && validatePhoneNumber() && validateEmail() && validatePassword() && validateReenterPassword()) {
            String userEmail = signUpEmail.getEditText().getText().toString().trim();
            String userPassword = signUpPassword.getEditText().getText().toString();

            //loading animation start
            loadingClass.startLoading();

            //Creating user account in firebase database as adding more user data
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Adding other details of user in fire store db

                        //Getting values from sign up form
                        String _id = mAuth.getCurrentUser().getUid();
                        String _userFullName = signUpFullName.getEditText().getText().toString().trim();
                        String _userPhoneNumber = signUpPhoneNumber.getEditText().getText().toString().trim();

                        //Creating userModel object to insert into the database
                        UserModel userModelObj = new UserModel(_id, _userFullName, _userPhoneNumber, null);

                        //Pushing data to firebase database
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(_id).setValue(userModelObj).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Firebase_fail: ", e.toString());
                            }
                        });

                        //Stopping loading icon
                        loadingClass.dismissLoading();

                        //Toast.makeText(SignUpActivity.this, "Successfully created account.", Toast.LENGTH_SHORT).show();

                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(SignUpActivity.this, "Successfully created account.\nPlease Sign In To Continue.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //Stopping loading icon
                    loadingClass.dismissLoading();
                }
            });
        }
    }

    /**
     * @return true if full name is valid
     */
    private boolean validateFullName() {
        String value = signUpFullName.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            signUpFullName.setError("Full name cannot be left empty.");
        } else {
            signUpFullName.setError(null);
            signUpFullName.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if phone is valid
     */
    private boolean validatePhoneNumber() {
        String value = signUpPhoneNumber.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            signUpPhoneNumber.setError("Phone number cannot be empty.");
        } else if (value.length() < 10) {
            signUpPhoneNumber.setError("Phone number should be 10 characters long.");
        } else if (!value.substring(0, 2).equals("98")) {
            signUpPhoneNumber.setError("Phone number should start with 98.");
        } else {
            signUpPhoneNumber.setError(null);
            signUpPhoneNumber.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if email is valid
     */
    private boolean validateEmail() {
        String value = signUpEmail.getEditText().getText().toString().trim();
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]+";
        if (value.isEmpty()) {
            signUpEmail.setError("Email field cannot be left empty.");
        } else if (!value.matches(pattern)) {
            signUpEmail.setError("Invalid email address.");
        } else {
            signUpEmail.setError(null);
            signUpEmail.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if password is valid
     */
    private boolean validatePassword() {
        String value = signUpPassword.getEditText().getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}";
        if (value.isEmpty()) {
            signUpPassword.setError("Password cannot be empty.");
        } else if (value.length() < 8) {
            signUpPassword.setError("Password should be at least 8 characters long.");
        } else if (!value.matches(pattern)) {
            signUpPassword.setError("Password should contain at least one special character and one number.");
        } else {
            signUpPassword.setError(null);
            signUpPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if full both password are same
     */
    private boolean validateReenterPassword() {
        String value = signUpReenterPassword.getEditText().getText().toString();
        if (value.isEmpty()) {
            signUpReenterPassword.setError("This field cannot be left empty.");
        } else if (!value.equals(getSignUpPassword())) {
            signUpReenterPassword.setError("Both passwords do not match.");
        } else {
            signUpReenterPassword.setError(null);
            signUpReenterPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return value of first password field
     */
    private String getSignUpPassword() {
        return signUpPassword.getEditText().getText().toString();
    }
}