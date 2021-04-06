package edu.cnm.deepdive.gallery;

import android.app.Application;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.gallery.service.GoogleSignInService;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;

public class GalleryApplication extends Application {

  private String bearerToken;

  @Override
  public void onCreate() {
    super.onCreate();
    setupSignin(); //both singletons
    setupPicasso();
  }

  private void setupSignin() {
    GoogleSignInService.setContext(this);
    GoogleSignInService.getInstance().getBearerToken().observeForever((bearerToken) ->
        this.bearerToken = bearerToken);
  }

  private void setupPicasso() {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor((Chain chain) ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", bearerToken)
                    .build()
            )
        )
        .build();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(client))
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    );
  }

}
