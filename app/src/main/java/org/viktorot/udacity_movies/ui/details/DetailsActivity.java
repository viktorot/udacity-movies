package org.viktorot.udacity_movies.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.viktorot.udacity_movies.ui.details.reviews.ReviewsAdapter;
import org.viktorot.udacity_movies.ui.details.trailers.TrailersAdapter;

import io.reactivex.functions.Consumer;

public class DetailsActivity extends AppCompatActivity {

    public static String ARG_MOVIE = "movie";

    private DetailsViewModel viewModel;

    private ImageView img;
    private TextView tvOriginalTitle;
    private TextView tvReleaseDate;
    private TextView tvScore;
    private TextView tvOverview;
    private TextView tvTrailersTitle;
    private TextView tvReviewsTitle;
    private ToggleButton btnFav;

    private RecyclerView rvTrailers;
    private TrailersAdapter trailersAdapter = new TrailersAdapter(DetailsActivity.this::openTrailer);

    private RecyclerView rvReviews;
    private ReviewsAdapter reviewsAdapter = new ReviewsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.image);
        tvOriginalTitle = findViewById(R.id.original_title_txt);
        tvReleaseDate = findViewById(R.id.release_txt);
        tvScore = findViewById(R.id.score_txt);
        tvOverview = findViewById(R.id.overview_txt);
        tvReviewsTitle = findViewById(R.id.reviews_title);
        tvTrailersTitle = findViewById(R.id.trailers_title);

        btnFav = findViewById(R.id.fav);
        btnFav.setEnabled(false);
        btnFav.setTextOff("");
        btnFav.setTextOn("");
        btnFav.setOnCheckedChangeListener((view, checked) -> {
            viewModel.setFavourite(checked);
        });

        rvTrailers = findViewById(R.id.trailers);
        rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setAdapter(trailersAdapter);

        rvReviews = findViewById(R.id.reviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewsAdapter);

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
        viewModel.trailers.observe(this, trailers -> {
            if (trailers == null || trailers.isEmpty()) {
                tvTrailersTitle.setVisibility(View.GONE);
                return;
            }
            tvTrailersTitle.setVisibility(View.VISIBLE);
            trailersAdapter.setItems(trailers);
        });
        viewModel.reviews.observe(this, reviews -> {
            if (reviews == null || reviews.isEmpty()) {
                tvReviewsTitle.setVisibility(View.GONE);
                return;
            }
            tvReviewsTitle.setVisibility(View.VISIBLE);
            reviewsAdapter.setItems(reviews);
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

    public void openTrailer(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null && viewModel.movie.getValue() != null) {
            outState.putParcelable(ARG_MOVIE, viewModel.movie.getValue());
        }
    }
}
