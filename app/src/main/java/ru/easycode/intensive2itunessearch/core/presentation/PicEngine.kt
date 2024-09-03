package ru.easycode.intensive2itunessearch.core.presentation

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.easycode.intensive2itunessearch.R

interface PicEngine {

    fun show(imageView: ImageView, url: String)

    class Base : PicEngine {

        override fun show(imageView: ImageView, url: String) {
            Picasso.get()
                .load(url)
                .into(imageView)
        }
    }

    class Mock : PicEngine {

        override fun show(imageView: ImageView, url: String) {
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }
}