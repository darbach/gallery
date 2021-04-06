package edu.cnm.deepdive.gallery.controller;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.gallery.R;
import edu.cnm.deepdive.gallery.databinding.FragmentUploadPropertiesBinding;
import edu.cnm.deepdive.gallery.model.Gallery;
import edu.cnm.deepdive.gallery.viewmodel.GalleryViewModel;
import edu.cnm.deepdive.gallery.viewmodel.ImageViewModel;
import java.util.List;
import java.util.UUID;

public class UploadPropertiesFragment extends DialogFragment implements TextWatcher {

  private FragmentUploadPropertiesBinding binding;
  private Uri uri;
  private AlertDialog dialog;
  private ImageViewModel imageViewModel;
  private GalleryViewModel galleryViewModel;
  private List<Gallery> galleries;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //noinspection ConstantConditions
    uri = UploadPropertiesFragmentArgs.fromBundle(getArguments()).getContentUri();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentUploadPropertiesBinding.inflate(LayoutInflater.from(getContext()),
        null, false);
    dialog = new Builder(getContext())
        .setIcon(R.drawable.ic_upload)
        .setTitle(R.string.upload_properties_title)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel,
            (dlg, which) -> { /* No need to do anything. */ })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> upload())
        .create();
    dialog.setOnShowListener((dlg) -> {
      binding.imageTitle.addTextChangedListener(this);
      binding.imageDescription.addTextChangedListener(this);
      checkSubmitConditions();
    });
    return dialog;
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Picasso
        .get()
        .load(uri)
        .into(binding.galleryImage);
    //noinspection ConstantConditions
    imageViewModel = new ViewModelProvider(getActivity()).get(ImageViewModel.class);
    galleryViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
    galleryViewModel.getGalleries().observe(getViewLifecycleOwner(),
        (galleries) -> {
          this.galleries = galleries;
          AutoCompleteTextView simpleAutoText = binding.galleryTitle;
          ArrayAdapter<Gallery> adapter = new ArrayAdapter<>(
              getContext(), android.R.layout.simple_list_item_1, galleries);
          simpleAutoText.setThreshold(5); //max number of gallery titles to show as you type
          simpleAutoText.setAdapter(adapter);
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Ignored. */ }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) { /* Ignored. */ }

  @Override
  public void afterTextChanged(Editable s) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    //noinspection ConstantConditions
    positive.setEnabled(!binding.imageTitle.getText().toString().trim().isEmpty());
  }

  @SuppressWarnings("ConstantConditions")
  private void upload() {
    String title = binding.imageTitle.getText().toString().trim();
    String description = binding.imageDescription.getText().toString().trim();
    String galleryTitle = binding.galleryTitle.getText().toString().trim();
    String titleId = "";
    UUID galleryId = null;
    for (Gallery g : galleries) {
      if (g != null && galleryTitle.equals(g.getTitle())) {
        galleryId = g.getId();
      }
    }
    imageViewModel.store(galleryId, uri, title, (description.isEmpty() ? null : description));
  }

}