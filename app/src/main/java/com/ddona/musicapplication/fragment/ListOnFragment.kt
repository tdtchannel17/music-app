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
import androidx.recyclerview.widget.GridLayoutManager
import com.ddona.musicapplication.R
import com.ddona.musicapplication.adapter.MusicOnlineAdapter
import com.ddona.musicapplication.databinding.FragmentListonBinding
import com.ddona.musicapplication.model.MusicOnline
import com.ddona.musicapplication.service.MusicServiceOnline
import androidx.lifecycle.Observer
import com.ddona.musicapplication.MusicActivity
import com.ddona.musicapplication.MyApp

class ListOnFragment : Fragment(), MusicOnlineAdapter.IMusicOnline, View.OnClickListener {
    private lateinit var binding: FragmentListonBinding
    private var connect: ServiceConnection? = null
    private var service: MusicServiceOnline? = null
    private var animation: Animation? = null
    private var indexCurrent: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListonBinding.inflate(
            inflater,
            container,
            false
        )
        binding.rc.layoutManager = GridLayoutManager(context, 3)
        binding.rc.adapter = MusicOnlineAdapter(this)

        binding.data = (context!!.applicationContext as MyApp).songModelOnline

        animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        binding.ivAvatar.startAnimation(animation)

        openServiceUnBound()
        createConnectService()

        binding.btnSearch.setOnClickListener(this)
        register()

        return binding.root
    }

    private fun register() {
        //dang ky su thay doi gia tri cua musicOnlines: observe
        (context!!.applicationContext as MyApp)
            .songModelOnline.musicOnlines.observe(this, Observer {
//                it: gia tri thay doi
                binding.rc.adapter!!.notifyDataSetChanged()
            })
    }

    private fun openServiceUnBound(){
        val intent = Intent()
        intent.setClass(context!!, MusicServiceOnline::class.java)
        //bat service unbound
        //moi lan goi startService thi chac chan vao onStartCommand
        context!!.startService(intent)
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
                    binding.rc.adapter!!.notifyDataSetChanged()
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

    override fun onItemClick(position: Int) {
        indexCurrent = position
        service?.playMusic(position)
        controlMusic(position)
        (activity as MusicActivity).addDetailFragment(position)
    }

    override fun getCount(): Int {
        if (service == null) {
            return 0
        }
        return service!!.getMusicOnlines().size
    }

    override fun getData(position: Int): MusicOnline {
        return service!!.getMusicOnlines()[position]
    }

    fun playPause(linkMusic: String, setBtnPlay: Boolean) {
        if (linkMusic != null && setBtnPlay == true) {
            service!!.play()
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
        } else if (linkMusic != null && setBtnPlay == false) {
            service!!.pause()
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play)
        }
    }

    private fun controlMusic(indexCurrent: Int) {
        this.indexCurrent = indexCurrent
        var linkMusic = service!!.getMusicOnlines()[this.indexCurrent!!].linkMusic
        var setBtnPlay = true

        if (linkMusic == null) {
            service!!.getLinkMusicAsyn(
                service!!.getMusicOnlines()[this.indexCurrent!!].linkHtml,
                this.indexCurrent!!
            )
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
        } else {
            service!!.playMusic(this.indexCurrent!!)
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
        }

        binding.btnPlay.setOnClickListener {
            setBtnPlay = !setBtnPlay
            initialiseSeeBar()
            playPause(linkMusic.toString(), setBtnPlay)
        }

        binding.btnNext.setOnClickListener {
            initialiseSeeBar()
            this.indexCurrent = (this.indexCurrent!! + 1) % service!!.getMusicOnlines().size
            if (linkMusic == null) {
                service!!.getLinkMusicAsyn(
                    service!!.getMusicOnlines()[this.indexCurrent!!].linkHtml,
                    this.indexCurrent!!
                )
            } else {
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
                service!!.transferMusic(this.indexCurrent!!)
            }
        }

        binding.btnPrevious.setOnClickListener {
            initialiseSeeBar()
            if (this.indexCurrent!! == 0) {
                this.indexCurrent = service!!.getMusicOnlines().size - 1
            } else {
                this.indexCurrent = (this.indexCurrent!! - 1) % service!!.getMusicOnlines().size
            }
            if (linkMusic == null) {
                service!!.getLinkMusicAsyn(
                    service!!.getMusicOnlines()[this.indexCurrent!!].linkHtml,
                    this.indexCurrent!!
                )
            } else {
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
                service!!.transferMusic(this.indexCurrent!!)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_search -> {
                service?.searchMusicAsyn(binding.edtFilter.text.toString())
            }
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