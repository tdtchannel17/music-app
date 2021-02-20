package com.ddona.musicapplication.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ddona.musicapplication.MyApp
import com.ddona.musicapplication.R
import com.ddona.musicapplication.databinding.FragmentDetailBinding
import com.ddona.musicapplication.service.MusicServiceOnline

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var connect: ServiceConnection? = null
    private var service: MusicServiceOnline? = null
    private var animation: Animation? = null
    private var indexCurrent: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(
            inflater, container, false
        )

        animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        binding.ivAvatar.startAnimation(animation)
        indexCurrent = arguments?.getInt("POSITION")
        controlMusic(indexCurrent!!)
        createConnectService()
        register()

        return binding.root
    }

    private fun register() {
        (context!!.applicationContext as MyApp)
            .songModelOnline.musicOnlines.observe(this, Observer {
                service
            })
    }

    private fun createConnectService() {
        //tao cau
        connect = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {

            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
                val myBinder = binder as MusicServiceOnline.MyBinder
                service = myBinder.service
                if (service!!.getMusicOnlines().size == 0) {
                    service?.searchMusicAsyn("")
                } else {
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
                }

            }
        }
        //tao intent de xac dinh can ket noi den service nao
        val intent = Intent()
        intent.setClass(context!!, MusicServiceOnline::class.java)
        //gui yeu cau
        context!!.bindService(intent, connect!!, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroyView() {
        context!!.unbindService(connect!!)
        super.onDestroyView()
    }

    fun playPause(setBtnPlay: Boolean) {
        if (setBtnPlay == true) {
            service!!.play()
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
        } else if (setBtnPlay == false) {
            service!!.pause()
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play)
        }
    }

    private fun controlMusic(indexCurrent: Int) {
        Log.d("indexCurrent", "" + indexCurrent)
        this.indexCurrent = indexCurrent
        var setBtnPlay = true

        binding.btnPlay.setOnClickListener {
            setBtnPlay = !setBtnPlay
            initialiseSeeBar()
            playPause(setBtnPlay)
        }

        binding.btnNext.setOnClickListener {
            this.indexCurrent = (this.indexCurrent!! + 1) % service!!.getMusicOnlines().size
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
                service!!.transferMusic(this.indexCurrent!!)
        }

        binding.btnPrevious.setOnClickListener {
            if (this.indexCurrent!! > 0) {
                this.indexCurrent = (this.indexCurrent!! - 1) % service!!.getMusicOnlines().size
            } else {
                this.indexCurrent = service!!.getMusicOnlines().size - 1
            }
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
                service!!.transferMusic(this.indexCurrent!!)
        }
    }

    private fun initialiseSeeBar() {
        binding.seerbar.max = service?.duration()!!

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seerbar.progress = service!!.media().currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seerbar.progress = 0
                }
            }
        }, 0)
    }
}