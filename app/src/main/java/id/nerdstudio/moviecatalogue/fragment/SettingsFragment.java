package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.nerdstudio.moviecatalogue.MainActivity;
import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final String currentLanguage = AppSharedPreferences.getLanguage(getContext()) == null ? "en" : AppSharedPreferences.getLanguage(getContext());
        TextView languageCurrent = view.findViewById(R.id.language_current);
        languageCurrent.setText(currentLanguage.equals("en") ? "English" : "Bahasa Indonesia");
        ImageView languageIcon = view.findViewById(R.id.language_icon);
        languageIcon.setImageResource(currentLanguage.equals("en") ? R.drawable.ic_flag_usa : R.drawable.ic_flag_id);
        view.findViewById(R.id.language_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = currentLanguage;
                if (lang.equals("en")) {
                    lang = "in";
                } else {
                    lang = "en";
                }
                AppSharedPreferences.putLanguage(getContext(), lang);

                ((MainActivity)getActivity()).setLanguage(true);
            }
        });
        return view;
    }
}
