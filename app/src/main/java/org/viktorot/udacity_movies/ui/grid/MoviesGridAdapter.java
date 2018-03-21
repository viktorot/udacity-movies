package org.viktorot.udacity_movies.ui.grid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.service.MovieService;

import java.util.ArrayList;
import java.util.List;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private final Context context;
    private final MoviesGridAdapter.Callback callback;

    private ArrayList<Movie> data = new ArrayList<>();

    public MoviesGridAdapter(@NonNull Context context, @NonNull Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setData(List<Movie> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        ViewHolder holder = new ViewHolder(v);

        holder.image.setOnClickListener(v1 -> {
            int position = holder.getAdapterPosition();
            callback.onMovieClicked(data.get(position));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = data.get(position);

        Picasso
                .with(context)
                .load(MovieService.getInstance().getImageUrl(movie.posterUrl))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;


        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface Callback {
        void onMovieClicked(Movie movie);
    }
}
