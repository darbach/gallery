package edu.cnm.deepdive.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.gallery.BuildConfig;
import edu.cnm.deepdive.gallery.adapter.ImageAdapter.Holder;
import edu.cnm.deepdive.gallery.databinding.ItemImageBinding;
import edu.cnm.deepdive.gallery.model.Image;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final LayoutInflater inflater;
  private final List<Image> images;
  private final OnImageClickHelper onImageClickHelper;

  public ImageAdapter(Context context, List<Image> images, OnImageClickHelper onImageClickHelper) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.images = images;
    this.onImageClickHelper = onImageClickHelper;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemImageBinding binding = ItemImageBinding.inflate(inflater, parent, false);
    return new Holder(binding, onImageClickHelper);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return images.size();
  }

  public class Holder extends RecyclerView.ViewHolder implements OnClickListener {

    private final ItemImageBinding binding;
    private Image image;
    private OnImageClickHelper onImageClickHelper;

    public Holder(ItemImageBinding binding, OnImageClickHelper onImageClickHelper) {
      super(binding.getRoot());
      this.binding = binding;
      this.onImageClickHelper = onImageClickHelper;
      binding.getRoot().setOnClickListener(this);
    }

    private void bind(int position) {
      image = images.get(position);
      if (image.getHref() != null) {
        Picasso.get().load(String.format(BuildConfig.CONTENT_FORMAT, image.getHref()))
            .into(binding.imageGallery);
      }
      binding.imageTitle.setText(image.getTitle());
      binding.imageDescription.setText(image.getDescription());
      binding.getRoot().setOnClickListener(this); //makes the RecyclerView item clickable
    }

    @Override
    public void onClick(View view) {
      onImageClickHelper.onImageClick(images.get(getAdapterPosition()), getAdapterPosition());
    }
  }

  public interface OnImageClickHelper {

    void onImageClick(Image image, int position);
  }

}
