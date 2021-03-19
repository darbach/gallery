package edu.cnm.deepdive.gallery.model;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class Image {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date updated;

  @Expose
  private String title;

  @Expose
  private String description;

  @Expose
  private String name;

  @Expose
  private String contentType;

  @Expose
  private User contributor;

//  private Gallery gallery;


}
