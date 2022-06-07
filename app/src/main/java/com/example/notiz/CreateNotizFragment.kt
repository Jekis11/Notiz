package com.example.notiz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notiz.Ent.Notiz
import com.example.notiz.database.NotesDatabase
import kotlinx.android.synthetic.main.fragment_create_notiz.*
import kotlinx.android.synthetic.main.fragment_create_notiz.imgBack
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNotizFragment : BaseFragment() {

    var selectedColor = "#171C26"
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

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

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

        imgMore.setOnClickListener{
           // var noteBottomSheetFragment = NotizBottomSheetFragment.newInstance(noteId)
           // noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"Note Bottom Sheet Fragment")
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
                NotesDatabase.getDatabase(it).notizDao().insertNotes(Notiz)
                ednotiztitel.setText("")
                ednotizSubTitel.setText("")
                ednotizDesc.setText("")
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

    private val BroadcastReceiver : BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionColor = p1!!.getStringExtra("actionColor")
            when(actionColor!!){

                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Image" ->{
                    //readStorageTask()
                    layoutWebUrl.visibility = View.GONE
                }

                "WebUrl" ->{
                    layoutWebUrl.visibility = View.VISIBLE
                }
                "DeleteNote" -> {
                    //delete note
                    //  deleteNote()
                }


                else -> {
                    layoutImage.visibility = View.GONE
                    //imgNote.visibility = View.GONE
                    layoutWebUrl.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }
            }
        }

    }

    override fun onDestroy() {

        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }
        }
