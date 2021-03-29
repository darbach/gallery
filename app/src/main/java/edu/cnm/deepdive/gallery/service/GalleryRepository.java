package edu.cnm.deepdive.gallery.service;

import android.content.Context;
import edu.cnm.deepdive.gallery.model.Gallery;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;

public class GalleryRepository {

  private final Context context;
  private final GalleryServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  public GalleryRepository(Context context) {
    this.context = context;
    serviceProxy = GalleryServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  public Single<Gallery> getGallery(UUID id) {
    return signInService.refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((account) -> serviceProxy.getGallery(id, account));
  }

  public Single<List<Gallery>> getGalleries() {
    return signInService.refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap(serviceProxy::getGalleries);
  }

}
