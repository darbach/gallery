package edu.cnm.deepdive.gallery.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.gallery.model.Image;
import edu.cnm.deepdive.gallery.model.User;
import edu.cnm.deepdive.gallery.service.ImageRepository;
import edu.cnm.deepdive.gallery.service.UserRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<User> user;
  private final MutableLiveData<Image> image;
  private final MutableLiveData<List<Image>> images;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public MainViewModel(@NonNull Application application) {
    super(application);
    userRepository = new UserRepository(application);
    imageRepository = new ImageRepository(application);
    account = new MutableLiveData<>(userRepository.getAccount());
    user = new MutableLiveData<>();
    image = new MutableLiveData<>();
    images = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    loadImages();
  }

  public LiveData<User> getUser() {
    return user;
  }

  public LiveData<Image> getImage() {
    return image;
  }

  public LiveData<List<Image>> getImages() {
    return images;
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void store(Uri uri, String title, String description) {
    throwable.postValue(null);
    pending.add(
        imageRepository
            .add(uri, title, description)
            .subscribe(
                (image) -> loadImages(), //success TODO Explore updating list in place w/out refresh
                this::postThrowable //failed
            )
    );
  }

  public void loadImages() {
    throwable.postValue(null);
    pending.add(
        imageRepository
            .getAll()
            .subscribe(
                images::postValue, //success
                throwable::postValue //failed
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
