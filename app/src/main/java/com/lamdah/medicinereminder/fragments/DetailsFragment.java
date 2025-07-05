package com.lamdah.medicinereminder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.lamdah.medicinereminder.R;
import com.lamdah.medicinereminder.database.DatabaseHelper;
import com.lamdah.medicinereminder.models.Pill;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DetailsFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DATA = "data";

    private String title;
    private List<Pill> pills;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String title, List<Pill> pills) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_DATA, (Serializable) pills);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            pills = (List<Pill>) getArguments().getSerializable(ARG_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        LinearLayout pillsContainer = rootView.findViewById(R.id.pills_container);
        for (Pill pill : pills) {
            LinearLayout pillDetails = (LinearLayout) inflater.inflate(R.layout.pill_details, container, false);
            TextView pillName = pillDetails.findViewById(R.id.pill_name);
            TextView pillTime = pillDetails.findViewById(R.id.pill_time);
            TextView pillDays = pillDetails.findViewById(R.id.pill_days);
            pillName.setText(pill.getName());
            pillTime.setText(formatDateTime(pill.getDatetime()));
            pillDays.setText(getDays(pill.getDays()));
            Button deleteBtn = pillDetails.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(view -> {
                // Handle delete button click here
                Toast.makeText(getContext(), "Delete pill: " + pill.getName(), Toast.LENGTH_SHORT).show();
                // Delete the pill from the database
                DatabaseHelper db = new DatabaseHelper(getContext());
                boolean success = db.deletePill(pill.getId());
                if (!success) {
                    Toast.makeText(getContext(), "Failed to delete pill", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Remove the pill details view from the container
                pillsContainer.removeView(pillDetails);
                pillsContainer.invalidate();
            });

            pillsContainer.addView(pillDetails);
        }

        return rootView;
    }

    private String getDays(boolean[] days) {
        StringBuilder sb = new StringBuilder();
        String[] dayNames = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        for (int i = 0; i < days.length; i++) {
            if (days[i]) {
                sb.append(dayNames[i]).append(", ");
            }
        }
        if (sb.length() > 0) {
            // Remove the extra ", " at the end of the string
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private String formatDateTime(long datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(datetime));
    }
}
