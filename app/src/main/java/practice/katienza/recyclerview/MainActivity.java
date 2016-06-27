package practice.katienza.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnCheckMatchListener{
    private final String[] lettersToPlay= {"H","A","P","Y","B","I","R","T","D","N","H","A","P","Y","B","I","R","T","D","N"};
    private final String[] lettersBoard={"H","A","P","P","Y"," ","B","I","R","T","H","D","A","Y"," ","H","A","N"};
    private final List<String> lettersToPlayList = new ArrayList<String>(Arrays.asList(lettersToPlay));
    private final List<String> lettersBoardList = new ArrayList<String>(Arrays.asList(lettersBoard));
    private RecyclerView lettersInPlay;
    private RecyclerAdapter lettersInPlayAdapter;
    private RecyclerView letterBoardInPlay;
    private RecyclerAdapter letterBoardInPlayAdapter;
    private Handler handler = new Handler();
    private FlipUpdaterThread flipUpdaterThread= new FlipUpdaterThread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLettersToPlay();
        setupBoard();
    }


    private void setupBoard() {
        letterBoardInPlay= (RecyclerView) findViewById(R.id.letters_board);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        letterBoardInPlay.setLayoutManager(llm);
        letterBoardInPlayAdapter = new RecyclerAdapter(lettersBoardList,null,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        letterBoardInPlay.setAdapter(letterBoardInPlayAdapter);
    }

    private void setupLettersToPlay() {
        lettersInPlay = (RecyclerView) findViewById(R.id.letters_to_play);
        GridLayoutManager llm = new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        lettersInPlay.setLayoutManager(llm);
        lettersInPlayAdapter = new RecyclerAdapter(lettersToPlayList, this,R.anim.from_middle,R.anim.to_middle );
        RecyclerView.ItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        lettersInPlay.addItemDecoration(itemDecor);
        lettersInPlay.setAdapter(lettersInPlayAdapter);
    }

    @Override
    public void onClick(View v) {
        addToCheck(v);
    }

    @Override
    public void addToCheck(View v){
        toCompare.add(v);
        checkFlips(v);
        if (toCompare.size() == 2) {
            onCheckMatch();
        }
    }

    private void checkFlips(View v) {
        if(flipUpdaterThread.isFlipping()){
            handler.removeCallbacks(flipUpdaterThread);
            if(flipUpdaterThread.contains(v)){
                flipUpdaterThread.remove(v);
            }
            else{
                ((ViewAnimator)v.findViewById(R.id.card)).showNext();
            }
            handler.post(flipUpdaterThread);
        }
        else{
            ((ViewAnimator)v.findViewById(R.id.card)).showNext();
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
                flipUpdaterThread.flip(new ArrayList<View>(toCompare));
                handler.postDelayed(flipUpdaterThread,2000);
            } else {
                removeInstancesFromList(lettersToPlayList, lettersToPlayList.get(lettersInPlay.getChildAdapterPosition(v1)));
                lettersInPlayAdapter.notifyItemRemoved(lettersInPlay.getChildAdapterPosition(v1));
                lettersInPlayAdapter.notifyItemRemoved(lettersInPlay.getChildAdapterPosition(v2));
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
}
