package org.viktorot.udacity_movies.ui.details;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.service.MovieService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class DetailsViewModel extends ViewModel {

    private static String TAG = DetailsViewModel.class.getSimpleName();

    public MutableLiveData<Movie> movie = new MutableLiveData<>();

    private Disposable disposable;

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);
    }

    private void setMovieId(@NonNull int id) {
        disposable = MovieService.getInstance().getMovie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setMovie, this::onMovieError);
    }

    private void onMovieError(Throwable error) {
        Log.e(TAG, "movies request error", error);
    }

    @Override
    protected void onCleared() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onCleared();
    }
}
