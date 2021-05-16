package np.com.ranjan.rental.View.ProductManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

/**
 * Activity to change product details.
 */
public class EditProductActivity extends AppCompatActivity {

    //Objects and variables
    Button submitButton;
    TextView uploadImageButton;
    TextInputLayout textItemName, textItemPrice, textItemDescription;
    ImageView imageView;
    Spinner categorySpinner;


    //for image
    Uri imageUri;
    int deviceWidth;

    //was photo changed variable
    private static boolean WAS_PHOTO_CHANGED = false;

    //Loading class
    private LoadingClass loadingClass;

    //User id
    private String firebaseUserID;

    //Product obj
    Product productObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //Loading class initialization
        loadingClass = new LoadingClass(this);

        //calculateDeviceWidth
        calculateDeviceWidth();

        //getting intent data
        productObj = (Product) getIntent().getSerializableExtra("productModel");

        //hooks
        textItemName = findViewById(R.id.edit_Product_name);
        textItemPrice = findViewById(R.id.edit_Product_price);
        textItemDescription = findViewById(R.id.edit_Product_description);
        imageView = findViewById(R.id.edit_Product_ImageView);
        categorySpinner = findViewById(R.id.edit_Product_category_spinner);
        uploadImageButton = findViewById(R.id.edit_Product_textView);
        submitButton = findViewById(R.id.button_edit_Product);

        //set values
        fillProductData();
    }

    /**
     * This fills product data in form.
     */
    private void fillProductData() {
        textItemName.getEditText().setText(productObj.getName());
        textItemPrice.getEditText().setText(productObj.getCost());
        textItemDescription.getEditText().setText(productObj.getDescription());

        //Spinner
        String _category = productObj.getCategory();

        switch (_category) {
            case "Vehicles and Transport":
                categorySpinner.setSelection(0);
                break;
            case "Real estate":
                categorySpinner.setSelection(1);
                break;
            case "Musical Instruments":
                categorySpinner.setSelection(2);
                break;
            case "Video, Photo and Audio":
                categorySpinner.setSelection(3);
                break;
            case "Tools and Machinery":
                categorySpinner.setSelection(4);
                break;
            case "Clothing":
                categorySpinner.setSelection(5);
                break;
            case "Electronics":
                categorySpinner.setSelection(6);
                break;
            case "Others":
                categorySpinner.setSelection(7);
                break;

        }

        //Image
        Picasso.get().load(productObj.getPhoto()).resize(deviceWidth, deviceWidth).centerCrop().into(imageView);

    }

    /**
     * Calculates device width to images can be displayed to edge of devices.
     */
    private void calculateDeviceWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
    }


    /**
     * This method ends activity
     *
     * @param view View object
     */
    public void cancelEditProduct(View view) {
        finish();
    }


    /**
     * This method changes image
     *
     * @param view view
     */
    public void changeProductImage(View view) {
        //Intent to open gallery to select image
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }

    /**
     * @param requestCode Request code for android gallery
     * @param resultCode  code that determines if data was successful
     * @param data        Intent object
     */
    //Method that checks whether image was selected from library
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                //using picasso library to crop and view image in image viewer
                Picasso.get().load(imageUri).resize(deviceWidth, deviceWidth).centerCrop().into(imageView);
                WAS_PHOTO_CHANGED = true;
            }
        }
    }


    /**
     * This methods updates product details in firebase database
     * @param view View
     */
    public void updateProduct(View view) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(productObj.getId());

        if (validateProductName() && validateProductPrice() && validateProductDescription()) {

            //Name update
            if (!productObj.getName().equals(textItemName.getEditText().getText().toString().trim())) {

                databaseReference.child("name").setValue(textItemName.getEditText().getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FIRE_BASE---", "Item name update failed : " + e.toString());
                    }

                    });
            }

            //Price update
            if (!productObj.getCost().equals(textItemPrice.getEditText().getText().toString().trim())) {

                databaseReference.child("cost").setValue(textItemPrice.getEditText().getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FIRE_BASE---", "Item Cost update failed : " + e.toString());
                    }

                });
            }

            //Description update
            if (!productObj.getDescription().equals(textItemDescription.getEditText().getText().toString().trim())) {

                databaseReference.child("description").setValue(textItemDescription.getEditText().getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FIRE_BASE---", "Item Description update failed : " + e.toString());
                    }

                });
            }

            //Category update
            databaseReference.child("category").setValue(categorySpinner.getSelectedItem().toString()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FIRE_BASE---", "Item Description update failed : " + e.toString());
                }

            });



            //image check and update
            imageUpdate();

            //display toast
            Toast.makeText(EditProductActivity.this, "Successfully Updated Product.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Function that updates product image in firebase
     */
    private void imageUpdate() {
        if(WAS_PHOTO_CHANGED){
            //Uploading photo
            final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference(productObj.getId()+".jpg");


            //loading class
            loadingClass.startLoading();

            imageStorageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //this will reload fragment
                    imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //Storing url of uploaded images in database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(productObj.getId());
                            String finalImageUrl = uri.toString().replace(".jpg","_1000x1000.jpg");
                            databaseReference.child("photo").setValue(finalImageUrl).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("P_PIC: ",e.toString());
                                }
                            });
                        }
                    });
                    //dismiss loafing
                    loadingClass.dismissLoading();
                    Toast.makeText(EditProductActivity.this, "Successfully Updated Product.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    //Validation methods

    /**
     * @return true is product name is correct
     */
    private boolean validateProductName() {
        String value = textItemName.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            textItemName.setError("Item name cannot be left empty.");
        } else {
            textItemName.setError(null);
            textItemName.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true is product description is correct
     */
    private boolean validateProductDescription() {
        String value = textItemDescription.getEditText().getText().toString();
        if (value.isEmpty()) {
            textItemDescription.setError("Item description cannot be left empty.");
        } else {
            textItemDescription.setError(null);
            textItemDescription.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * @return true is product price is correct
     */
    private boolean validateProductPrice() {
        String value = textItemPrice.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            textItemPrice.setError("Item price cannot be left empty.");
        }else if(Integer.valueOf(value).equals(0)){
            textItemPrice.setError("Item price cannot 0.");
        }
        else {
            textItemPrice.setError(null);
            textItemPrice.setErrorEnabled(false);
            return true;
        }
        return false;
    }



    /**
     * Method to delete method
     * @param view View
     */
    public void deleteProduct(View view) {
        DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("Products").child(productObj.getId());
        itemReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditProductActivity.this, "Successfully deleted product.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}