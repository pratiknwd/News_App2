package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide

class ViewPagerAdapter(
    var context: Context,
    var sliderItems: List<SliderItems>,
    var titles: ArrayList<String?>,
    var desc: ArrayList<String?>,
    var newslinks: ArrayList<String?>,
    var heads: ArrayList<String?>,
    var verticalViewPager: VerticalViewPager
) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
    var newposition = 0
    var x1 = 0f
    var x2 = 0f

    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return sliderItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.item_container, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val imageView2 = itemView.findViewById<ImageView>(R.id.imageView2)
        val title = itemView.findViewById<TextView>(R.id.headline)
        val desctv = itemView.findViewById<TextView>(R.id.desc)
        val head = itemView.findViewById<TextView>(R.id.head)
        val tapToOpen = itemView.findViewById<TextView>(R.id.taphere)
        tapToOpen.setOnClickListener { openWebView(position) }

        //set data from array list to textview
        title.text = titles[position]
        desctv.text = desc[position]
        head.text = heads[position]
        Glide.with(context)
            .load(sliderItems[position].image)
            .centerCrop()
            .into(imageView)
        Glide.with(context)
            .load(sliderItems[position].image)
            .centerCrop()
            .override(12, 12)
            .into(imageView2)
        verticalViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                //save position value in newposition variable on page change
                newposition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        verticalViewPager.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> x1 = motionEvent.x
                MotionEvent.ACTION_UP -> {
                    x2 = motionEvent.x
                    val deltaX = x1 - x2
                    if (deltaX > 300) {
                        openWebView(position)
                    }
                }
            }
            false
        }
        container.addView(itemView)
        return itemView
    }

    private fun openWebView(position: Int) {
        val i = Intent(context, NewsDetailActivity::class.java)
        if (position == 1) {
            //for first page
            i.putExtra("url", newslinks[0])
            context.startActivity(i)
        } else {
            //when page scrolled
            i.putExtra("url", newslinks[newposition])
            context.startActivity(i)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
