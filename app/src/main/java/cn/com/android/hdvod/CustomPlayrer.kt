package cn.com.android.hdvod

import android.content.Context
import android.hardware.display.DisplayManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.RemoteException
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import java.util.*


/**
 *  Created by yinzhengwei on 2018/12/2.
 *  @Function
 */
class CustomPlayrer(context: Context, attribute: AttributeSet) : SurfaceView(context, attribute) {

    private val TAG = javaClass.name
    private var mMediaPlayer: MediaPlayer = MediaPlayer()
    private var mDisplayManager: DisplayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    private var am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val mTrackAudioIndex = Vector<Int>()
    private var curAudioIndex = 0
    private var trackNum = 0


    fun start() {
        if (!mMediaPlayer.isPlaying) {
            mMediaPlayer.start()
        }
    }

    fun pause() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.pause()
        }
    }

    fun stop() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
    }

    private var maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
    private var defaultVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM)

    fun upVolume() {
        if (defaultVolume == maxVolume) return
        defaultVolume++

        //mMediaPlayer.setVolume(defaultVolume, defaultVolume)

        setVolume(AudioManager.ADJUST_LOWER)
    }

    fun downVolume() {
        if (defaultVolume == 0) return
        defaultVolume--

        // mMediaPlayer.setVolume(defaultVolume, defaultVolume)

        setVolume(AudioManager.ADJUST_RAISE)
    }

    private fun setVolume(type: Int) {
        try {

            //直接设置音量值
            am.setStreamVolume(AudioManager.STREAM_SYSTEM, defaultVolume, AudioManager.FLAG_SHOW_UI)

            //渐进式设置
//            am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
//                    type, AudioManager.FLAG_PLAY_SOUND)
        } catch (e: RemoteException) {
            Log.e(TAG, "Dead object in setStreamVolume", e)
        }
    }

    fun nextSong(path: String) {
        try {

            mMediaPlayer.reset()

            //这个是网络资源
            // mMediaPlayer.setDataSource(context,uri)

            //这个是本地资源
            mMediaPlayer.setDataSource(path)
            mMediaPlayer.setDisplay(holder)
            mMediaPlayer.prepare()
            mMediaPlayer.start()

            getTrack()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //升降调
    fun soundChange() {
        //mMediaPlayer.setPitch(9.0f)
        //mMediaPlayer.setPitch(-6.0f);
    }

    fun setTrack() {
        Log.e(TAG, "mTrackAudioIndex size = " + mTrackAudioIndex.size)

        Toast.makeText(context, "$trackNum", Toast.LENGTH_SHORT).show()

        //如果是单音轨的歌，则切换左右声道
        if (trackNum < 2)
            return

        //左右声道(1左；2右；0正常)
        //mMediaPlayer.setParameter(1102,0);
        //mMediaPlayer.setParameter(1102,1);
        //mMediaPlayer.setParameter(1102,2);

        curAudioIndex = (curAudioIndex + 1) % trackNum
        try {
            mMediaPlayer.selectTrack(mTrackAudioIndex[curAudioIndex]);

        } catch (e: IllegalStateException) {
            Log.d(TAG, "setAudioTrack(): IllegalStateException: set audio track fail")
        } catch (e: RuntimeException) {
            Log.d(TAG, "setAudioTrack(): RuntimeException: set subtitle fail")
        }
    }

    private fun getTrack() {
        val trackInfos = mMediaPlayer.trackInfo
        mTrackAudioIndex.clear()
        curAudioIndex = 0

        trackNum = 0

        if (trackInfos != null && trackInfos.isNotEmpty()) {
            trackInfos.forEachIndexed { j, info ->
                println("******************track type = " + info.trackType)
                val typE_AUDIO = MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO
                println("MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO = " + typE_AUDIO)
                if (info.trackType == typE_AUDIO) {
                    trackNum++
                    Log.e(TAG, "add track ********************** $j")
                    // mTrackInfosAudio.add(info);
                    mTrackAudioIndex.add(j)
                }
            }
        } else {
            if (trackInfos == null) {
                println("trackInfos = null")
            } else {
            }
            System.out.println("trackInfos.length = " + trackInfos.size)
        }
    }
}