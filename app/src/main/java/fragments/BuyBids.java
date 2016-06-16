package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.OfertalocaActivity;
import com.hecticus.ofertaloca.testapp.R;

import tools.AsyncResponseRemBids;
import tools.GetRemainingBids;

/**
 * Fragmento de la seccion BUY BIDS
 */
public class BuyBids extends Fragment implements View.OnClickListener, AsyncResponseRemBids {

    Button getBids;
    int userID;

    public BuyBids() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get UserID from OfertaLoca Activity.
        userID = ((OfertalocaActivity) getActivity()).userID;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_buy_bids, container, false);


        //Define Layout elements.
        getBids = (Button) view.findViewById(R.id.check_remaining_bids);
        getBids.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this.getActivity(), getString(R.string.toast_getting_bids), Toast.LENGTH_LONG).show();
        SyncBids(userID);
    }

    @Override  //this override the implemented method from asyncTask
    public void processFinish(int remainingBids) {
        //Update remaining bids in Main Activity. (Drawer)
        ((OfertalocaActivity) getActivity()).remainingBids = remainingBids;
        ((OfertalocaActivity) getActivity()).updateDrawer();

    }

    //Get bids from WS. (Database).
    //What needs to be implemented is some way of buying Bid Packs.
    public void SyncBids(int ClientID) {
        GetRemainingBids asyncTask = new GetRemainingBids(getActivity().getApplicationContext(), this, ClientID);
        //this to set delegate/listener back to this class
        asyncTask.delegate = this;
        //execute the async task
        asyncTask.execute();
    }

    //Add bids on-demand (+1000bids, or something similar.
    //Just for debug.
    public void AddBids(int ClientID, int paymentType) {
        new GetRemainingBids(getActivity().getApplicationContext(), this, ClientID);
    }


}
