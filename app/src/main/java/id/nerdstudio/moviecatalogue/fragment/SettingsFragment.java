package id.nerdstudio.moviecatalogue.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.nerdstudio.moviecatalogue.MainActivity;
import id.nerdstudio.moviecatalogue.R;
import id.nerdstudio.moviecatalogue.config.AppSharedPreferences;
import id.nerdstudio.moviecatalogue.util.AlarmUtil;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.language_current)
    TextView languageCurrent;
    @BindView(R.id.language_icon)
    ImageView languageIcon;
    @BindView(R.id.daily_reminder)
    ToggleButton dailyReminder;
    String currentLanguage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        currentLanguage = AppSharedPreferences.getLanguage(getContext()) == null ? "en" : AppSharedPreferences.getLanguage(getContext());
        languageCurrent.setText(currentLanguage.equals("en") ? "English" : "Bahasa Indonesia");
        languageIcon.setImageResource(currentLanguage.equals("en") ? R.drawable.ic_flag_usa : R.drawable.ic_flag_id);
        dailyReminder.setChecked(AppSharedPreferences.getReminderEnabled(getContext()));
        return view;
    }

    @OnClick({R.id.language_setting, R.id.daily_reminder})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.language_setting:
                String lang = currentLanguage;
                if (lang.equals("en")) {
                    lang = "in";
                } else {
                    lang = "en";
                }
                AppSharedPreferences.putLanguage(getContext(), lang);

                ((MainActivity) getActivity()).setLanguage(true);
                break;
            case R.id.daily_reminder:
                boolean on = dailyReminder.getText().equals(R.string.on);
                AppSharedPreferences.putReminder(getContext(), on);
                if (on) {
                    AlarmUtil.setReminder(getContext());
                } else {
                    AlarmUtil.cancelReminder(getContext());
                }
                break;
        }
    }

}
