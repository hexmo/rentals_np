package np.com.ranjan.rental.View.ProductManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.Model.UserModel;
import np.com.ranjan.rental.R;

/**
 * This activity shows product details.
 */
public class ViewProductActivity extends AppCompatActivity {

    //Objects and variables
    TextView productName, productPrice, productCategory, productDesc, titleBar, sellerName;
    ImageView productImage;

    int deviceWidth;


    //Product obj
    Product productObj;
    UserModel userObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        //Hooks
        productName = findViewById(R.id.view_product_name);
        productPrice = findViewById(R.id.view_product_price);
        productCategory = findViewById(R.id.view_product_category);
        productDesc = findViewById(R.id.view_product_description);
        titleBar = findViewById(R.id.textView_my_products);
        productImage = findViewById(R.id.view_product_image);
        sellerName = findViewById(R.id.view_product_seller_name);


        //Get data from Intent
        productObj = (Product) getIntent().getSerializableExtra("productModel");

        //calculateDeviceWidth
        calculateDeviceWidth();

        //set data
        setData();

        //Showing list of user products from firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(productObj.getOwner());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userObj = dataSnapshot.getValue(UserModel.class);

                if(userObj != null){
                sellerName.setText("Item listed by: " +  userObj.getFullName());}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database error: ", databaseError.toString());
            }
        });


    }

    /**
     * This method sets product info in view
     */
    private void setData() {
        productName.setText(productObj.getName());
        productPrice.setText(String.format("NRs: %s/day", productObj.getCost()));
        productCategory.setText(String.format("Category: %s", productObj.getCategory()));
        productDesc.setText(productObj.getDescription());
        titleBar.setText(productObj.getName());

        //Image
        Picasso.get().load(productObj.getPhoto()).resize(deviceWidth, deviceWidth).centerCrop().into(productImage);
    }


    /**
     * Closes the view product activity
     * @param view View
     */
    public void closeViewProduct(View view) {
        finish();
    }

    /**
     * Calculates device width so that image can sit flush with the edge of screen
     */
    private void calculateDeviceWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
    }


    /**
     * This method opens phone dialler with product owner information
     * @param view View
     */
    public void openPhoneDialler(View view) {

        String tel = userObj.getPhoneNumber();

        // Use format with "tel:" and phoneNumber created is stored in u.
        Uri uri = Uri.parse("tel:" + tel);

        // Create the intent and set the data for the intent as the phone number.
        Intent i = new Intent(Intent.ACTION_DIAL, uri);

        try {
            // Launch the Phone app's dialer with a phone number to dial a call.
            startActivity(i);
        } catch (SecurityException s) {
            // show() method display the toast with exception message.
            Toast.makeText(this, s.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}