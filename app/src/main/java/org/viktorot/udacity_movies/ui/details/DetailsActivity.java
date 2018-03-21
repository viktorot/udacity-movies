package org.viktorot.udacity_movies.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.viktorot.udacity_movies.BuildConfig;
import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.service.MovieService;

public class DetailsActivity extends AppCompatActivity {

    private static String ARG_ID = "id";
    public static String ARG_MOVIE = "movie";

    private DetailsViewModel viewModel;

    private ImageView img;
    private TextView tvOriginalTitle;
    private TextView tvReleaseDate;
    private TextView tvScore;
    private TextView tvOverview;

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

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.movie.observe(this, movie -> {
            if (movie == null) {
                return;
            }
            setData(movie);
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
