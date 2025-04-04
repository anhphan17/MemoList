package com.example.memolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter{
    private final ArrayList<Memo> memoData;
    private ArrayList<Memo> memos;

    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private final Context parentContext;

    public void updateMemos(ArrayList<Memo> newMemos) {
        memos.clear();
        memos.addAll(newMemos);

        memoData.clear();
        memoData.addAll(newMemos);
        notifyDataSetChanged();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMemoTitle;
        public TextView textViewMemoDescription;
        public TextView textViewPrioritySelection;
        public TextView textViewDate;
        public Button deleteButton;
        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMemoTitle = itemView.findViewById(R.id.textMemoTitle);
            textViewMemoDescription = itemView.findViewById(R.id.textMemoDescription);
            textViewPrioritySelection = itemView.findViewById(R.id.textMemoPriority);
            textViewDate = itemView.findViewById(R.id.textMemoDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteMemo);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getMemoTextView() {
            return textViewMemoTitle;
        }

        public TextView getDescriptionTextView() {
            return textViewMemoDescription;
        }

        public TextView getPrioritySelectionTextView() {
            return textViewPrioritySelection;
        }

        public TextView getDateTextView() {
            return textViewDate;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public MemoAdapter (ArrayList<Memo> arrayList, Context context) {
        memoData = new ArrayList<>(arrayList);
        memos = new ArrayList<>(arrayList);
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MemoViewHolder mvh = (MemoViewHolder) holder;
        Memo memo = memoData.get(position);

        mvh.getMemoTextView().setText(memo.getMemoTitle());
        mvh.getDescriptionTextView().setText(memo.getMemoDescription());
        mvh.getPrioritySelectionTextView().setText(memo.getPrioritySelection());
        mvh.getDateTextView().setText(memo.getDateAsString());

        String priority = memo.getPrioritySelection();
        int colorBlue = parentContext.getResources().getColor(R.color.blue);
        int colorGreen = parentContext.getResources().getColor(R.color.green);
        int colorRed = parentContext.getResources().getColor(R.color.red);

        if (priority != null) {
            if (priority.equals("1 - High")) {
                mvh.getPrioritySelectionTextView().setTextColor(colorRed);
            }
            else if (priority.equals("2 - Medium")) {
                mvh.getPrioritySelectionTextView().setTextColor(colorGreen);
            }
            else if (priority.equals("3 - Low")) {
                mvh.getPrioritySelectionTextView().setTextColor(colorBlue);
            }
        }

        if (isDeleting) {
            mvh.getDeleteButton().setVisibility(View.VISIBLE);
            mvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else {
            mvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    public void deleteItem(int position) {
        Memo memo = memoData.get(position);
        MemoDataSource ds = new MemoDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteMemo(memo.getMemoID());
            ds.close();
            if (didDelete) {
                memoData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }
}
