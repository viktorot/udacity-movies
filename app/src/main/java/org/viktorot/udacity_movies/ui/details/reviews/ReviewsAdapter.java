package org.viktorot.udacity_movies.ui.details.reviews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList<Review> items = new ArrayList<>();

    public void setItems(@NonNull List<Review> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        final Review item = items.get(position);
        holder.tvName.setText(item.author);
        holder.tvContent.setText(item.content);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvContent = itemView.findViewById(R.id.content);
        }
    }
}
