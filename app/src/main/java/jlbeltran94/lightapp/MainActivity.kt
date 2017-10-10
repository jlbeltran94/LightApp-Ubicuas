package jlbeltran94.lightapp

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jlbeltran94.lightapp.databinding.ActivityMainBinding
import jlbeltran94.lightapp.util.push
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jetbrains.anko.backgroundColor
import rx.mqtt.android.RxMqtt




/**
 * Created by jlbeltran94 on 7/10/17.
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var col: ColorDrawable
//    lateinit var client: ApiInterface
    val dis:CompositeDisposable = CompositeDisposable()
    val URL:String = "tcp://iot.eclipse.org:1883"
    val rgbCol:String = "/unicauca/light/D0/color"
    val status:String = "/unicauca/light/D0/relay/0"
    val red:String = "/unicauca/light/D0/channel/0"
    val green:String = "/unicauca/light/D0/channel/1"
    val blue:String = "/unicauca/light/D0/channel/2"
    val white:String = "/unicauca/light/D0/channel/3"
    val brightness:String = "/unicauca/light/D0/brightness"

    val mqttAndroidCLient: MqttAndroidClient by lazy{
        RxMqtt.client(applicationContext, URL)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        col = colorPreview.background as ColorDrawable
//        client = ApiClient.getInstance()


        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),progress,Color.green(col.color),Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rxSendRed()
            }

        })

        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),progress,Color.blue(col.color))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rxSendGreen()
            }

        })



        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colorPreview.backgroundColor = Color.argb(Color.alpha(col.color),Color.red(col.color),Color.green(col.color), progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rxSendBlue()
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
                rxSendWhite()
            }

        })

        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rxSendBrightness()
            }

        })



        switch1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                rxSendStatus()
            }

        })

        fabUpdate.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        connect()
    }



    fun rxSendRed(){
        val value = redBar.progress.toString()
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$red/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun rxSendGreen(){
        val value = greenBar.progress.toString()
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$green/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun rxSendBlue(){
        val value = blueBar.progress.toString()
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$blue/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun rxSendWhite(){
        val value = whiteBar.progress.toString()
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$white/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun rxSendStatus(){
        val value = if(switch1.isChecked) "1" else "0"
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$status/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun rxSendBrightness(){
        val value = brightnessBar.progress.toString()
        val encodedValue = value.toByteArray(Charsets.UTF_8)
        val msg = MqttMessage(encodedValue)
        dis push RxMqtt.publish(mqttAndroidCLient, "$brightness/set", msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    override fun onDestroy() {
        super.onDestroy()
        dis.dispose()
    }

    fun connect(){
        dis push RxMqtt.connect(mqttAndroidCLient)
                .map { subscribeAll() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onError = {
                            Log.e("Error de conexion", "Problema al conectar", it)
                        }
                )
    }

    fun subscribeAll(){

        dis push RxMqtt.message(mqttAndroidCLient,red)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            redBar.progress = it.toInt()
                        },
                        onError = {
                            Log.e("ERROR", "RED ERROR", it)
                        })

        dis push RxMqtt.message(mqttAndroidCLient,green)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            greenBar.progress = it.toInt()
                        },
                        onError = {
                            Log.e("ERROR", "GREEN ERROR", it)
                        })

        dis push RxMqtt.message(mqttAndroidCLient,blue)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            blueBar.progress = it.toInt()
                        },
                        onError = {
                            Log.e("ERROR", "BLUE ERROR", it)
                        })

        dis push RxMqtt.message(mqttAndroidCLient,white)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            whiteBar.progress = it.toInt()
                        },
                        onError = {
                            Log.e("ERROR", "WHITE ERROR", it)
                        })

        dis push RxMqtt.message(mqttAndroidCLient, status)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            switch1.isChecked = it == "1"
                        },
                        onError = {
                            Log.e("ERROR", "STATUS ERROR", it)
                        })

        dis push RxMqtt.message(mqttAndroidCLient, brightness)
                .map { String(it.payload) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            brightnessBar.progress = it.toInt()
                        },
                        onError = {
                            Log.e("ERROR", "BRIGHTNESS ERROR", it)
                        })
    }



}
