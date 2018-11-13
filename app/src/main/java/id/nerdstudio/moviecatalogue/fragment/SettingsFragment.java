package id.nerdstudio.moviecatalogue.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

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
        String currentLanguage = AppSharedPreferences.getLanguage(getContext()) == null ? "en" : AppSharedPreferences.getLanguage(getContext());
        TextView languageCurrent = view.findViewById(R.id.language_current);
        languageCurrent.setText(currentLanguage.equals("en") ? "English" : "Bahasa Indonesia");
        ImageView languageIcon = view.findViewById(R.id.language_icon);
        languageIcon.setImageResource(currentLanguage.equals("en") ? R.drawable.ic_flag_usa : R.drawable.ic_flag_id);
        view.findViewById(R.id.language_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = AppSharedPreferences.getLanguage(getContext()) == null ? "en" : AppSharedPreferences.getLanguage(getContext());
                if (lang.equals("en")) {
                    lang = "in";
                }
                AppSharedPreferences.putLanguage(getContext(), lang);

                Resources resources = getActivity().getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                Configuration configuration = resources.getConfiguration();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLocale(new Locale(lang.toLowerCase()));
                } else {
                    configuration.locale = new Locale(lang);
                }
                resources.updateConfiguration(configuration, displayMetrics);

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });
        return view;
    }
}
