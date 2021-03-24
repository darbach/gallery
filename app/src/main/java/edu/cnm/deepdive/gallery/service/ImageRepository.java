package edu.cnm.deepdive.gallery.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import edu.cnm.deepdive.gallery.model.Image;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageRepository {

  private final Context context;
  private final GalleryServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final ContentResolver resolver;
  private final MediaType multipartFormType;

  public ImageRepository(Context context) {
    this.context = context;
    serviceProxy = GalleryServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
    resolver = context.getContentResolver();
    multipartFormType = MediaType.parse("multipart/form-data");
  }

  @SuppressWarnings("BlockingMethodInNonBlockingContext")
  public Single<Image> add(Uri uri, String title, String description) {
    File[] filesCreated = new File[1];
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io()) //run on a background thread
        .flatMap((token) -> {
          try( //write image to app's private space so we can attach a URI before uploading
              Cursor cursor = resolver //return a row of a table
                  .query(uri, null, null, null, null);
              InputStream input = resolver.openInputStream(uri);
              ) {
            MediaType type = MediaType.parse(resolver.getType(uri)); // "image/png", "image/bmp" etc
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);//displayName column
            cursor.moveToFirst();
            String filename = cursor.getString(nameIndex); //get the filename
            File outputDir = context.getCacheDir(); //write to private file system
            File outputFile = File.createTempFile("xfer", null, outputDir);
            filesCreated[0] = outputFile;
            Files.copy(input, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            RequestBody fileBody = RequestBody.create(outputFile, type);//file part of multipart file
            MultipartBody.Part filePart = //package it all into a multipart
                MultipartBody.Part.createFormData("file", filename, fileBody);
            RequestBody titlePart = RequestBody.create(title, multipartFormType);//title of multipart
            if (description !=  null) {//post w/description of multipart
              RequestBody descriptionPart = RequestBody.create(description, multipartFormType);
              return serviceProxy.post(token, filePart, titlePart, descriptionPart);
            } else {//post w/out description
              return serviceProxy.post(token, filePart, titlePart);
            }
          }
        })
        .doFinally(() -> { //close the temp file in app's private space
          if (filesCreated[0] != null) {
            try {
              //noinspection ResultOfMethodCallIgnored
              filesCreated[0].delete();
            } catch (Exception e) {
              Log.e(getClass().getName(), e.getMessage(), e);
            }
          }
        });
  }

  public Single<List<Image>> getAll() {
    return signInService.refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap(serviceProxy::getAllImages);
  }

}
