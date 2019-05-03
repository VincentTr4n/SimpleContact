package com.github.arekolek.phone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private List<Contact> exampleList;
    private List<Contact> exampleListFull;
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
            List<Contact> list = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                list.addAll(exampleListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Contact item : exampleList){
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

        private void bind(final Contact item, int position){
            textView1.setText(item.getName());
            textView2.setText(item.getPhone());

            itemView.setOnClickListener(v -> clickListener.onItemClick(item));

            itemView.setOnLongClickListener(v -> {
                clickListener.onItemLongClick(item);
                return true;
            });
        }

    }

    public void setClickListener(ClickListener clickListener) {
        ExampleAdapter.clickListener = clickListener;
    }

    public ExampleAdapter(List<Contact> list){
        this.exampleList = list;
        this.exampleListFull = new ArrayList<>(list);
    }

    public interface ClickListener {
        void onItemClick(Contact item);
        void onItemLongClick(Contact item);
    }
}
