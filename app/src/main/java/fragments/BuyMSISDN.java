package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.R;


/**

 */
public class BuyMSISDN extends Fragment implements View.OnClickListener {

    boolean cardIsFront;

    public BuyMSISDN() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_msisdn, container, false);

        final Button sendSMS = (Button) view.findViewById(R.id.buy_with_msisdn);
        sendSMS.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        //TODO: Call to Cable and Wireless API.
        Toast.makeText(this.getActivity(), getString(R.string.toast_missing_api_call), Toast.LENGTH_LONG).show();
    }

}