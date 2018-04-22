package org.viktorot.udacity_movies.ui.grid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.viktorot.udacity_movies.db.MovieDb;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Sort;
import org.viktorot.udacity_movies.repo.MoviesRepo;
import org.viktorot.udacity_movies.service.MovieService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends AndroidViewModel {

    private static String TAG = MainViewModel.class.getSimpleName();

    public MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    private Sort sort = Sort.Popularity;

    private Disposable disposable;

    public MainViewModel(Application application) {
        super(application);
        getData();
    }

    private void getData() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = MoviesRepo.getInstance().getMovies(getApplication().getContentResolver(), sort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMoviesSuccess, this::onMoviesError);
    }

    public void setSort(Sort sort) {
        this.sort = sort;
        getData();
    }

    private void onMoviesSuccess(List<Movie> movies) {
        Log.v(TAG, "data received");
        this.movies.setValue(movies);
    }

    private void onMoviesError(Throwable error) {
        Log.e(TAG, "movies request error", error);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
