package np.com.ranjan.rental.View.UserManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

public class EnterEmailActivity extends AppCompatActivity {
    TextInputLayout loginEmail;
    //For firebase
    private FirebaseAuth mAuth;

    //Loading class
    private LoadingClass loadingClass;

    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        //Hooks
        loginEmail = findViewById(R.id.reset_email);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    public void resetPassword(View view) {

        if(emailValid()) {

            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EnterEmailActivity.this, "Please check your email.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EnterEmailActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private boolean emailValid() {
        emailAddress = loginEmail.getEditText().getText().toString().trim();
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]+";
        if (emailAddress.isEmpty()) {
            loginEmail.setError("Email field cannot be left empty.");
        } else if (!emailAddress.matches(pattern)) {
            loginEmail.setError("Invalid email address.");
        } else {
            loginEmail.setError(null);
            loginEmail.setErrorEnabled(false);
            return true;
        }
        return false;
    }
}