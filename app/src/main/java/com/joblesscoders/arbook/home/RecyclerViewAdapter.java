package com.joblesscoders.arbook.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.book.BookActivity;
import com.joblesscoders.arbook.details.DetailsActivity;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.utils.StorageUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private List<Book> bookList;
    private Context context;
    private BookItemClickListener bookItemClickListener;

    public RecyclerViewAdapter(List<Book> bookList, Context context) {
        setHasStableIds(true);
        this.bookList = bookList ;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_recyclerview_item, parent, false);
        viewHolder = new BookHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Book book = bookList.get(position);
        holder = (BookHolder) holder;
        ((BookHolder) holder).booktitle.setText(book.getTitle());
        ((BookHolder) holder).bookauthor.setText(listToString(book.getAuthor()));
        ((BookHolder) holder).bookpublisher.setText(book.getPublisher()+"");
        ((BookHolder) holder).isbn.setText("ISBN : "+book.getIsbn()+"");

        RecyclerView.ViewHolder finalHolder = holder;
        Glide.with(context)
                .load(book.getThumbnail())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        ((BookHolder) finalHolder).bookpic.setBackground(null);
                        return false;
                    }
                })
                .into(((BookHolder) holder).bookpic);

        ((BookHolder) holder).selectad.setOnClickListener(v -> {
//            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, BookActivity.class);
            intent.putExtra("book", book);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        ((BookHolder) holder).delete_button.setOnClickListener(v -> {
            StorageUtil.removeBookFromList(book.get_id(), context);
            Toast.makeText(context, "Successfully removed the book!", Toast.LENGTH_SHORT).show();
            removeBook(position);
        });

    }

    public String listToString(List<String> list) {
        StringBuilder s = new StringBuilder();
        for (String st: list) {
            s.append(st).append(" ");
        }
        return s.toString();
    }
    public void removeBook(int position) {
        bookList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookList.size());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView bookpic;
        public TextView bookauthor,booktitle,bookpublisher,isbn;
        public View selectad, delete_button;


        public BookHolder(View itemView) {
            super(itemView);
            bookpic=(ImageView)itemView.findViewById(R.id.bookpic);
            bookauthor=itemView.findViewById(R.id.author);
            booktitle=itemView.findViewById(R.id.title);
            isbn=itemView.findViewById(R.id.isbn);
            selectad=itemView.findViewById(R.id.selectad);
            bookpublisher = itemView.findViewById(R.id.bookpublisher);
            delete_button = itemView.findViewById(R.id.delete_icon);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    bookItemClickListener.onItemClick(v, getAdapterPosition());
//
//                }
//            });
        }

        @Override
        public void onClick(View v) {

        }
    }

}
