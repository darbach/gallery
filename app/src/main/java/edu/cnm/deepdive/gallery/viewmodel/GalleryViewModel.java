package edu.cnm.deepdive.gallery.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.gallery.model.Gallery;
import edu.cnm.deepdive.gallery.service.GalleryRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class GalleryViewModel extends AndroidViewModel implements LifecycleObserver {

  private final GalleryRepository galleryRepository;
  private final MutableLiveData<Gallery> gallery;
  private final MutableLiveData<List<Gallery>> galleries;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public GalleryViewModel(@NonNull Application application) {
    super(application);
    galleryRepository = new GalleryRepository(application);
    galleries = new MutableLiveData<>();
    gallery = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    loadGalleries();
  }

  public LiveData<Gallery> getGallery() {
    return gallery;
  }

  public LiveData<List<Gallery>> getGalleries() {
    return galleries;
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void loadGalleries() {
    throwable.postValue(null);
    pending.add(
        galleryRepository.getGalleries()
            .subscribe(
                galleries::postValue, //success
                throwable::postValue //failed
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}
