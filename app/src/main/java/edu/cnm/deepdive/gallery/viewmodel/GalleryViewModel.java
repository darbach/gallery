package edu.cnm.deepdive.gallery.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.gallery.model.Gallery;
import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

  private final MutableLiveData<Gallery> gallery;
  private final MutableLiveData<List<Gallery>> galleries;
  private final MutableLiveData<Throwable> throwable;

  public GalleryViewModel(@NonNull Application application) {
    super(application);
    galleries = new MutableLiveData<>();
    gallery = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
  }

  public LiveData<Gallery> getGallery() {
    return gallery;
  }

  public LiveData<List<Gallery>> getGalleries() {
    return galleries;
  }

}
