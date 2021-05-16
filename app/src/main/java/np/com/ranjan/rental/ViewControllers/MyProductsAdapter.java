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
import np.com.ranjan.rental.View.ProductManagement.EditProductActivity;

/**
 * This adapter shows only users product
 */
public class MyProductsAdapter extends FirebaseRecyclerAdapter<Product, MyProductsAdapter.MyProductViewHolder> {


    public MyProductsAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyProductViewHolder holder, int position, @NonNull final Product product) {

        //Set data on front end
        Picasso.get().load(product.getPhoto()).resize(500,500).centerCrop().into(holder.imageView);
        holder.name.setText(product.getName());
        holder.price.setText(String.format("NRs: %s/day", product.getCost()));

        //Opens edit product activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(view.getContext(), EditProductActivity.class);
                intent.putExtra("productModel",product);
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridlayout, parent, false);

        return new MyProductViewHolder(view);
    }

    class MyProductViewHolder extends RecyclerView.ViewHolder {

        //Objects
        TextView name, price;
        ImageView imageView;
        MaterialCardView cardView;


        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            name = itemView.findViewById(R.id.item_name_grid_layout_resource);
            price = itemView.findViewById(R.id.item_price_grid_layout_resource);
            imageView = itemView.findViewById(R.id.image_grid_layout_resource);
            cardView = itemView.findViewById(R.id.my_product_card_view);
        }
    }

}
