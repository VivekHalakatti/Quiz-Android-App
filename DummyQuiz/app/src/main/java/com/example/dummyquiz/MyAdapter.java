package com.example.dummyquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

public class MyAdapter extends Adapter<MyAdapter.MyViewHolder> {
    static List<Datamodel> dataholder;
    private OnItemClickListener mItemCLickListener;

    public MyAdapter(List<Datamodel> dataholder, OnItemClickListener mItemCLickListener) {
        this.dataholder = dataholder;
        this.mItemCLickListener = mItemCLickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new MyViewHolder(view, mItemCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindView(dataholder.get(position));
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public interface OnItemClickListener {
        void OnItemClicked(int adapterPosition);
    }

    static class MyViewHolder extends ViewHolder implements View.OnClickListener {
        TextView id_text, question_text, options_text, bookmarkcheck, answered;
        MyAdapter.OnItemClickListener mItemClick;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener mItemCLickListener) {
            super(itemView);
//            id_text = itemView.findViewById(R.id.question_id);
            question_text = itemView.findViewById(R.id.question);
            options_text = itemView.findViewById(R.id.options);
            bookmarkcheck = itemView.findViewById(R.id.bookmark_textview);
            answered = itemView.findViewById(R.id.answer_status);
            mItemClick = mItemCLickListener;
            itemView.setOnClickListener(this);
        }

        public void bindView(Datamodel datamodel) {
//            id_text.setText(String.valueOf(datamodel.getId()));
            question_text.setText(datamodel.getQuestion());
            options_text.setText(datamodel.getOptionsall());
            if (datamodel.isAnswered() == false) {
                answered.setText("Unanswered");
            } else {
                answered.setText("Completed!");
            }
            if (datamodel.isBookmark() == false) {
                bookmarkcheck.setText("Bookmark?");
            } else {
                bookmarkcheck.setText("Saved!");
            }
        }

        @Override
        public void onClick(View view) {
            mItemClick.OnItemClicked(getAdapterPosition());
        }
    }
}
