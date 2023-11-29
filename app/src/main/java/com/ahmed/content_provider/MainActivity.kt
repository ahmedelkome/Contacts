package com.ahmed.content_provider

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.content_provider.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var contacts = ArrayList<contact>()
    lateinit var binding: ActivityMainBinding
    private val REQ_permission = 1
    private var permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,permission,REQ_permission)
        }else
        {
            MyTask().execute()
        }
    }

    private fun readcontacts() {
        contacts = ArrayList()
        var cursor : Cursor? = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME+" ASC")
        if (cursor!!.moveToFirst()){
            do {
                var id : Long = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val curi : Uri = ContactsContract.Data.CONTENT_URI
                var cCursor:Cursor? = contentResolver.query(curi,null
                    ,ContactsContract.Data.CONTACT_ID + "=?"
                    , arrayOf(arrayOf(id).toString())
                    ,null)
                var displayName : String = ""
                var nickname : String = ""
                var homephone : String = ""
                var mobilephone : String = ""
                var workphone : String = ""
                var photopath : String = "" + R.drawable.ic_launcher_contacts_foreground
                var photobyte : ByteArray? = null
                var homeemail : String = ""
                var workemail : String = ""
                var companyname : String = ""
                var title : String = ""
                var contactnumbers : String = ""
                var contactemailaddresses : String = ""
                var contactotherdetails : String = ""
                if (cCursor!!.moveToFirst()){
                    displayName = cCursor.getString(cCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                do{
                    if (cCursor.getString(cCursor.getColumnIndex("mimetype"))==
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                    {
                        nickname = cCursor.getString(cCursor.getColumnIndex("data1"))
                        contactotherdetails += nickname+ "\n"
                    }

                    if (cCursor.getString(cCursor.getColumnIndex("mimetype"))==
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    {
                        when(cCursor.getInt(cCursor.getColumnIndex("data2")))
                        {
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                                homephone = cCursor.getString(cCursor.getColumnIndex("data1"))
                                contactnumbers += "Home phone$homephone n"
                            }
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> {
                                workphone = cCursor.getString(cCursor.getColumnIndex("data1"))
                                contactnumbers += "Home phone$workphone n"
                            }
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                                mobilephone = cCursor.getString(cCursor.getColumnIndex("data1"))
                                contactnumbers += "Home phone$mobilephone n"
                            }
                        }
                    }
                    //read email
                    if (cCursor.getString(cCursor.getColumnIndex("mimetype"))==
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    {
                        when(cCursor.getInt(cCursor.getColumnIndex("data2")))
                        {
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                                homeemail = cCursor.getString(cCursor.getColumnIndex("data1"))
                                contactemailaddresses += "Home phone$homeemail n"
                            }
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> {
                                workemail = cCursor.getString(cCursor.getColumnIndex("data1"))
                                contactemailaddresses += "Home phone$workemail n"
                            }
                        }
                    }
                    if (cCursor.getString(cCursor.getColumnIndex("mimetype")).
                        equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
                    {
                        companyname = cCursor.getString(cCursor.getColumnIndex("data1"))
                        contactotherdetails += "Company name$companyname n"
                        title = cCursor.getString(cCursor.getColumnIndex("data4"))
                        contactotherdetails +="Title$title n"
                    }
                    if (cCursor.getString(cCursor.getColumnIndex("mimetype")).
                        equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE))
                    {
                        photobyte = cCursor.getBlob(cCursor.getColumnIndex("data15"))
                        if (photobyte!=null){
                            val bitmap:Bitmap = BitmapFactory.decodeByteArray(
                                photobyte,0,photobyte.size
                            )
                            val caheDirectory:File = baseContext.cacheDir
                            val tmp:File = File(caheDirectory.path+"/_androhub"+id+".png")
                            try {
                                val fileOutputStream:FileOutputStream= FileOutputStream(tmp)
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream)
                                fileOutputStream.flush()
                                fileOutputStream.close()
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                            photopath = tmp.path
                        }
                    }
                }while (cCursor.moveToNext())
                contacts.add(
                    contact(id.toString(),displayName
                    ,contactemailaddresses,contactnumbers
                    ,photopath,contactotherdetails)
                )
                }
            }while (cursor.moveToNext())

        }
    }

    private fun populatedcontacts(items : ArrayList<contact>){
        val adapter : contactadapter = contactadapter(items)
        binding.recListContact.adapter = adapter
        binding.recListContact.layoutManager = LinearLayoutManager(this)
        binding.recListContact.setHasFixedSize(true)
    }

    inner class MyTask : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.pb.visibility=View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Void? {
            readcontacts()
            return null
        }

        override fun onPostExecute(result: Void?) {
            binding.pb.visibility = View.GONE
            populatedcontacts(contacts)
        }

    }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQ_permission && grantResults.size > 0) {
                MyTask().execute()
            }
        }

    }
