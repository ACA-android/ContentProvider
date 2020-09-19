package com.dm.contentprovider

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val permissionRequestCode = 444
    private val imageRequestCode = 555
    private var contactPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactPermission =
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

        requestPermissions(arrayOf("android.permission.READ_CONTACTS"), permissionRequestCode)

        requestContacts()

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
//        val list = packageManager.queryIntentActivities(intent,
//                PackageManager.GET_ACTIVITIES)
//        startActivityForResult(intent, imageRequestCode)
        startActivityForResult(Intent.createChooser(intent, "choose app"), imageRequestCode)
    }

    private fun requestContacts() {
        if (contactPermission) {
            val contentResolver = contentResolver
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                            ContactsContract.Contacts.STARRED),
                    null,
                    null,
                    null
            )
            if (cursor != null && cursor.count > 0) {
                val sb = StringBuilder("")
                while (cursor.moveToNext()) {
                    sb.append("${cursor.getString(0)} - ${cursor.getInt(1) != 0}\n")

                }
                contactTextView.text = sb
            } else {
                contactTextView.text = "No contacts found"
            }
        } else {
            contactTextView.text = "Please grant permission"
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == permissionRequestCode) {
            contactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            requestContacts()
        }
    }
}