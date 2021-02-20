package com.ddona.musicapplication.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.musicapplication.R
import com.ddona.musicapplication.adapter.MusicOfflineAdapter
import com.ddona.musicapplication.databinding.FragmentListoffBinding
import com.ddona.musicapplication.model.MediaManagerOffline
import com.ddona.musicapplication.model.MusicOffline

class ListOffFragment : Fragment(), MusicOfflineAdapter.IMusic {
    private lateinit var binding: FragmentListoffBinding
    private val musicOffLines = mutableListOf<MusicOffline>()
    private var indexCurrent = 0
    private var animation: Animation? = null
    private val play = MediaManagerOffline()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListoffBinding.inflate(
            inflater, container, false
        )
        getAllMusic()
        binding.rc.layoutManager = LinearLayoutManager(context)
        binding.rc.adapter = MusicOfflineAdapter(this)
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        binding.ivAvatar.startAnimation(animation)

        return binding.root
    }

    private fun getAllMusic() {
        musicOffLines.clear()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = context!!.contentResolver
            .query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val albumIds = mutableListOf<Long>()
        while (!cursor.isAfterLast) {
            val path = cursor.getString(
                cursor.getColumnIndex("_data")
            )
            val name = cursor.getString(
                cursor.getColumnIndex(
                    MediaStore.Audio.Media.DISPLAY_NAME
                )
            )
            val duration = cursor.getLong(
                cursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION
                )
            )
            val albumId = cursor.getLong(
                cursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM_ID
                )
            )

            musicOffLines.add(
                MusicOffline(
                    name, path,
                    duration = duration,
                    albumId = albumId,
                    uriAlbum = Uri.parse("content://media/external/audio/albumart/" + albumId)
                )
            )
            if (albumId > 0) {
                albumIds.add(albumId)
            }
            cursor.moveToNext()
        }
        cursor.close()
    }

    override fun getCount() = musicOffLines.size

    override fun getData(position: Int): MusicOffline {
        return musicOffLines[position]
    }

    override fun onClick(position: Int) {
        play.release()
        controlMusic(position)
    }

    private fun controlMusic(indexCurrent: Int) {
        this.indexCurrent = indexCurrent
        var setBtnPlay = true
        playMusic(musicOffLines[this.indexCurrent].path, setBtnPlay)

        binding.btnPlay.setOnClickListener {
            setBtnPlay = !setBtnPlay
            playMusic(musicOffLines[this.indexCurrent].path, setBtnPlay)
        }

        binding.btnNext.setOnClickListener {
            this.indexCurrent = (this.indexCurrent + 1) % musicOffLines.size
            transferMusic(musicOffLines[this.indexCurrent].path)
        }

        binding.btnPrevious.setOnClickListener {
            if (this.indexCurrent > 0) {
                this.indexCurrent = (this.indexCurrent - 1) % musicOffLines.size
            }else{
                this.indexCurrent = musicOffLines.size - 1
            }
            transferMusic(musicOffLines[this.indexCurrent].path)
        }
    }

    private fun transferMusic(uri: String) {
        binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
        play.release()
        play.setData(uri)
        initialiseSeeBar()
        play.play()

        binding.seerbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) play.mp()?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun playMusic(uri: String, setBtnPlay: Boolean) {
        if (setBtnPlay == true) {
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause)
            play.setData(uri)
            initialiseSeeBar()
            play.play()
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play)
            if (play.mp() != null) play.pause()
        }

        binding.seerbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) play.mp()?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun initialiseSeeBar() {
        binding.seerbar.max = play.mp()?.duration!!

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seerbar.progress = play.mp()!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seerbar.progress = 0
                }
            }
        }, 0)
    }
}