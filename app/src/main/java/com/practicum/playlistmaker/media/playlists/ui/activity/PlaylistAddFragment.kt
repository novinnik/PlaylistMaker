package com.practicum.playlistmaker.media.playlists.ui.activity

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistAddBinding
import com.practicum.playlistmaker.media.playlists.ui.view_model.PlaylistAddViewModel
import com.practicum.playlistmaker.util.Converter.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistAddFragment : Fragment() {

    private var _binding: FragmentPlaylistAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistAddViewModel>()
    private var imageFileUri: Uri? = null
    private var titleTextWatcher: TextWatcher? = null
    private var descriptionTextWatcher: TextWatcher? = null
    private var title = ""
    private var description = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarNewPlaylist.setOnClickListener { backPressed() }

        binding.newPlaylistCreate.isEnabled = false

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                if (uri != null){
                    imageFileUri = uri
                    Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.ic_placeholder)
                        .centerCrop()
                        .transform(RoundedCorners(dpToPx(CORNER_RADIUS, requireContext())))
                        .into(binding.newPlaylistImage)
                } else {
                    showMessageToast(requireContext().resources.getString(R.string.no_select_image))
                }
            }

        binding.newPlaylistImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                title = s?.toString() ?: ""
                if (title.isEmpty()) {binding.newPlaylistCreate.isEnabled = false} else {binding.newPlaylistCreate.isEnabled = true}

            }
        }

        titleTextWatcher?.let { binding.titleNewPlaylist.addTextChangedListener(it) }

        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                description = s?.toString() ?: ""
            }
        }

        descriptionTextWatcher?.let { binding.descriptionNewPlaylist.addTextChangedListener(it) }

        binding.newPlaylistCreate.setOnClickListener {
            saveNewPlayList()
            findNavController().navigateUp()
        }

        //проверка перед выходом (слушатель для обработки нажатия кнопки Back)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun saveNewPlayList() {
        viewModel.saveNewPlaylist(imageFileUri, title, description)
        showMessageToast("Плейлист $title успешно создан")
    }

    private fun backPressed(){
        if (binding.titleNewPlaylist.text.toString().isNotEmpty() || description.isNotEmpty() || imageFileUri != null){
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialog(){
        MaterialAlertDialogBuilder(requireContext(),R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog_Dialog)
            .setTitle(requireContext().resources.getString(R.string.question_finisch_creating_a_playlist))
            .setMessage(requireContext().resources.getString(R.string.message_no_save_data))
            .setNeutralButton(requireContext().resources.getString(R.string.button_cancel)){dialog, which ->
            }
            .setPositiveButton(requireContext().resources.getString(R.string.button_complete)){dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

    companion object {
        private const val CORNER_RADIUS = 8f
        fun newInstance() = PlaylistAddFragment()

    }
}