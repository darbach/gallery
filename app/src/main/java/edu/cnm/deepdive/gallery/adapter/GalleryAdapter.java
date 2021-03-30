package edu.cnm.deepdive.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.gallery.adapter.GalleryAdapter.Holder;
import edu.cnm.deepdive.gallery.databinding.ItemGalleryBinding;
import edu.cnm.deepdive.gallery.model.Gallery;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final LayoutInflater inflater;
  private final List<Gallery> galleries;
  private final OnGalleryClickHelper onGalleryClickHelper;

  public GalleryAdapter(Context context, List<Gallery> galleries,
      OnGalleryClickHelper onGalleryClickHelper) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.onGalleryClickHelper = onGalleryClickHelper;
    this.galleries = galleries;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemGalleryBinding binding = ItemGalleryBinding.inflate(inflater, parent, false);
    return new Holder(binding, onGalleryClickHelper);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return galleries.size();
  }

  class Holder extends RecyclerView.ViewHolder implements OnClickListener {

    private final ItemGalleryBinding binding;
    OnGalleryClickHelper onGalleryClickHelper;
    private Gallery gallery;

    public Holder(ItemGalleryBinding binding, OnGalleryClickHelper onGalleryClickHelper) {
      super(binding.getRoot());
      this.binding = binding;
      this.onGalleryClickHelper = onGalleryClickHelper;
      binding.getRoot().setOnClickListener((OnClickListener) this);
    }

    private void bind(int position) {
      gallery = galleries.get(position);
      binding.imageTitle.setText(gallery.getTitle());
      binding.imageDescription.setText(gallery.getDescription());
      binding.getRoot().setOnClickListener(this); //makes the RecyclerView item clickable
    }

    @Override
    public void onClick(View view) {
      onGalleryClickHelper
          .onGalleryClick(galleries.get(getAdapterPosition()).getId().toString(), view);
    }
  }

  public interface OnGalleryClickHelper {

    void onGalleryClick(String galleryId, View view);
  }
}
