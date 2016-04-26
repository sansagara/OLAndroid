package tools;

import android.content.Context;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sansagara.testapp.R;

import java.util.ArrayList;
import java.util.List;

import tools.DownloadImageTask;

/**
 * Created by sansagara on 22/04/16.
 * This Class extends Recicler View.
 * Used to adapt a Recicler View with some cards inside it, and set the content for a list of products.
 * @param: the context it is being called from, to display a Toast or Dialog.
 */
public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder>  {
    private List<Product> products;
    private Context context;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //The data items to modify in the Cardview Layout.
        CardView card;
        TextView title;
        TextView subtitle;
        ImageView padlock;
        TextView timeRemaining;
        TextView accumulatedPrice;
        ImageView productImage;

        //Set the Viewholder and items.
        public ViewHolder(RelativeLayout itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.CardView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            padlock = (ImageView) itemView.findViewById(R.id.padlock);
            accumulatedPrice = (TextView) itemView.findViewById(R.id.actual_price);
            timeRemaining = (TextView) itemView.findViewById(R.id.time_remaining);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }
    }

    // Default constructor. (First-Time load)
    public MyRVAdapter(Context context) {
        //Set the context.
        this.context = context;

        // Create some items
        products = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            products.add(new Product("Item " + i, "This is the item number " + i));
        }

        //new GetJSONDataFromServer(context).execute("Hello");

    }

    // Constructor for the Data from Rest Service
    public MyRVAdapter(Context context, ArrayList products_array) {
        //Set the context.
        this.context = context;

        // Provide the items.
        products = products_array;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_megapop_product, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((RelativeLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Product product = products.get(position);

        //Set the title, subtitle and padlock icon style for each card.

        holder.title.setText(product.getName());
        holder.subtitle.setText(product.getDescription());
        holder.accumulatedPrice.setText( String.format("%.2f", product.getAccumulated_price() ) );

        //Set Blue padlock if status is positive.
        if (product.getStatus() > 0) holder.padlock.setImageResource(R.drawable.padlock2);

        //Async download image.
        new DownloadImageTask(holder.productImage).execute(product.getImage_url());


        //Get a click listener on each card.
        holder.card.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show toast on Click.
                Toast.makeText(context,
                        "Name: " + product.getName() +
                        " Desc: " + product.getDescription() +
                        " AccPrice:" + product.getAccumulated_price() +
                        " MktPrice:" + product.getMarket_price() +
                        "Status: " + product.getStatus(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Handler for timer.
        final Handler mHandler = new Handler();

        //Timer handling
        final TextView timeRemaining = holder.timeRemaining;
        Runnable hMyTimeTask = new Runnable() {
            int nCounter = product.getRemainingTime();
            public void run() {
                nCounter--;
                timeRemaining.setText("00:00:" + String.format("%02d", nCounter));

                //Delay execution by one second!
                if (nCounter > 0) {
                    mHandler.postDelayed(this, 1000);
                } else {
                    //holder.card.get;
                }
            }
        };

        //Start timer immediately
        mHandler.post(hMyTimeTask);



    }

    // Return the size of the DataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }
}