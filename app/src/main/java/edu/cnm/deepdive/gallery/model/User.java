package edu.cnm.deepdive.gallery.model;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class User {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date connected;

  @Expose
  private String displayName;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getConnected() {
    return connected;
  }

  public void setConnected(Date connected) {
    this.connected = connected;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
