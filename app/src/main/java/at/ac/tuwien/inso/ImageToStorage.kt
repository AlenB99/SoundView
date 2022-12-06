package at.ac.tuwien.inso

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import at.ac.tuwien.inso.databinding.FragmentImageChooserBinding
import at.ac.tuwien.inso.databinding.FragmentImageToStorageBinding
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageToStorage.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageToStorage : Fragment() {

    private val sharedViewModel: GenerateCoverViewModel by activityViewModels()
    private var _binding: FragmentImageToStorageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageToStorageBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_image_to_storage, container, false)
        println(sharedViewModel.prompt.value.toString())
        val imageView = binding.imageView3

        // From python script we get a PyObject, which is converted to a string. Afterwards
        // its added to urlList, so that we can select the urls through indexing
        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post {
            Picasso.get().load(sharedViewModel.imageurl.value).into(imageView)
        }


        view.findFocus()?.let {
            (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




/*

 */
