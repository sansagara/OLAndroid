package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hecticus.ofertaloca.testapp.R;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.view.CreditCardView;

/**

 */
public class BuyCreditCard extends Fragment {

    boolean cardIsFront;

    public BuyCreditCard() {
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
        View view = inflater.inflate(R.layout.fragment_buy_credit_card, container, false);

        final CreditCardView creditCardView = (CreditCardView) view.findViewById(R.id.creditCardView);

        final EditText CardHolder =(EditText)view.findViewById(R.id.ed_owner_name);
        final EditText CardNumber =(EditText)view.findViewById(R.id.ed_card_number);
        final EditText CardExpDate =(EditText)view.findViewById(R.id.ed_exp_date);
        final EditText CardCVV =(EditText)view.findViewById(R.id.ed_cvv);

        cardIsFront = true;

        CardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!cardIsFront) {
                    creditCardView.flipToFront();
                    cardIsFront = true;
                }
                if (!hasFocus) {
                    creditCardView.setTextNumber(CardNumber.getText());
                    if (CardNumber.getText().length() > 4) {
                        String checkVisa = CardNumber.getText().toString();
                        if (checkVisa.substring(0,3).equals("413") || checkVisa.substring(0,3).equals("416")) {
                            creditCardView.chooseFlag(IssuerCode.VISACREDITO);
                        } else if (checkVisa.substring(0,3).equals("419")) {
                            creditCardView.chooseFlag(IssuerCode.VISAELECTRON);
                        } else if (checkVisa.substring(0,2).equals("51") || checkVisa.substring(0,2).equals("55")) {
                            creditCardView.chooseFlag(IssuerCode.MASTERCARD);
                        } else {
                            creditCardView.chooseFlag(IssuerCode.OTHER);
                        }
                    }
                }
            }
        });

        CardHolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!cardIsFront) {
                    creditCardView.flipToFront();
                    cardIsFront = true;
                }
                if (!hasFocus) {
                    creditCardView.setTextOwner(CardHolder.getText());
                }
            }
        });

        CardExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!cardIsFront) {
                    creditCardView.flipToFront();
                    cardIsFront = true;
                }
                if (!hasFocus) {
                    creditCardView.setTextExpDate(CardExpDate.getText());
                }
            }
        });

        CardCVV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (cardIsFront) {
                        creditCardView.flipToBack();
                        cardIsFront = false;
                    }
                } else {
                    creditCardView.setTextCVV(CardCVV.getText());
                }
            }
        });

        return view;
    }

}