package edu.cnm.deepdive.gallery.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import edu.cnm.deepdive.gallery.NavGraphDirections;
import edu.cnm.deepdive.gallery.NavGraphDirections.OpenUploadProperties;
import edu.cnm.deepdive.gallery.R;
import edu.cnm.deepdive.gallery.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

  private static final int PICK_IMAGE_REQUEST = 1023;
  private FragmentFirstBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentFirstBinding.inflate(inflater, container, false);
    binding.buttonFirst.setOnClickListener((v) ->
        NavHostFragment
            .findNavController(FirstFragment.this)
            .navigate(R.id.action_FirstFragment_to_SecondFragment));
    binding.pickImage.setOnClickListener((v) -> pickImage());
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
      OpenUploadProperties action = NavGraphDirections.openUploadProperties(data.getData());
      Navigation.findNavController(binding.getRoot()).navigate(action);
    }
  }

  private void pickImage() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image_title)), PICK_IMAGE_REQUEST);
  }

}