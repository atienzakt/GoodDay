package practice.katienza.recyclerview;

import android.view.View;
import android.widget.ViewAnimator;

import java.util.List;

/**
 * Created by katienza on 6/27/2016.
 */
class FlipUpdaterThread implements Runnable {
    private List<View> toFlip;
    private boolean isFlipping;

    public void flip(List<View> toFlip) {
        isFlipping = true;
        this.toFlip = toFlip;
    }

    public boolean isFlipping(){
        return isFlipping;
    }

    @Override
    public void run() {
        for (View v : toFlip) {
            ((ViewAnimator) v.findViewById(R.id.card)).showNext();
        }
        isFlipping = false;
    }

    public boolean contains(View v) {
        return toFlip.contains(v);
    }

    public void remove(View v) {
        toFlip.remove(v);
    }
}
