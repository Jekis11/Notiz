package com.example.notiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notiz.Ent.Notiz
import com.example.notiz.adapter.NotizAdapter
import com.example.notiz.database.NotesDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    var notesAdapter: NotizAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)

        recycler_view.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var notes = NotesDatabase.getDatabase(it).notizDao().getAllNotes()
                notesAdapter!!.setData(notes)
                recycler_view.adapter = notesAdapter
            }
        }


        notesAdapter!!.setOnClickListener(onClicked)

        fabBtnCreateNotiz.setOnClickListener {

            replaceFragment(CreateNotizFragment.newInstance(),true)

        }
    }


    private val onClicked = object : NotizAdapter.OnItemClickListener{
        override fun onClicked(notesId: Int) {

            var fragment : Fragment
            var bundle = Bundle()
            bundle.putString("edit","isEdit")
            bundle.putInt("noteId",notesId)
            fragment = CreateNotizFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,true)
        }

    }



    fun replaceFragment(fragment: Fragment, istransition:Boolean){
    val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
    if (istransition){
        fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
    }
    fragmentTransition.add(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()

}
}

