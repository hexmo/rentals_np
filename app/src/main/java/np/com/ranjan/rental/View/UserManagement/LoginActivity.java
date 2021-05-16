package np.com.ranjan.rental.View.UserManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import np.com.ranjan.rental.View.MainApp.MainActivity;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

/**
 * This activity id for handling login
 */
public class LoginActivity extends AppCompatActivity {
    //Variables and Objects declaration
    TextInputLayout loginEmail, loginPassword;

    //For firebase
    private FirebaseAuth mAuth;

    //Loading class
    private LoadingClass loadingClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hooks
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);

        //Loading
        loadingClass = new LoadingClass(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    /**
     * Call SignUpActivity
     * @param view View
     */
    public void gotToSignUpActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method to call login method
     * @param view View
     */
    public void login(View view) {
        String userEmail = loginEmail.getEditText().getText().toString().trim();
        String userPassword = loginPassword.getEditText().getText().toString();
        if(validatePassword() && validateEmail()){

            //Calling loading icon
            loadingClass.startLoading();

            mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        //Stopping loading icon
                        loadingClass.dismissLoading();

                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //Stopping loading icon
                    loadingClass.dismissLoading();
                }
            });
        }
    }

    /**
     * @return true if email is correct
     */
    private boolean validateEmail() {
        String value = loginEmail.getEditText().getText().toString().trim();
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]+";
        if (value.isEmpty()) {
            loginEmail.setError("Email field cannot be left empty.");
        } else if (!value.matches(pattern)) {
            loginEmail.setError("Invalid email address.");
        } else {
            loginEmail.setError(null);
            loginEmail.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true if password is correct
     */
    private boolean validatePassword() {
        String value = loginPassword.getEditText().getText().toString();
        if (value.isEmpty()) {
            loginPassword.setError("Password cannot be empty.");
        } else {
            loginPassword.setError(null);
            loginPassword.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    public void gotToResetEmail(View view) {
        Intent intent = new Intent(getApplicationContext(), EnterEmailActivity.class);
        startActivity(intent);
    }
}