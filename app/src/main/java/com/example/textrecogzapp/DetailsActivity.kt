package com.example.textrecogzapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class DetailsActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    lateinit var bmp:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        val extras = intent.extras
        val byteArray = extras!!.getByteArray("image")

         bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)


        imageView.setImageBitmap(bmp)

    }

    fun handleGenerateText(view: View) {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bmp, 0)


        val result = recognizer.process(image)
            .addOnSuccessListener { result ->
                var mainText=""
                val resultText = result.text
                for (block in result.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox

                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox

                            mainText+=elementText+" "
                        }
                    }
                }
                textView.text = mainText

            }
            .addOnFailureListener { e ->
                Toast.makeText(this@DetailsActivity,"Failed...try again..use proper image",Toast.LENGTH_LONG).show()
            }

    }
}