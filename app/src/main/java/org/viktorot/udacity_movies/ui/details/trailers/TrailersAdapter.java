package org.viktorot.udacity_movies.ui.details.trailers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.models.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private ArrayList<Trailer> items = new ArrayList<>();

    public void setItems(@NonNull List<Trailer> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);

        return new TrailersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trailer item = items.get(position);
        holder.tvTitle.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View root;
        final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.root);
            tvTitle = itemView.findViewById(R.id.title);
        }
    }
}
