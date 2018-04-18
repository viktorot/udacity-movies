package org.viktorot.udacity_movies.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import org.viktorot.udacity_movies.db.MovieDb;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Review;
import org.viktorot.udacity_movies.models.Trailer;
import org.viktorot.udacity_movies.service.MovieService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DetailsViewModel extends AndroidViewModel {

    private static String TAG = DetailsViewModel.class.getSimpleName();

    public MutableLiveData<Movie> movie = new MutableLiveData<>();
    public MutableLiveData<Boolean> favourite = new MutableLiveData<>();
    public MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    public MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();

    private CompositeDisposable comp = new CompositeDisposable();


    public DetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);

        this.getFavouriteStatus(movie.id);
        this.getTrailers(movie.id);
        this.getReviews(movie.id);
    }

    private void getTrailers(int id) {
        Disposable disp = MovieService.getInstance().getTrailers(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> { this.trailers.setValue(data); }, this::onError);

        comp.add(disp);
    }

    private void getReviews(int id) {
        Disposable disp = MovieService.getInstance().getReviews(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> { this.reviews.setValue(data); }, this::onError);

        comp.add(disp);
    }

    public void setFavourite(boolean favourite) {
        Movie movie = this.movie.getValue();
        Disposable disp = MovieDb.setFavourite(getApplication().getContentResolver(), movie, favourite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { this.favourite.setValue(favourite); }, this::onError);

        comp.add(disp);
    }

    private void getFavouriteStatus(long id) {
        Disposable disp = MovieDb.isFavourite(getApplication().getContentResolver(), id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fav -> { this.favourite.setValue(fav); }, this::onError);

        comp.add(disp);
    }

    private void onError(Throwable error) {
        Log.e(TAG, "movies request error", error);
    }

    @Override
    protected void onCleared() {
        comp.dispose();
        super.onCleared();
    }
}
