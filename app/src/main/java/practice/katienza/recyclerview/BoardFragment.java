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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class BoardFragment extends Fragment implements View.OnClickListener, OnCheckMatchListener {
    private final String[] lettersToPlay = {"Good","Day","Friends"};
    private final List<String> lettersBoardList;
    private final List<String> lettersToPlayList;
    private RecyclerView lettersInPlay;
    private LettersToPlayAdapter lettersInPlayAdapter;
    private RecyclerView letterBoardInPlay;
    private Handler handler;
    private FlipUpdaterThread flipUpdaterThread;

    public BoardFragment() {
        lettersBoardList = fitToGrid(Arrays.asList(lettersToPlay),longestWordLengthInList(lettersToPlay));
        lettersToPlayList = new ArrayList<>(getUniqueLetterInPairs(lettersBoardList));
        handler = new Handler();
        flipUpdaterThread = new FlipUpdaterThread();
    }

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
        GridLayoutManager llm = new GridLayoutManager(getActivity(),longestWordLengthInList(lettersToPlay),GridLayoutManager.VERTICAL,false);
        letterBoardInPlay.setLayoutManager(llm);
        letterBoardInPlay.setAdapter(new LetterBoardAdapter(lettersBoardList));
    }

    private void setupTimer(){
        final TextView t = (TextView) getView().findViewById(R.id.timer);
        GregorianCalendar timerSet = new GregorianCalendar(2017,GregorianCalendar.JANUARY,1);
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
            public void onFinish() {}
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
            if (!s1.equalsIgnoreCase(s2)) {
                flipUpdaterThread.flip(new ArrayList<>(toCompare));
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
        for(String letters:lettersBoardList){
            if(letters.equalsIgnoreCase(s)){
                ((ViewAnimator)letterBoardInPlay.findViewHolderForLayoutPosition(index).itemView.findViewById(R.id.card)).showNext();
            }
            index++;
        }
    }

    private void removeInstancesFromList(List<String> list,String toBeRemoved) {
        List<String> toRemove = new ArrayList<>();
        toRemove.add(toBeRemoved);
        list.removeAll(toRemove);
    }

    private List<String> fitToGrid(List<String> toFit, int gridSpan){
        List<String> toReturn = new ArrayList<>();
        StringBuilder toAdd = new StringBuilder();
        for(Iterator<String> i = toFit.iterator();i.hasNext();){
            String s = i.next();
            if((toAdd.length()+s.length())>gridSpan){
                while(toAdd.length()<gridSpan){
                    toAdd.append(" ");
                }
                for(char c:toAdd.toString().toCharArray()){
                    toReturn.add(c+"");
                }
                toAdd.setLength(0);
            }
            toAdd.append(s);
            if(i.hasNext() && (toAdd.length()<gridSpan)){
                toAdd.append(" ");
            }
        }
        for(char c:toAdd.toString().toCharArray()){
            toReturn.add(c+"");
        }
        return toReturn;
    }

    private int longestWordLengthInList(String[] list){
        int longestLength=-1;
        for(String s:list){
            if(s.length()>longestLength){
                longestLength=s.length();
            }
        }
        return longestLength;
    }

    private List<String> getUniqueLetterInPairs(List<String> list){
        Set<String> siftSet = new HashSet<>();
        for(String s:list){
            for(char c:s.toCharArray())
            {
                if(c!=' ') {
                    siftSet.add((c + "").toUpperCase());
                }
            }
        }
        List<String> returnList = new ArrayList<>();
        returnList.addAll(siftSet);
        returnList.addAll(siftSet);
        return returnList;
    }
}