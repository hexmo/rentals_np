package np.com.ranjan.rental.View.ProductManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.SearchResultsAdapter;

/**
 * this activity shows search result
 * The search is extremely case sensitive so needs to be upgraded in future.
 */
public class SearchResultsActivity extends AppCompatActivity {
    //Objects and variables
    RecyclerView recyclerView;
    SearchResultsAdapter searchResultsAdapter;

    String searchKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        //for recycler view
        recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //getting search key from home fragment which was passed with intent
        searchKey = (String) getIntent().getSerializableExtra("searchKey");

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.
                                getInstance().getReference().child("Products").
                                orderByChild("name").startAt(searchKey).endAt(searchKey+"\uf8ff"), Product.class).build();


        searchResultsAdapter = new SearchResultsAdapter(options);

        recyclerView.setAdapter(searchResultsAdapter);
    }

    /**
     * Closes activity when back arrow is pressed on top left corner of screen.
     * @param view View
     */
    public void cancelSearchProduct(View view) {
        finish();

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