package at.ac.tuwien.inso

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.databinding.FragmentGenerateCoverBinding
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.squareup.picasso.Picasso

class GenerateCoverFragment : Fragment(R.layout.fragment_generate_cover) {

    private val sharedViewModel: GenerateCoverViewModel by activityViewModels()
    private var _binding: FragmentGenerateCoverBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGenerateCoverBinding.inflate(inflater, container, false)


       binding.btnGenerate.setOnClickListener {
            sharedViewModel.setPrompt(binding.editTextPrompt.text.toString())
           findNavController().navigate(R.id.action_generateCoverFragment_to_imageChooser)
        }
        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
