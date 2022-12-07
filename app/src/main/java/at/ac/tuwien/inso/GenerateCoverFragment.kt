package at.ac.tuwien.inso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import at.ac.tuwien.inso.databinding.FragmentGenerateCoverBinding
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
