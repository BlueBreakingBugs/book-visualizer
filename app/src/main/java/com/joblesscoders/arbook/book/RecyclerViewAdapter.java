package com.joblesscoders.arbook.book;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentResult;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.details.DetailsActivity;
import com.joblesscoders.arbook.home.BookItemClickListener;
import com.joblesscoders.arbook.pojo.Contents;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private List<Contents> contentList;
    private Context context;
    private BookItemClickListener bookItemClickListener;

    public RecyclerViewAdapter(List<Contents> contentList, Context context) {
        setHasStableIds(true);
        this.contentList = contentList ;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_ar_content_item, parent, false);
        viewHolder = new ContentHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Contents content = contentList.get(position);
        holder = (com.joblesscoders.arbook.book.RecyclerViewAdapter.ContentHolder) holder;

        ((ContentHolder) holder).title.setText(content.getTitle());
        ((ContentHolder) holder).type.setText(content.getType());
        ((ContentHolder) holder).description.setText(content.getDescription());

        ((ContentHolder) holder).root.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("content", content);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        Glide.with(context)
                .load(content.getThumbnail())
                .into(((ContentHolder) holder).thumb);

    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumb;
        TextView title;
        TextView type;
        TextView description;
        View root;

        public ContentHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.book_thumb);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            description = itemView.findViewById(R.id.description);
            root = itemView.getRootView();
            itemView.setOnClickListener(v -> {
//                    bookItemClickListener.onItemClick(v, getAdapterPosition());
                // TODO, start floating AR Session
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

}