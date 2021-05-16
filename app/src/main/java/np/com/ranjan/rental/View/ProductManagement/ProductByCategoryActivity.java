package np.com.ranjan.rental.View.ProductManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.ViewControllers.SearchResultsAdapter;

public class ProductByCategoryActivity extends AppCompatActivity {
    //Objects and variables
    RecyclerView recyclerView;
    SearchResultsAdapter searchResultsAdapter;
    TextView categoryHeading;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        category = (String) getIntent().getSerializableExtra("category");

        categoryHeading = findViewById(R.id.category_header);

        categoryHeading.setText(category);


        //for recycler view
        recyclerView = findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


//        FirebaseRecyclerOptions<Product> options =
//                new FirebaseRecyclerOptions.Builder<Product>()
//                        .setQuery(FirebaseDatabase.
//                                getInstance().getReference().child("Products").equalTo("category",category), Product.class).build();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
        .setQuery(FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").equalTo(category), Product.class).build();


        searchResultsAdapter = new SearchResultsAdapter(options);

        recyclerView.setAdapter(searchResultsAdapter);
    }

    public void cancelCategoryProduct(View view) {
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