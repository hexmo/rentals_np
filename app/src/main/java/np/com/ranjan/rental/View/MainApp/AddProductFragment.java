package np.com.ranjan.rental.View.MainApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.LoadingClass;

public class AddProductFragment extends Fragment {

    //Objects and variables
    Button submitButton;
    TextView uploadImageButton;
    TextInputLayout textItemName, textItemPrice, textItemDescription ;
    ImageView imageView;
    Spinner categorySpinner;

    //for image
    Uri imageUri;
    int deviceWidth;

    //Loading class
    private LoadingClass loadingClass;

    //User id
    private String firebaseUserID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_product, container, false);

        //Loading class initialization
        loadingClass = new LoadingClass(this.getActivity());

        //calculateDeviceWidth
        calculateDeviceWidth();

        //Hooks
        textItemName = view.findViewById(R.id.add_item_name);
        textItemPrice = view.findViewById(R.id.add_item_price);
        textItemDescription = view.findViewById(R.id.add_item_description);
        imageView = view.findViewById(R.id.addProductImageView);
        categorySpinner = view.findViewById(R.id.add_item_category_spinner);
        uploadImageButton = view.findViewById(R.id.textViewAddItem);
        submitButton = view.findViewById(R.id.button_add_product);


        //Choose image from gallery and show in image view
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItemImage();
            }
        });


        //Submit button on click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadItem();
            }
        });

        return view;
    }

    /**
     * This method is responsible for uploading items to firebase by reading them from UI.
     */
    private void uploadItem() {
        if (validateProductName() && validateProductPrice() && validateProductDescription() && validateImage()){

            //preparing for object creation
            String _id = FirebaseDatabase.getInstance().getReference("Products").push().getKey();
            String _owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String _name = textItemName.getEditText().getText().toString().trim();
            String _cost = textItemPrice.getEditText().getText().toString().trim();
            String _category = categorySpinner.getSelectedItem().toString();
            String _description = textItemDescription.getEditText().getText().toString().trim();
            String _photo = null;

            //Creating product object
            final Product productModelObj = new Product(_id,_owner,_name,_cost,_category,_description,_photo);

            //Pushing data to firebase database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
            databaseReference.child(_id).setValue(productModelObj).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase_fail: ", e.toString());
                }
            });


            //Uploading photo
            final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference(_id+".jpg");

            //loading class (start showing loading icon)
            loadingClass.startLoading();

            imageStorageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //this will reload fragment
                    imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //Storing url of uploaded images in database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(productModelObj.getId());
                            String finalImageUrl = uri.toString().replace(".jpg","_1000x1000.jpg");
                            databaseReference.child("photo").setValue(finalImageUrl).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("P_PIC: ",e.toString());
                                }
                            });
                        }
                    });

                    //This method will reload fragment
                    initiateFragmentReload();

                    //dismiss loading (This will stop the loading icon)
                    loadingClass.dismissLoading();
                }
            });


        }
        else {
            Toast.makeText(this.getActivity(), "Please select all fields properly.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method will reolad the fragment so all text fields and image view will be empty
     */
    private void initiateFragmentReload() {
        Toast.makeText(this.getActivity(), "Successfully uploaded product.", Toast.LENGTH_SHORT).show();
        //Reloading fragment
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }


    /**
     * It calculates device width so that we can display image image flush with the screen
     */
    private void calculateDeviceWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
    }


    /**
     * This method is responsible for selecting image from gallery.
     */
    private void selectItemImage() {
        //Intent to open gallery to select image
        Intent openGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent,1000);
    }

    /**
     * @param requestCode Request code for android gallery
     * @param resultCode code that determines if data was successful
     * @param data Intent object
     */
    //Method that checks whether image was selected from library
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                //using picasso library to crop and view image in image viewer
                Picasso.get().load(imageUri).resize(deviceWidth,deviceWidth).centerCrop().into(imageView);
            }
        }
    }

    //Validation methods

    /**
     * @return true if product name was okay
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
     * @return true if product description was okay
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
     * @return true if product price was okay
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
     * This method checks whether the user has selected the image or not.
     * @return boolean
     */
    private boolean validateImage(){
        if(imageUri != null && !imageUri.equals(Uri.EMPTY)){
            return true;
        }else {
            Toast.makeText(this.getActivity(), "Please select image.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}