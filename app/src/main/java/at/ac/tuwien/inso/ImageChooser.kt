package at.ac.tuwien.inso

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import at.ac.tuwien.inso.databinding.FragmentImageChooserBinding
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
 * Use the [ImageChooser.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ImageChooser : Fragment() {

    private val sharedViewModel: GenerateCoverViewModel by activityViewModels()
    private var _binding: FragmentImageChooserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentImageChooserBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_generate_cover, container, false)
        Thread{
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(view.context))
        }
        val py = Python.getInstance()
        val module = py.getModule("image_generate")
        try {
            println(sharedViewModel.prompt.value.toString())
            val url = module.callAttr("image_generate", sharedViewModel.prompt.value.toString()).toString()


            val imageView = binding.imageView5
            val imageView2 = binding.imageView6
            val imageView3 = binding.imageView7
            val imageView4 = binding.imageView8
            val urlList: List<String> = url.split(",").map { it.trim()
                .replace("[","").replace("]","")
                .replace("'","") }
            sharedViewModel.setImageurls(urlList)
            // From python script we get a PyObject, which is converted to a string. Afterwards
            // its added to urlList, so that we can select the urls through indexing
            val uiHandler = Handler(Looper.getMainLooper())
            uiHandler.post {
                Picasso.get().load(urlList[0]).into(imageView)
                Picasso.get().load(urlList[1]).into(imageView2)
                Picasso.get().load(urlList[2]).into(imageView3)
                Picasso.get().load(urlList[3]).into(imageView4)
            }


            view.findFocus()?.let {
                (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(it.windowToken, 0)
            }
        } catch (e: PyException) {
            println(e.message + " ")
            Toast.makeText(view.context, e.message, Toast.LENGTH_LONG).show()
        }

        }.start()
        binding.imageView5.setOnClickListener {
            sharedViewModel.setImageurl(0)
            findNavController().navigate(R.id.action_imageChooser_to_imageToStorage)
        }
        return binding.root;
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*

     */
}