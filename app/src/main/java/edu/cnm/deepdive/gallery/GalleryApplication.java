package edu.cnm.deepdive.gallery;

import android.app.Application;
import edu.cnm.deepdive.gallery.service.GoogleSignInService;

public class GalleryApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
  }
}
