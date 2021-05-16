package np.com.ranjan.rental.View.MainApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.Model.UserModel;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.UserManagement.EditProfileActivity;
import np.com.ranjan.rental.ViewControllers.LoadingClass;
import np.com.ranjan.rental.ViewControllers.MyProductsAdapter;

/**
 * This fragment shows user details.
 * It also shows all products uploaded by user.
 */
public class ProfileFragment extends Fragment {

    //Object and variables declaration
    Button buttonEditProfile;
    TextView textViewProfileName, textViewPhoneNumber;
    UserModel userModel;
    ImageView profileImageView;
    RecyclerView recyclerView;
    MyProductsAdapter myProductsAdapter;

    //Loading class(This class shows loading animation)
    private LoadingClass loadingClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Loading
        loadingClass = new LoadingClass(this.getActivity());

        //Hooks
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        textViewProfileName = view.findViewById(R.id.profile_name_text_view);
        textViewPhoneNumber = view.findViewById(R.id.profile_phone_no_text_view);
        profileImageView = view.findViewById(R.id.profile_fragment_image_view);
        recyclerView = view.findViewById(R.id.my_product_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));

        //Firebase query creation for firebase recycler view UI
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Products").orderByChild("owner").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Product.class)
                        .build();


        //creating adapter object
        myProductsAdapter = new MyProductsAdapter(options);

        //setting adapter to recycler view
        recyclerView.setAdapter(myProductsAdapter);



        //Fetch profile data from database and put it in required text views
        fetchAndFillData();

        //Adding onclick listener on edit profile button
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("userModel",userModel);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myProductsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myProductsAdapter.stopListening();
    }


    /**
     * This method call two method which display data in fragment.
     */
    private void fetchAndFillData(){

        fetchAndFillUserData();
        fetchAndFillMyProductsData();
    }


    /**
     * This methods fetched user data from firebase and displays it on view
     */
    private void fetchAndFillUserData() {

        //Starting load icon animation
        loadingClass.startLoading();

        //Getting reference for user data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                userModel = dataSnapshot.getValue(UserModel.class);

                //Setting data in profile fragment
                textViewProfileName.setText(userModel.getFullName());
                textViewPhoneNumber.setText(userModel.getPhoneNumber());

                //Setting profile image
                if(userModel.getProfilePicture() != null){
                    Picasso.get().load(userModel.getProfilePicture()).placeholder(R.drawable.blank_profile_picture).resize(200,200).centerCrop().into(profileImageView);
                }

                //Stopping loading icon
                loadingClass.dismissLoading();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database error: ", databaseError.toString());
            }
        });

    }


    /**
     * This method was replaced in onCreate
     */
    private void fetchAndFillMyProductsData() {

    }


}