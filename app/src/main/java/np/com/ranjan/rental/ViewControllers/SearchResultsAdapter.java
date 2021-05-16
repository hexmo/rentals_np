package np.com.ranjan.rental.ViewControllers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import np.com.ranjan.rental.Model.Product;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.ProductManagement.ViewProductActivity;

/**
 * This shows products on basis of search key
 */
public class SearchResultsAdapter extends FirebaseRecyclerAdapter<Product, SearchResultsAdapter.SearchViewHolder> {


    public SearchResultsAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull final Product product) {


        //Setting data
        Picasso.get().load(product.getPhoto()).resize(500,500).centerCrop().into(holder.imageView);
        holder.name.setText(product.getName());
        holder.price.setText("NRs: "+product.getCost()+"/day");

        //This method opens view product activity where user can see product details
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(view.getContext(), ViewProductActivity.class);
                intent.putExtra("productModel",product);
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridlayout, parent, false);

        return new SearchViewHolder(view);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        //Objects
        TextView name, price;
        ImageView imageView;
        MaterialCardView cardView;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            name = itemView.findViewById(R.id.item_name_grid_layout_resource);
            price = itemView.findViewById(R.id.item_price_grid_layout_resource);
            imageView = itemView.findViewById(R.id.image_grid_layout_resource);
            cardView = itemView.findViewById(R.id.my_product_card_view);
        }
    }

}
