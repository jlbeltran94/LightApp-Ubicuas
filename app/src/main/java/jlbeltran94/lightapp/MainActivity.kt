package jlbeltran94.lightapp

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jlbeltran94.lightapp.databinding.ActivityMainBinding
import jlbeltran94.lightapp.models.Data
import jlbeltran94.lightapp.net.ApiClient
import jlbeltran94.lightapp.net.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var col: ColorDrawable
    lateinit var client: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        col = colorPreview.background as ColorDrawable
        client = ApiClient.getInstance()
        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),progress,Color.green(col.color),Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),progress,Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })



        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),Color.green(col.color), progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }


        })

        whiteBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val alp: Int = 255 - progress
                colorPreview.backgroundColor = Color.argb(alp,Color.red(col.color),Color.green(col.color),Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        button.setOnClickListener { sendData() }

    }

    fun sendData(){
        val checked: Int = if(switch1.isChecked) 1 else 0
        val red: Int = Color.red(col.color)
        val green: Int = Color.green(col.color)
        val blue: Int = Color.blue(col.color)
        val alpha: Int = Color.alpha(col.color)

        client.sendData(Data(red,green,blue,alpha, checked))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("MSG", it.msg)
                        },
                        onError = {
                            Log.e("ERROR", "ERROR")
                        }
        )
    }
}
