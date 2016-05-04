package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.hecticus.ofertaloca.testapp.R;

import java.util.ArrayList;

import tools.AsyncResponse;
import tools.GetJSONDataFromServer;
import tools.MyRVAdapterOpen;


public class OpenAuctions extends Fragment implements AsyncResponse{
    private RecyclerView.Adapter mAdapter;

    public OpenAuctions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.content_ofertaloca_open, container, false);

        //Get the RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) V.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new MyRVAdapterOpen(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        // Call with 1 in auctionType for Open Auctions
        GetJSONDataFromServer asyncTask = new GetJSONDataFromServer(getActivity().getApplicationContext(), this, 1);

        //Call Async task
        //this to set delegate/listener back to this class
        asyncTask.delegate = this;

        //execute the async task
        asyncTask.execute();

        return mRecyclerView;
    }


    @Override  //this override the implemented method from asyncTask
    public void processFinish(ArrayList auctions_array) {
        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        mAdapter = new MyRVAdapterOpen(getActivity().getApplicationContext(), auctions_array);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

}