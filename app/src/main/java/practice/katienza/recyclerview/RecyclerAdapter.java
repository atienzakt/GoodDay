package practice.katienza.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by katienza on 6/15/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleViewHolder> {

    private List<String> contents;
    private View.OnClickListener onClickListener;
    private int inAnimation;
    private int outAnimation;
    public RecyclerAdapter(List<String> contents, View.OnClickListener onClickListener,int inAnimation,int outAnimation){
        this.contents=contents;
        this.onClickListener=onClickListener;
        this.inAnimation=inAnimation;
        this.outAnimation=outAnimation;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.letters_to_play_adapter,parent,false);
        return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        holder.tv1.setText(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{
        private TextView tv1;
        private ViewAnimator viewAnimator;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(onClickListener);
            viewAnimator = (ViewAnimator) itemView.findViewById(R.id.card);
            viewAnimator.setInAnimation(AnimationUtils.loadAnimation(itemView.getContext(),inAnimation));
            viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(itemView.getContext(),outAnimation));
        }
    }
}
