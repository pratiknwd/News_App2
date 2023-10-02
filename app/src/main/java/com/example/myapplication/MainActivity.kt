package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity_CommTag"

    //now create list of type slider items
    val sliderItems: MutableList<SliderItems> = ArrayList()

    val titles = ArrayList<String?>()
    val desc = ArrayList<String?>()
    val images = ArrayList<String?>()
    val newslinks = ArrayList<String?>()
    val heads = ArrayList<String?>()

    lateinit var mRef: DatabaseReference
    var verticalViewPager: VerticalViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        verticalViewPager = findViewById(R.id.verticalViewPager)
        mRef = FirebaseDatabase.getInstance().getReference("news")
        getNewsFromRealtimeDB()
    }

    open fun getNewsFromRealtimeDB() {
        mRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) {
                    //add data to array list
                    titles.add(ds.child("title").getValue(String::class.java))
                    desc.add(ds.child("desc").getValue(String::class.java))
                    images.add(ds.child("imagelink").getValue(String::class.java))
                    newslinks.add(ds.child("newslink").getValue(String::class.java))
                    heads.add(ds.child("head").getValue(String::class.java))
                }
                for (i in images.indices) {
                    //here we add slider items with the images that are store in images array list....
                    sliderItems.add(SliderItems(images[i]!!))

                    //we change int to string because now we retrieve image link and save to array list...istead of drwable image
                }
                verticalViewPager!!.adapter =
                    ViewPagerAdapter(
                        this@MainActivity, sliderItems, titles, desc, newslinks, heads,
                        verticalViewPager!!
                    )

                //now add all array list in adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: error getting data from firebase")
            }
        })
    }
}
