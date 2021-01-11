package android.cnam.bookypocket.ui.bookyMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.cnam.bookypocket.R;

public class BookyMapFragment extends Fragment {

    private BookyMapViewModel bookyMapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookyMapViewModel =
                ViewModelProviders.of(this).get(BookyMapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookymap, container, false);
        final TextView textView = root.findViewById(R.id.text_bookymap);
        bookyMapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}