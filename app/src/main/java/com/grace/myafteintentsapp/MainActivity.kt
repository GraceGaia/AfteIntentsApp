package com.grace.myafteintentsapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share


class MainActivity : AppCompatActivity() {
    var button_call:Button? = null
    var button_sms:Button? = null
    var button_email:Button? = null
    var button_share:Button? = null
    var button_mpesa:Button? = null
    var button_camera:Button? = null
    var imageview_photo:ImageView? = null
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_call = findViewById(R.id.mBtnCall)
        button_sms = findViewById(R.id.mBtnSms)
        button_email = findViewById(R.id.mBtnEmail)
        button_share = findViewById(R.id.mBtnShare)
        button_mpesa = findViewById(R.id.mBtnSik)
        button_camera = findViewById(R.id.mBtnCamera)
        imageview_photo = findViewById(R.id.mImgPic)

        button_call!!.setOnClickListener {
//            makeCall("0714359957")
//            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918511812660"))
//            if (ContextCompat.checkSelfPermission(
//                    this@MainActivity,
//                    Manifest.permission.CALL_PHONE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this@MainActivity,
//                    arrayOf(Manifest.permission.CALL_PHONE),
//                    1
//                )
//            } else {
//                startActivity(intent)
//            }
            val phone = ""
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)

        }
        button_sms!!.setOnClickListener {
            sendSMS("0798654321")
        }
        button_email!!.setOnClickListener {
            email("ggw@gmail.com","REQUEST FOR LEAVE","Dear Sir,.")
        }
        button_share!!.setOnClickListener {
            share("let me m-pesa you. Send me your number")
        }
        button_mpesa!!.setOnClickListener {
            val simToolKitLaunchIntent: Intent? =
                this@MainActivity.getPackageManager().getLaunchIntentForPackage("com.android.stk")
            simToolKitLaunchIntent?.let { startActivity(it) }
        }
        button_camera!!.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

    }

//Open the camera to take a photo
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    //Requesting permission from the user
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Setting an image on an image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            imageview_photo!!.setImageURI(image_uri)
        }
    }


}