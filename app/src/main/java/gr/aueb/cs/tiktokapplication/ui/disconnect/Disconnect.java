package gr.aueb.cs.tiktokapplication.ui.disconnect;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.aueb.cs.tiktokapplication.R;

public class Disconnect extends Fragment {

    private DisconnectViewModel mViewModel;

    public static Disconnect newInstance() {
        return new Disconnect();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disconnect, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DisconnectViewModel.class);
        // TODO: Use the ViewModel
    }

}