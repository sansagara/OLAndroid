package tools;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * Created by sansagara on 20/04/16.
 * Set of tools to get the Device Location in terms of Country Code.
 */
public class Location {


    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getSIMCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device from Device Configuration
     * @return country code
     */
    public static String getConfigCountry(Context context) {
        String locale = context.getResources().getConfiguration().locale.getCountry();
        return locale;
    }

}
