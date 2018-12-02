package cn.com.android.hdvod

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val path1 = "/mnt/usbhost1/1.mp4"
    private val path2 = "/mnt/usbhost1/2.mp4"
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_play.setOnClickListener {
            player.start()
        }
        btn_pause.setOnClickListener {
            player.pause()
        }
        btn_stop.setOnClickListener {
            player.stop()
        }
        btn_next.setOnClickListener {
            if (isFirst)
                player.nextSong(path2)
            else
                player.nextSong(path1)

            isFirst = !isFirst
        }
        btn_up.setOnClickListener {
            player.upVolume()
        }
        btn_down.setOnClickListener {
            player.downVolume()
        }
        btn_track.setOnClickListener {
            player.setTrack()
        }
        btn_reboot.setOnClickListener {
            val intent = Intent()
            intent.action = "android.com.ynh.reboot"
            sendBroadcast(intent)
        }
        btn_poweroff.setOnClickListener {
            val intent = Intent()
            intent.action = "android.com.ynh.power_off"
            sendBroadcast(intent)
        }

    }
}
