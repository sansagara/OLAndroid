package fragments;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;

import com.hecticus.ofertaloca.testapp.R;

/**
* Created by sansagara.
 * Fragment to show the ol_preferences.xml Preferences screen.
 */
public class FragmentPreferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ol_preferences);

        EditTextPreference userID = (EditTextPreference) findPreference(getString(R.string.prefs_userid_key));
        userID.getEditText().setKeyListener(DigitsKeyListener.getInstance());

        EditTextPreference nickName = (EditTextPreference) findPreference(getString(R.string.prefs_nickname_key));
        nickName.getEditText().setKeyListener(TextKeyListener.getInstance());

    }

}