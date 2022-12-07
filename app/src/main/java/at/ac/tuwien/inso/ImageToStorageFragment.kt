package at.ac.tuwien.inso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import at.ac.tuwien.inso.databinding.FragmentImageToStorageBinding
import at.ac.tuwien.inso.ui.MainActivity
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [ImageToStorageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageToStorageFragment : Fragment() {

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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Cover Art"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        // From python script we get a PyObject, which is converted to a string. Afterwards
        // its added to urlList, so that we can select the urls through indexing
        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post {
            Picasso.get().load(sharedViewModel.imageurl.value).into(imageView)
        }

        binding.downloadButton.setOnClickListener {
            Picasso.get().load(sharedViewModel.imageurl.value).into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom?) {
                    // Save the bitmap to a file

                    val file = File(view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        , sharedViewModel.prompt.value+ "_" + Date() +  ".png")
                    (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
                    (activity as AppCompatActivity).supportActionBar?.title = "Cover Art"
                    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
                    println(file.absoluteFile.toString())
                    try {
                        file.createNewFile()
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
                        ostream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    // Handle failure
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    // Handle placeholder
                }
            })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = activity as? MainActivity
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else              -> super.onOptionsItemSelected(item)
        }
    }

}
