package org.viktorot.udacity_movies.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.viktorot.udacity_movies.BuildConfig;
import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.db.MovieDb;
import org.viktorot.udacity_movies.db.MoviesContract;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.service.MovieService;

import io.reactivex.functions.Consumer;

public class DetailsActivity extends AppCompatActivity {

    private static String ARG_ID = "id";
    public static String ARG_MOVIE = "movie";

    private DetailsViewModel viewModel;

    private ImageView img;
    private TextView tvOriginalTitle;
    private TextView tvReleaseDate;
    private TextView tvScore;
    private TextView tvOverview;
    private ToggleButton btnFav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MovieService.create(BuildConfig.API_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.image);
        tvOriginalTitle = findViewById(R.id.original_title_txt);
        tvReleaseDate = findViewById(R.id.release_txt);
        tvScore = findViewById(R.id.score_txt);
        tvOverview = findViewById(R.id.overview_txt);

        btnFav = findViewById(R.id.fav);
        btnFav.setEnabled(false);
        btnFav.setTextOff(getResources().getString(R.string.label_favourite));
        btnFav.setTextOn(getResources().getString(R.string.label_favourite));
        btnFav.setOnCheckedChangeListener((view, checked) -> { viewModel.setFavourite(checked); });


        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.movie.observe(this, movie -> {
            if (movie == null) {
                return;
            }
            setData(movie);
        });
        viewModel.favourite.observe(this, fav -> {
            if (fav == null) {
                return;
            }

            btnFav.setEnabled(true);
            btnFav.setChecked(fav);
        });

        if (savedInstanceState != null) {
            Movie movie = savedInstanceState.getParcelable(ARG_MOVIE);
            if (movie == null) {
                finish();
                return;
            }
            viewModel.setMovie(movie);
        } else {
            Movie movie = getIntent().getParcelableExtra(ARG_MOVIE);
            if (movie == null) {
                throw new IllegalStateException("movie must not be null");
            }
            viewModel.setMovie(movie);
        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private void setData(Movie movie) {
        Picasso
                .with(this)
                .load(MovieService.getInstance().getImageUrl(movie.posterUrl))
                .into(img);

        getSupportActionBar().setTitle(movie.title);

        tvOriginalTitle.setText(movie.title);
        tvScore.setText(String.valueOf(movie.score));
        tvReleaseDate.setText(movie.releaseDate);
        tvOverview.setText(movie.overview);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null && viewModel.movie.getValue() != null) {
            outState.putParcelable(ARG_MOVIE, viewModel.movie.getValue());
        }
    }
}
