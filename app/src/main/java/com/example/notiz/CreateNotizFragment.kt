package com.example.notiz

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notiz.Ent.Notiz
import com.example.notiz.database.NotesDatabase
import com.example.notiz.util.NotizBottomAdapter
import com.example.notiz.util.NotizBottomAdapter.Companion.noteId
import kotlinx.android.synthetic.main.fragment_create_notiz.*
import kotlinx.android.synthetic.main.fragment_create_notiz.imgBack
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notes_bottom_sheet.*
import kotlinx.android.synthetic.main.item_rv_notiz.view.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class CreateNotizFragment : BaseFragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    var selectedColor = "#171C26"
    var currentDatum:String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE= 456
    private var selectedImagePath= ""
    private var webLink= ""
    private var noteId= -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId= requireArguments().getInt("noteId",-1)
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



        if(noteId != -1){

            launch {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).notizDao().getSpecificNote(noteId)
                    colorView.setBackgroundColor(Color.parseColor(notes.color))
                    ednotiztitel.setText(notes.title)
                    ednotizSubTitel.setText(notes.subtitle)
                    ednotizDesc.setText(notes.notizText)
                    if(notes.imgurl != ""){
                        selectedImagePath = notes.imgurl!!

                    imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.imgurl))
                        imgNote.visibility = View.VISIBLE
                        layoutomage.visibility = View.VISIBLE
                        imgDelete.visibility = View.VISIBLE

                }
                else{
                        imgNote.visibility = View.GONE
                        layoutomage.visibility = View.GONE
                        imgDelete.visibility = View.GONE
                }

                    if(notes.weblink != ""){

                        webLink = notes.weblink!!
                        tvWebLink.text = notes.weblink
                        layoutWebUrl.visibility = View.VISIBLE
                        imgURLDelete.visibility = View.VISIBLE
                        edWeblink.setText(notes.weblink)

                    }
                    else{
                        layoutWebUrl.visibility = View.GONE
                        imgURLDelete.visibility = View.GONE
                    }

                }
            }

        }


        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        val cor = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDatum = cor.format(Date())
        colorView.setBackgroundColor(Color.parseColor(selectedColor))

        tvDataTime.text = currentDatum


        imgDone.setOnClickListener {
            //speichern Notiz
            if(noteId != -1){
                updateNote()
            }else{
                speichern()
            }

        }

        imgBack.setOnClickListener {
            replaceFragment(HomeFragment.newInstance(),false)
            //requireActivity().supportFragmentManager.popBackStack()
        }

        imgMore.setOnClickListener{
            var noteBottomSheetFragment = NotizBottomAdapter.newInstance(noteId)
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"Note Bottom Sheet Fragment")
        }

        imgDelete.setOnClickListener {
            layoutomage.visibility = View.GONE
            selectedImagePath = ""
        }

        imgURLDelete.setOnClickListener {
            webLink = ""
            tvWebLink.visibility = View.GONE
            layoutWebUrl.visibility = View.GONE
            imgURLDelete.visibility = View.GONE
        }

        btnOk.setOnClickListener {
            if (edWeblink.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),"Url is Required",Toast.LENGTH_SHORT).show()
            }
        }


        btnCancel.setOnClickListener {
            if(noteId != -1){
                tvWebLink.visibility = View.VISIBLE
                layoutWebUrl.visibility = View.GONE
            }else{
                layoutWebUrl.visibility = View.GONE

            }



        }

        tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(edWeblink.text.toString()))
            startActivity(intent)
        }
    }


    private fun updateNote(){
        launch{


            context?.let {
                var Notiz = NotesDatabase.getDatabase(it).notizDao().getSpecificNote(noteId)

                Notiz.title = ednotiztitel.text.toString()
                Notiz.subtitle = ednotizSubTitel.text.toString()
                Notiz.notizText = ednotizDesc.text.toString()
                Notiz.datatime = currentDatum
                Notiz.color = selectedColor
                Notiz.imgurl = selectedImagePath
                Notiz.weblink = webLink

                NotesDatabase.getDatabase(it).notizDao().updateNote(Notiz)
                ednotiztitel.setText("")
                ednotizSubTitel.setText("")
                ednotizDesc.setText("")
                layoutomage.visibility = View.GONE
                imgNote.visibility = View.GONE
                tvWebLink.visibility = View.GONE
                replaceFragment(HomeFragment.newInstance(),false)
                //requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }


    private fun speichern(){

        if(ednotiztitel.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Titel ist erforderlich", Toast.LENGTH_SHORT).show()
        }

       else  if (ednotizSubTitel.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Untertitel ist erforderlich", Toast.LENGTH_SHORT).show()
        }


       else if (ednotizDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"Notiz Beschreibung muss kann nicht null", Toast.LENGTH_SHORT).show()
        }

        else {

        launch{
            val Notiz = Notiz()

            Notiz.title = ednotiztitel.text.toString()
            Notiz.subtitle = ednotizSubTitel.text.toString()
            Notiz.notizText = ednotizDesc.text.toString()
            Notiz.datatime = currentDatum
            Notiz.color = selectedColor
            Notiz.imgurl = selectedImagePath
            Notiz.weblink = webLink

            context?.let {
                NotesDatabase.getDatabase(it).notizDao().insertNotes(Notiz)
                ednotiztitel.setText("")
                ednotizSubTitel.setText("")
                ednotizDesc.setText("")
                layoutomage.visibility = View.GONE
                imgNote.visibility = View.GONE
                tvWebLink.visibility = View.GONE
                replaceFragment(HomeFragment.newInstance(),false)
                //requireActivity().supportFragmentManager.popBackStack()
            }
          }
        }


    }

    private fun deleteNote(){
        launch {
            context?.let{
                NotesDatabase.getDatabase(it).notizDao().deleteSpecificNote(noteId)
                replaceFragment(HomeFragment.newInstance(),false)
            }
        }
    }

   private fun checkWebUrl(){
       if (Patterns.WEB_URL.matcher(edWeblink.text.toString()).matches()){
           layoutWebUrl.visibility = View.GONE
           edWeblink.isEnabled = false
           webLink = edWeblink.text.toString()
           tvWebLink.visibility = View.VISIBLE
           tvWebLink.text = edWeblink.text.toString()
       }else{
           Toast.makeText(requireContext()," URL ist nicht valid", Toast.LENGTH_SHORT).show()
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
            var actionColor = p1!!.getStringExtra("action")
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
                    readStorageTask()
                    layoutWebUrl.visibility = View.GONE

                }

                "WebUrl" ->{
                  layoutWebUrl.visibility = View.VISIBLE
                }
                "DeleteNote" -> {
                    //delete note
                    deleteNote()
                }


                else -> {
                    layoutomage.visibility = View.GONE
                    imgNote.visibility = View.GONE
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

    private fun hasReadStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasWriteStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }



    private fun readStorageTask(){
        if (hasReadStoragePerm()){


            pickImageFromGallery()
        }else{
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent,REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                var selectedImageUrl = data.data
                if (selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility = View.VISIBLE
                        layoutomage.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    }catch (e:Exception){
                        Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requireActivity())
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }


    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }
}
