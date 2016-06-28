package practice.katienza.recyclerview;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.List;

/**
 * Created by katienza on 6/28/2016.
 */
public class LetterBoardAdapter extends RecyclerView.Adapter<LetterBoardAdapter.RecycleViewHolder> {
    private List<String> contents;
    public LetterBoardAdapter(List<String> contents){
        this.contents = contents;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.letters_on_board,parent,false);
        return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        holder.tv1.setText(contents.get(position));
        if(holder.tv1.getText().equals(" ")){
            holder.tv1.setBackgroundColor(212121);
            holder.viewAnimator.showNext();
        }
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
            viewAnimator = (ViewAnimator) itemView.findViewById(R.id.card);
            viewAnimator.setInAnimation(AnimationUtils.loadAnimation(itemView.getContext(),android.R.anim.slide_in_left));
            viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(itemView.getContext(),android.R.anim.slide_out_right));
        }
    }
}
