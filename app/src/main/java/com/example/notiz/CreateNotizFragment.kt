package com.example.notiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notiz.Ent.Notiz
import com.example.notiz.database.NotizDataBase
import kotlinx.android.synthetic.main.fragment_create_notiz.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNotizFragment : BaseFragment() {

    var currentDatum:String? = null
    
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
        return inflater.inflate(R.layout.fragment_create_notiz, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNotizFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cor = SimpleDateFormat("dd/M/yyyy hh:mm::ss")
        currentDatum = cor.format(Date())

        tvDataTime.text = currentDatum


        imgDone.setOnClickListener {
            //speichern Notiz

            speichern()
        }

        imgBack.setOnClickListener {
        replaceFragment(HomeFragment.newInstance(),false)
        }
    }

    private fun speichern(){

        if(ednotiztitel.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Titel ist erforderlich", Toast.LENGTH_SHORT).show()
        }

        if (ednotizSubTitel.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Untertitel ist erforderlich", Toast.LENGTH_SHORT).show()
        }


        if (ednotizDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Beschreibung muss kann nicht null", Toast.LENGTH_SHORT).show()
        }


        launch{
            val Notiz = Notiz()

            Notiz.title = ednotiztitel.text.toString()
            Notiz.subtitle = ednotizSubTitel.text.toString()
            Notiz.notizText = ednotizDesc.text.toString()
            Notiz.datatime = tvDataTime.text.toString()

            context?.let {
                NotizDataBase.getDatabase(it).notizDao().insertNotiz(Notiz)
            }
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