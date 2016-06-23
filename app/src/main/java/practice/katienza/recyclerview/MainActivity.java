package practice.katienza.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnCheckMatchListener{

    //String[] tempArr= {"A","B","C","D","E","F","G","H","A","B","C","D","E","F","G","H"};
    private String[] tempArr= {"A","B","C","D","A","B","C","D"};
    private List<String> tempList = new ArrayList<String>(Arrays.asList(tempArr));
    private RecyclerView rv;
    private RecyclerAdapter rca;
    private Handler h = new Handler();
    private FlipUpdaterThread flipUpdaterThread= new FlipUpdaterThread();
    private boolean isFlipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager llm = new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);
        rca = new RecyclerAdapter(tempList, this );
        RecyclerView.ItemDecoration itemDecor = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(itemDecor);
        rv.setAdapter(rca);
    }

    @Override
    public void onClick(View v) {
        addToCheck(v);

    }

    @Override
    public void addToCheck(View v){
        toCompare.add(v);
        if(isFlipping){
            h.removeCallbacks(flipUpdaterThread);
            if(flipUpdaterThread.toFlip.contains(v)){
                flipUpdaterThread.toFlip.remove(v);
            }
            else{
                ((ViewAnimator)v.findViewById(R.id.card)).showNext();
            }
            h.post(flipUpdaterThread);
        }
        else{
            ((ViewAnimator)v.findViewById(R.id.card)).showNext();
        }
        if (toCompare.size() == 2) {
            onCheckMatch();
        }
    }

    @Override
    public void onCheckMatch() {
        final View v1 = toCompare.get(0);
        final View v2 = toCompare.get(1);
        if (toCompare.get(0) != toCompare.get(1)){
            String s1 = ((TextView) v1.findViewById(R.id.textView)).getText().toString();
            String s2 = ((TextView) v2.findViewById(R.id.textView)).getText().toString();
            if (!s1.equals(s2)) {
                isFlipping=true;
                flipUpdaterThread.flip(new ArrayList<View>(toCompare));
                h.postDelayed(flipUpdaterThread,10000);
            } else {
                removeInstancesFromList(tempList,tempList.get(rv.getChildAdapterPosition(v1)));
                rca.notifyItemRemoved(rv.getChildAdapterPosition(v1));
                rca.notifyItemRemoved(rv.getChildAdapterPosition(v2));
                toCompare.clear();
            }
        }
        toCompare.clear();

    }

    private void removeInstancesFromList(List<String> list,String toBeRemoved) {
        List<String> toRemove = new ArrayList<String>();
        toRemove.add(toBeRemoved);
        list.removeAll(toRemove);
    }

    private class FlipUpdaterThread implements Runnable {
        private List<View> toFlip;

        public void flip(List<View> toFlip) {
            this.toFlip=toFlip;
        }

        @Override
        public void run() {
            for(View v:toFlip) {
                ((ViewAnimator) v.findViewById(R.id.card)).showNext();
            }
            isFlipping=false;
        }
    }
}
