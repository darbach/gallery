package edu.cnm.deepdive.gallery.service;

import android.content.Context;

public class GalleryRepository {

  private final Context context;
  private final GalleryServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  public GalleryRepository(Context context) {
    this.context = context;
    serviceProxy = GalleryServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

}
