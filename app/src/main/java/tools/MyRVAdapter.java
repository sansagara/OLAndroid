package tools;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hecticus.ofertaloca.testapp.AuctionActivity;
import com.hecticus.ofertaloca.testapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sansagara on 22/04/16.
 * This Class extends Recycler View.
 * Used to adapt a Recycler View with some cards inside it, and set the content for a list of products.
 * @param: the context it is being called from, to display a Toast or Dialog.
 */
public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder>  {
    private List<Auction> auctions;
    private Context context;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //The data items to modify in the Cardview Layout.
        CardView card;
        TextView title;
        TextView subtitle;
        ImageView padlock;
        TextView listPrice;
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
            listPrice = (TextView) itemView.findViewById(R.id.price_list);
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
        auctions = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            auctions.add(new Auction("Product " + i, "Description for Product " + i));
        }

        //new GetJSONDataFromServer(context).execute("Hello");

    }

    // Constructor for the Data from Rest Service
    public MyRVAdapter(Context context, ArrayList auctions_array) {
        //Set the context.
        this.context = context;

        // Provide the items.
        auctions = auctions_array;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_ofertaloca_all_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((RelativeLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Auction auction = auctions.get(position);

        //Set the title, subtitle and padlock icon style for each card.

        holder.title.setText(auction.getName());
        holder.subtitle.setText(auction.getDescription());
        holder.listPrice.setText( String.format(context.getString(R.string.list_price_0), auction.getList_price()) );
        holder.accumulatedPrice.setText( String.format("%.2f", auction.getAccumulated_price() ) );

        //Set Blue padlock if status is positive.
        if (auction.getStatus() > 0) holder.padlock.setImageResource(R.drawable.padlock2);

        //Async download image.
        if (auction.getImage_url() != null) {
            new DownloadImageTask(holder.productImage).execute(auction.getImage_url());
        }


        //Get a click listener on each card.
        holder.card.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Show toast on Click.
                /*
                Toast.makeText(context,
                    "Name: " + auction.getName() +
                    " Desc: " + auction.getDescription() +
                    " AccPrice: " + auction.getAccumulated_price() +
                    " MktPrice: " + auction.getMarket_price() +
                    "Status: " + auction.getStatus(),
                    Toast.LENGTH_SHORT).show(); */

                //Go to AuctionActivity with extra params.
                Intent intent = new Intent(context, AuctionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("auction_id", auction.getID());
                intent.putExtra("product_name", auction.getName());
                context.startActivity(intent);

            }
        });

        //Handler for timer.
        final Handler mHandler = new Handler();

        //Timer handling
        final TextView timeRemaining = holder.timeRemaining;
        Runnable hMyTimeTask = new Runnable() {

            int nCounter = auction.getRemainingTime();
            public void run() {
                if (nCounter > 60) nCounter = 60;
                nCounter--;
                timeRemaining.setText("00:00:" + String.format("%02d", nCounter));

                //Delay execution by one second!
                if (nCounter > 0) {
                    mHandler.postDelayed(this, 1000);
                } else {
                    //deleteCard(holder.getAdapterPosition());
                }
            }
        };

        //Start timer immediately
        mHandler.post(hMyTimeTask);



    }

    //TODO: Implement some way to delete a card when desired.
    public void deleteCard(int position) {
       auctions.remove(position);
    }

    // Return the size of the DataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return auctions.size();
    }
}