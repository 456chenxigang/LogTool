package dsppa.com.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cxg on 2017/4/25.
 */

public abstract  class BaseRecyclerView<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{

    protected RecyclerOnItemClickListener recyclerOnItemClick;

    public abstract void setOnItemClickListener(RecyclerOnItemClickListener recyclerOnItemClick);


    public interface RecyclerOnItemClickListener{
        void onItemClickListener(View v, int position);
    }

}
