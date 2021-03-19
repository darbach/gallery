package edu.cnm.deepdive.gallery.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.gallery.BuildConfig;
import edu.cnm.deepdive.gallery.model.Image;
import edu.cnm.deepdive.gallery.model.User;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GalleryServiceProxy {

  @GET("users/me")
  Single<User> getProfile(@Header("authorization") String bearerToken);

  /**
   * Uploading a file with a title and NO description.
   * @param bearerToken
   * @param file
   * @return
   */
  @Multipart
  @POST("images")
  Single<Image> post(@Header("Authorization") String bearerToken, @Part MultipartBody.Part file,
      @Part("title") RequestBody title);

  /**
   * Uploading a file with both a title and description.
   * @param bearerToken
   * @param file
   * @return
   */
  @Multipart
  @POST("images")
  Single<Image> post(@Header("Authorization") String bearerToken, @Part MultipartBody.Part file,
      @Part("title") RequestBody title, @Part("description") RequestBody description);

  static GalleryServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final GalleryServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(GalleryServiceProxy.class);
    }

  }

}
