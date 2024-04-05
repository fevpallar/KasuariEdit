/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
package com.fevly.kasuariprogroom

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fevly.kasuariprogroom.fragment.EditorFragment
import com.fevly.kasuariprogroom.storage.Permission
import com.fevly.kasuariprogroom.storage.StorageManager
import com.fevly.kasuariprogroom.storage.StorageUtil
import com.fevly.kasuariprogroom.textutil.TextProcessing
import com.fevly.kasuariprogroom.transmission.KasuariNetworkChannelManager
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    lateinit var permission: Permission
    lateinit var storageUtil: StorageUtil
    lateinit var storageManager: StorageManager

    lateinit var textProcessing: TextProcessing
    lateinit var kasuariNetworkChannelManager : KasuariNetworkChannelManager
    lateinit var tabLayoutBelow: TabLayout
    lateinit var viewPager: ViewPager

    lateinit var tablayoutAbove: TabLayout

    internal class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList<Fragment>()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }
    private fun createTabIconsBelow() {
        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.text = "Game"
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.note, 0, 0)
        tabLayoutBelow.getTabAt(0)!!.customView = tabOne

    }
    private fun createTabIconsAbove() {
        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.text = "File1.."
        tablayoutAbove.getTabAt(0)?.customView = tabOne
    }

    private fun createViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(EditorFragment(), "Editor..")
//        adapter.addFrag(Fragment2(), "Tab 2")
//        adapter.addFrag(Fragment3(), "Tab 3")
        viewPager.adapter = adapter
    }

    private fun createViewPager2(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(EditorFragment(), "Editor..")
//        adapter.addFrag(Fragment2(), "Tab 2")
//        adapter.addFrag(Fragment3(), "Tab 3")
        viewPager.adapter = adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        permission = Permission()
        storageUtil = StorageUtil()
        storageManager = StorageManager(applicationContext)
        kasuariNetworkChannelManager = KasuariNetworkChannelManager(applicationContext)
        kasuariNetworkChannelManager.exposeServicePleaseDoItNowKasuari("sampleservicename",9999)


        permission.askRuntimePermission(this)


        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        createViewPager(viewPager)
        tabLayoutBelow = findViewById<View>(R.id.tabs) as TabLayout
        tabLayoutBelow.setupWithViewPager(viewPager)
        createTabIconsBelow()
        tablayoutAbove = findViewById<View>(R.id.tabstop) as TabLayout
        tablayoutAbove.setupWithViewPager(viewPager)
        createTabIconsAbove()


      /*  viewPager2 = findViewById<View>(R.id.viewpager2) as ViewPager
        createViewPager(viewPager2)
        tabLayout2 = findViewById<View>(R.id.tabs2) as TabLayout
        tabLayout2.setupWithViewPager(viewPager2)
      */
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data // uri dari data yg ditarget oleh Intent ini

            var currSelectedFile =
                fileUri //content://com.android.providers.media.documents/document/image%3A44319
            Log.d("kasuariprogroom", "uri file yg diselect " + currSelectedFile)
            val fileNamed = fileUri?.let { storageUtil.getFileNameFromUri(this, it) }
            if (fileNamed != null) {
                Log.d("kasuariprogroom", "namanya " + fileNamed)
            }

            Toast.makeText(
                this,
                "File name = " + fileNamed.toString() + "\n" + "Uri = " + fileUri.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }




}

