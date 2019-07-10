package dsppa.com.library;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxg on 2017/4/25.
 */

public class LogRecyclerViewAdapter extends BaseRecyclerView<RecyclerView.ViewHolder> {

    private Context context;
    private List<LogInfo> logInfoList;

    public LogRecyclerViewAdapter(Context context, List<LogInfo> logInfoList) {
        this.logInfoList = logInfoList;
        this.context = context;
        if (this.logInfoList == null) {
            this.logInfoList = new ArrayList<>();
        }
    }

    public void setDates(List<LogInfo> LogInfoList) {
        this.logInfoList = LogInfoList;
        if (this.logInfoList == null) {
            this.logInfoList = new ArrayList<>();
        }
        this.notifyDataSetChanged();
    }

    public void updateData(int position, LogInfo LogInfo) {
        logInfoList.set(position, LogInfo);
        notifyItemChanged(position);
    }

    public void removeData(int position, LogInfo LogInfo) {
        logInfoList.remove(LogInfo);
        notifyItemRemoved(position);
    }

    @Override
    public void setOnItemClickListener(RecyclerOnItemClickListener recyclerOnItemClick) {
        this.recyclerOnItemClick = recyclerOnItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new SquareViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SquareViewHolder) {
            bindSquareView((SquareViewHolder) holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return logInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void bindSquareView(final SquareViewHolder holder, final int position) {

        holder.contentTv.setText(logInfoList.get(position).content);
        holder.contentTv.setTextColor(ContextCompat.getColor(context,R.color.white));
        switch (logInfoList.get(position).type) {
            case LogcatHelper.LOG_TYPE_E:
                holder.contentTv.setTextColor(ContextCompat.getColor(context,R.color.red));
                break;
            case LogcatHelper.LOG_TYPE_D:
                break;
            case LogcatHelper.LOG_TYPE_W:
                break;
            case LogcatHelper.LOG_TYPE_I:
                break;
            default:
                break;
        }
    }


    public static class SquareViewHolder extends RecyclerView.ViewHolder {

        private TextView contentTv;


        public SquareViewHolder(View v) {
            super(v);

            contentTv = v.findViewById(R.id.contentTv);
        }

        public View getView() {
            return this.itemView;
        }

    }
}
