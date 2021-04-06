package edu.cnm.deepdive.gallery.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.gallery.adapter.ImageAdapter;
import edu.cnm.deepdive.gallery.adapter.ImageAdapter.OnImageClickHelper;
import edu.cnm.deepdive.gallery.databinding.FragmentImageBinding;
import edu.cnm.deepdive.gallery.model.Image;
import edu.cnm.deepdive.gallery.viewmodel.GalleryViewModel;
import java.util.UUID;

public class ImageFragment extends Fragment implements OnImageClickHelper {

  private FragmentImageBinding binding;
  private GalleryViewModel galleryViewModel;
  private UUID galleryId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentImageBinding.inflate(inflater);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    galleryViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
    if (getArguments() != null) {
      ImageFragmentArgs args = ImageFragmentArgs.fromBundle(getArguments());
      galleryId = UUID.fromString(args.getGalleryImages());
    }
    galleryViewModel.getGallery(galleryId);
    galleryViewModel.getGallery().observe(getViewLifecycleOwner(), (gallery) -> {
      if (gallery.getImages() != null) {
        binding.imageView.setAdapter(new ImageAdapter(getContext(), gallery.getImages(), this));
      }
    });
    galleryViewModel.getThrowable().observe(getViewLifecycleOwner(), (throwable) -> {
      if (throwable != null) {
        Snackbar.make(binding.getRoot(), throwable.getMessage(),
            BaseTransientBottomBar.LENGTH_INDEFINITE).show();
      }
    });
  }

  @Override
  public void onImageClick(Image image, int position) {
    ImageDialogFragment fragment = ImageDialogFragment.newInstance(image);
    fragment.show(getChildFragmentManager(), fragment.getClass().getName());
  }
}