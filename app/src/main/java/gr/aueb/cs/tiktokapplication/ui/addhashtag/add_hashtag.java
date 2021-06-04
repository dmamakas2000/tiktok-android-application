package gr.aueb.cs.tiktokapplication.ui.addhashtag;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.aueb.cs.tiktokapplication.R;

public class add_hashtag extends Fragment {

    private AddHashtagViewModel mViewModel;

    public static add_hashtag newInstance() {
        return new add_hashtag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_hashtag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddHashtagViewModel.class);
        // TODO: Use the ViewModel
    }

}