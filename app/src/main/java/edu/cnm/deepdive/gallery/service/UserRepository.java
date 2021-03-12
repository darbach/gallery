package edu.cnm.deepdive.gallery.service;

import android.content.Context;
import edu.cnm.deepdive.gallery.model.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

  private final Context context;
  private final GalleryServiceProxy webService;
  private final GoogleSignInService signInService;

  public UserRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    webService = GalleryServiceProxy.getInstance();
  }

  public Single<User> getUserProfile() {
    return signInService.refresh()
        .observeOn(Schedulers.io())
        .flatMap((account) -> webService.getProfile(this.getBearer(account.getIdToken()))
            .subscribeOn(Schedulers.io()));
  }

  private String getBearer(String idToken) {
    return String.format("Bearer %s", idToken);
  }
}
