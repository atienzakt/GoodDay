package practice.katienza.recyclerview;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;


public class BoardFragment extends Fragment implements View.OnClickListener, OnCheckMatchListener {
    private final String[] lettersToPlay = {"H","A","P","Y","B","I","R","T","D","N","H","A","P","Y","B","I","R","T","D","N"};
    private final String[] lettersBoard = {"H","A","P","P","Y"," "," "," ",
            "B","I","R","T","H","D","A","Y",
            "H","A","N","N","A","H"};
    private final List<String> lettersToPlayList = new ArrayList<String>(Arrays.asList(lettersToPlay));
    private final List<String> lettersBoardList = new ArrayList<String>(Arrays.asList(lettersBoard));
    private RecyclerView lettersInPlay;
    private LettersToPlayAdapter lettersInPlayAdapter;
    private RecyclerView letterBoardInPlay;
    private LetterBoardAdapter letterBoardInPlayAdapter;
    private Handler handler = new Handler();
    private FlipUpdaterThread flipUpdaterThread = new FlipUpdaterThread();

    public BoardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.board_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupLettersToPlay();
        setupBoard();
        setupTimer();
    }

    private void setupLettersToPlay() {
        lettersInPlay = (RecyclerView) getView().findViewById(R.id.letters_to_play);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),4,GridLayoutManager.VERTICAL,false);
        lettersInPlay.setLayoutManager(llm);
        lettersInPlayAdapter = new LettersToPlayAdapter(lettersToPlayList, this);
        RecyclerView.ItemDecoration itemDecor = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        lettersInPlay.addItemDecoration(itemDecor);
        lettersInPlay.setAdapter(lettersInPlayAdapter);
    }

    private void setupBoard() {
        letterBoardInPlay = (RecyclerView) getView().findViewById(R.id.letters_board);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),8,GridLayoutManager.VERTICAL,false);
        letterBoardInPlay.setLayoutManager(llm);
        letterBoardInPlayAdapter = new LetterBoardAdapter(lettersBoardList);
        letterBoardInPlay.setAdapter(letterBoardInPlayAdapter);
    }

    private void setupTimer(){
        final TextView t = (TextView) getView().findViewById(R.id.timer);
        GregorianCalendar timerSet = new GregorianCalendar(2016,GregorianCalendar.JULY,20);
        GregorianCalendar timerNow = new GregorianCalendar();
        new CountDownTimer(timerSet.getTimeInMillis()-timerNow.getTimeInMillis(),1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int days = (int) ((millisUntilFinished / 1000) / 86400);
                int hours = (int) (((millisUntilFinished / 1000) - (days
                        * 86400)) / 3600);
                int minutes = (int) (((millisUntilFinished / 1000) - ((days
                        * 86400) + (hours * 3600))) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                String display=days+ " | "+hours+" | "+minutes+" | "+seconds;
                t.setText(display);
            }

            @Override
            public void onFinish() {

            }
        }.start();
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
                findAndFlipMatchesOnBoard(s1);
                removeInstancesFromList(lettersToPlayList, s1);
                lettersInPlayAdapter.notifyItemRemoved(lettersInPlay.getChildAdapterPosition(v1));
                lettersInPlayAdapter.notifyItemRemoved(lettersInPlay.getChildAdapterPosition(v2));
                toCompare.clear();
            }
        }
        toCompare.clear();
    }

    private void findAndFlipMatchesOnBoard(String s) {
        int index = 0;
        for(String letters:lettersBoard){
            if(letters.equals(s)){
                ((ViewAnimator)letterBoardInPlay.findViewHolderForLayoutPosition(index).itemView.findViewById(R.id.card)).showNext();
            }
            index++;
        }
    }

    private void removeInstancesFromList(List<String> list,String toBeRemoved) {
        List<String> toRemove = new ArrayList<String>();
        toRemove.add(toBeRemoved);
        list.removeAll(toRemove);
    }
}
