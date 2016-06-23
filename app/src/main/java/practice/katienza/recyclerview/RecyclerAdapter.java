package practice.katienza.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by katienza on 6/15/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleViewHolder> {

    private List<String> contents;
    private View.OnClickListener onClickListener;
    public RecyclerAdapter(List<String> contents, View.OnClickListener onClickListener){
        this.contents=contents;
        this.onClickListener=onClickListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view,parent,false);
        return new RecycleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        holder.tv1.setText(contents.get(position));
        holder.tv2.setText(contents.get(position)+ " || "+contents.get(position) );
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1,tv2;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.textView);
            tv2 = (TextView) itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
