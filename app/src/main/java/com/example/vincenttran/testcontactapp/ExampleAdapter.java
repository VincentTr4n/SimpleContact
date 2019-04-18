package com.example.vincenttran.testcontactapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;
    private static ClickListener clickListener;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item, viewGroup, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        exampleViewHolder.bind(exampleList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> list = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                list.addAll(exampleListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ExampleItem item : exampleList){
                    if(item.getPhone().toLowerCase().contains(filterPattern) || item.getName().toLowerCase().contains(filterPattern)){
                        list.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = list;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    class ExampleViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;

        public ExampleViewHolder(View view){
            super(view);
            textView1 = view.findViewById(R.id.text_view1);
            textView2 = view.findViewById(R.id.text_view2);
        }

        private void bind(final ExampleItem item, int position){
            textView1.setText(item.getName());
            textView2.setText(item.getPhone());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(item);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onItemLongClick(item);
                    return false;
                }
            });
        }

    }

    public void setClickListener(ClickListener clickListener) {
        ExampleAdapter.clickListener = clickListener;
    }

    public ExampleAdapter(List<ExampleItem> list){
        this.exampleList = list;
        this.exampleListFull = new ArrayList<>(list);
    }

    public interface ClickListener {
        void onItemClick(ExampleItem item);
        void onItemLongClick(ExampleItem item);
    }
}
