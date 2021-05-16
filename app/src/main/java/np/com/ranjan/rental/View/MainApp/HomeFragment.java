package np.com.ranjan.rental.View.MainApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.ProductManagement.SearchResultsActivity;
import np.com.ranjan.rental.ViewControllers.SearchResultsAdapter;


public class HomeFragment extends Fragment {

    //Objects and variables


    EditText searchText;
    ImageView searchIcon;

    RecyclerView recyclerView;

    //Creating object of searchResults Adapter(This is a multi purporse adapter)
    SearchResultsAdapter searchResultsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


        //Hooks
        searchText = view.findViewById(R.id.search_key_home_fragment);
        searchIcon = view.findViewById(R.id.search_icon_home_fragment);
        recyclerView = view.findViewById(R.id.home_recycler_view);


        //Action listener
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _searchKey = searchText.getText().toString().trim();
                if(_searchKey != "") {
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra("searchKey", _searchKey);
                    startActivity(intent);
                }else{
                    Toast.makeText(view.getContext(), "Please enter search phrase.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Setting layout
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        //getting search key from home fragment which was passed with intent
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.
                                getInstance().getReference().child("Products").
                                orderByChild("name"), Product.class).build();


        //Initializing object
        searchResultsAdapter = new SearchResultsAdapter(options);


        //setting adapter to recycler view
        recyclerView.setAdapter(searchResultsAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchResultsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        searchResultsAdapter.stopListening();
    }
}

