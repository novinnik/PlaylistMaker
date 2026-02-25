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
import androidx.core.os.bundleOf
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
    private var playlistId: Int = -1
    private var thisNewPlaylist = true

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

        playlistId = requireArguments().getInt(PLAYLIST_ID)?:-1

        if (playlistId > 0 ) {
            viewModel.setDataPlaylist(playlistId)
            thisNewPlaylist = false
        }

        binding.newPlaylistCreate.isEnabled = !thisNewPlaylist //false

        if (thisNewPlaylist){
            binding.newPlaylistCreate.text = requireContext().resources.getString(R.string.create)
            binding.toolbarNewPlaylist.title = requireContext().resources.getString(R.string.new_playlist)
        } else {
            binding.newPlaylistCreate.text = requireContext().resources.getString(R.string.save)
            binding.toolbarNewPlaylist.title = requireContext().resources.getString(R.string.edit)
        }

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                if (uri != null){
                    viewModel.onImageSelected(uri)
                    loadImage(uri)

                } else {
                    showMessageToast(requireContext().resources.getString(R.string.no_select_image))
                }
            }

        binding.newPlaylistImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val title = s?.toString() ?: ""
                viewModel.onTitleChanged(title)
                if (title.isEmpty()) {binding.newPlaylistCreate.isEnabled = false} else {binding.newPlaylistCreate.isEnabled = true}
            }
        }

        titleTextWatcher.let { binding.titleNewPlaylist.addTextChangedListener(it) }

        val descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onDescriptionChanged(s?.toString() ?: "")
            }
        }

        descriptionTextWatcher.let { binding.descriptionNewPlaylist.addTextChangedListener(it) }

        binding.newPlaylistCreate.setOnClickListener {
            if (thisNewPlaylist) {
                saveNewPlayList()
                findNavController().navigateUp()
            } else {
                updatePlaylist()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        //проверка перед выходом (слушатель для обработки нажатия кнопки Back)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            })

        viewModel.stateFormLiveData.observe(viewLifecycleOwner){state ->
            if (binding.titleNewPlaylist.text.toString() != state.title){
                binding.titleNewPlaylist.setText(state.title)
            }

            if (binding.descriptionNewPlaylist.text.toString() != state.description){
                binding.descriptionNewPlaylist.setText(state.description)
            }

            loadImage(state.uriCover)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun saveNewPlayList() {
        val title = binding.titleNewPlaylist.text.toString()
        viewModel.saveNewPlaylist()
        val textPlaylist = requireContext().resources.getString(R.string.playlist)
        val textOk = requireContext().resources.getString(R.string.ok_create_small).lowercase()
        showMessageToast("$textPlaylist $title $textOk")
    }

    private fun updatePlaylist(){
        viewModel.updatePlaylist()
    }

    private fun backPressed(){
        if (binding.titleNewPlaylist.text.toString().isNotEmpty() || binding.descriptionNewPlaylist.text.toString().isNotEmpty() || imageFileUri != null){
            when (thisNewPlaylist){
                true -> showDialogCreate()
                else -> findNavController().navigateUp()
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialogCreate(){
        MaterialAlertDialogBuilder(requireContext(),)
            .setTitle(requireContext().resources.getString(R.string.question_finisch_creating_a_playlist))
            .setMessage(requireContext().resources.getString(R.string.message_no_save_data))
            .setNeutralButton(requireContext().resources.getString(R.string.button_cancel)){dialog, which ->
            }
            .setPositiveButton(requireContext().resources.getString(R.string.button_complete)){dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

    private fun loadImage(uri: Uri?){
        imageFileUri = uri
        if (uri != null){
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.ic_placeholder_info)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, requireContext())))
            .into(binding.newPlaylistImage)
        }
    }

    companion object {
        private const val CORNER_RADIUS = 8f
        private const val PLAYLIST_ID = "playlist_id"
        fun newInstance() = PlaylistAddFragment()
        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}