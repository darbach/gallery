package edu.cnm.deepdive.gallery.controller;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.gallery.R;
import edu.cnm.deepdive.gallery.databinding.FragmentUploadPropertiesBinding;
import edu.cnm.deepdive.gallery.viewmodel.MainViewModel;


public class UploadPropertiesFragment extends DialogFragment {

  private FragmentUploadPropertiesBinding binding;
  private Uri uri;
  private AlertDialog dialog;
  private MainViewModel viewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO Get URI from parameters.
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentUploadPropertiesBinding.inflate(LayoutInflater.from(getContext()),
        null, false);
    dialog = new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_upload)
        .setTitle("Upload Properties")
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> { /* No need to do anything. */ })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> { /* TODO Start upload process. */ })
        .create();
    // TODO Attach text listener to validate fields and disable OK button for bad inputs.
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
    // TODO Setup viewModel & observe as necessary.
  }
}