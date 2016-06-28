package practice.katienza.recyclerview;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    public StartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof OnClickStartListener){
            ((OnClickStartListener)getActivity()).onClickStart();
        }
    }

    public interface OnClickStartListener{
        void onClickStart();
    }
}
