package org.viktorot.udacity_movies.ui.grid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.viktorot.udacity_movies.BuildConfig;
import org.viktorot.udacity_movies.R;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Sort;
import org.viktorot.udacity_movies.service.MovieService;
import org.viktorot.udacity_movies.ui.details.DetailsActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel viewModel;

    private RecyclerView recycler;

    private MoviesGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MoviesGridAdapter(this, movie -> {
            Log.v(TAG, movie.title);
            goToDetails(movie);
        });
        recycler.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.movies.observe(this, movies -> {
            if (movies == null) {
                return;
            }
            onDataChanged(movies);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popularity) {
            viewModel.setSort(Sort.Popularity);
            return true;
        } else if (id == R.id.action_sort_vote) {
            viewModel.setSort(Sort.VoteCount);
            return true;
        } else if (id == R.id.action_sort_favourite) {
            viewModel.setSort(Sort.Favourites);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToDetails(Movie movie) {
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra(DetailsActivity.ARG_MOVIE, movie);

        startActivity(i);
    }

    private void onDataChanged(List<Movie> data) {
        adapter.setData(data);
    }
}
