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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.ac.tuwien.inso.R
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
 * Use the [GenerateCoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class GenerateCoverFragment : Fragment(R.layout.fragment_generate_cover) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_generate_cover, container, false)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(view.context))
        }
        val py = Python.getInstance()
        val module = py.getModule("plot")

        view.findViewById<Button>(R.id.btn_generate).setOnClickListener {
            try {
                val url = module.callAttr("plot",
                    view.findViewById<EditText>(R.id.editText_prompt).text.toString(),
                    view.findViewById<EditText>(R.id.editText_prompt).text.toString())
                    .toString()
                val imageView = view.findViewById<View>(R.id.imageView) as ImageView
                Picasso.get().load(url).into(imageView)
                view.findFocus()?.let {
                    (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(it.windowToken, 0)
                }
            } catch (e: PyException) {
                println(e.message + " ")
                Toast.makeText(view.context, e.message, Toast.LENGTH_LONG).show()
            }
        }
        return view;
    }
}
