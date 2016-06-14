package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hecticus.ofertaloca.testapp.R;

/**
 * Fragmento de Seccion MY ORDERS
 */
public class MyOrders extends Fragment {

    public MyOrders() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_my_orders, container, false);
    }

}
