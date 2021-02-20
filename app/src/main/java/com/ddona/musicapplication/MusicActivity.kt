package com.ddona.musicapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ddona.musicapplication.databinding.ActivityMusicBinding
import com.ddona.musicapplication.fragment.DetailFragment
import com.ddona.musicapplication.fragment.ListOffFragment
import com.ddona.musicapplication.fragment.ListOnFragment

class MusicActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)
        addOfflineFragment()
        binding.btnOffline.setOnClickListener(this)
        binding.btnOnline.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_offline -> {
                addOfflineFragment()
            }
            R.id.btn_online -> {
                addOnlineFragment()
            }
        }
    }

    private fun addOfflineFragment() {
        binding.btnOffline.setBackgroundResource(R.color.white)
        binding.btnOnline.setBackgroundResource(R.drawable.color_white)
        val manager = supportFragmentManager
        val tran = manager.beginTransaction()
        val fr = ListOffFragment()

        tran
            .replace(R.id.content, fr)
            .addToBackStack(null)
            .commit()
    }

    fun addOnlineFragment() {
        binding.btnOnline.setBackgroundResource(R.color.white)
        binding.btnOffline.setBackgroundResource(R.drawable.color_white)
        val manager = supportFragmentManager
        val tran = manager.beginTransaction()
        val fr = ListOnFragment()

        tran
            .replace(R.id.content, fr)
            .addToBackStack(null)
            .commit()
    }

    fun addDetailFragment(position: Int) {
        binding.btnOnline.setBackgroundResource(R.color.white)
        binding.btnOffline.setBackgroundResource(R.drawable.color_white)
        val manager = supportFragmentManager
        val tran = manager.beginTransaction()
        val fr = DetailFragment()
        val bundle = Bundle()
        bundle.putInt("POSITION", position)
        fr.arguments = bundle

        tran
            .replace(R.id.content, fr)
            .addToBackStack(null)
            .commit()
    }
}