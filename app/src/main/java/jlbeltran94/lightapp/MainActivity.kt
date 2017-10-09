package jlbeltran94.lightapp

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.SeekBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jlbeltran94.lightapp.databinding.ActivityMainBinding
import jlbeltran94.lightapp.models.Data
import jlbeltran94.lightapp.net.ApiClient
import jlbeltran94.lightapp.net.ApiInterface
import jlbeltran94.lightapp.util.push
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var col: ColorDrawable
    lateinit var client: ApiInterface
    val dis:CompositeDisposable = CompositeDisposable()

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
                sendData()
            }

        })

        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),progress,Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sendData()
            }

        })



        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),Color.green(col.color), progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sendData()
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
                sendData()
            }

        })

        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sendData()
            }

        })

        switch1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                sendData()
            }

        })

        fabUpdate.setOnClickListener {
            receiveData()
        }

    }

    override fun onResume() {
        super.onResume()
        receiveData()
    }

    fun receiveData(){
        dis push client.receiveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            toast("Data updated")
                            redBar.progress = it.red
                            greenBar.progress = it.green
                            blueBar.progress = it.blue
                            whiteBar.progress = it.white
                            brightnessBar.progress = it.brightness
                            switch1.isChecked = it.status == 1
                        },
                        onError ={
                            toast("Something is wrong")
                        })
    }

    fun sendData(){
        val checked: Int = if(switch1.isChecked) 1 else 0
        val red: Int = redBar.progress
        val green: Int = greenBar.progress
        val blue: Int = blueBar.progress
        val white: Int = whiteBar.progress
        val brightness: Int = brightnessBar.progress

        dis push client.sendData(Data(red,green,blue,white, brightness, checked))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (it.success){
                                toast(it.msg)
                            }
                        },
                        onError = {
                            toast("Something is wrong")
                        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dis.dispose()
    }

}
