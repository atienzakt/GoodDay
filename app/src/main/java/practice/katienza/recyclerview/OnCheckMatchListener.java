package practice.katienza.recyclerview;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by katienza on 6/20/2016.
 */
public interface OnCheckMatchListener {
    List<View> toCompare = new ArrayList<View>(2);
    void addToCheck(View v);
    void onCheckMatch();
}
