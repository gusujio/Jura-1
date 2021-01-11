package com.example.jura20.fragments.InfoFragment

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.example.jura20.R
import com.example.jura20.fragments.HomeFragment.DetailFragment
import com.example.jura20.fragments.HomeFragment.Server.DateUtils
import com.example.jura20.fragments.HomeFragment.Server.Signature
import com.example.jura20.fragments.InfoFragment.data.DataStorageTalk
import com.example.jura20.fragments.InfoFragment.data.Talk
import kotlinx.android.synthetic.main.fragment_info.*
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.HttpClients
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment(), View.OnClickListener {

    lateinit var speech: TextToSpeech
    lateinit var play: ImageButton
    lateinit var listener: ImageButton
    lateinit var mediaRecorder: MediaRecorder
    var isRecording: Boolean = false
    var recordPermission: String = RECORD_AUDIO
    var PERMISSIOM_CODE: Int = 21
    lateinit var recordPath: String
    lateinit var recordFile: String
    lateinit var listenRecord: MediaPlayer
    lateinit var stop: ImageButton
    var accessKey =
        "RJ2OXPFYGHU2JBYO5U2F" //The value of this parameter is the AK obtained.

    var securityKey =
        "hm9WlyVZ6efWYaoajuRtFRkxjANPWX3ebN4NTwal" //The value of this parameter is the SK obtained.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_info, container, false)
        play = root.findViewById(R.id.play)
        listener = root.findViewById(R.id.dynamic)
        play.setOnClickListener(this)
        listener.setOnClickListener {
            if(speech.isSpeaking){
                speech.stop()
                speech.shutdown()
            }
        }
        return root
    }

    override fun onClick(v: View?) {
        if (isRecording){
            stopRecording()
            speech = TextToSpeech(context, TextToSpeech.OnInitListener {
                speech.language = Locale.UK
                Toast.makeText(context, "kkkk", Toast.LENGTH_LONG).show()
            })
            speech.speak("Hello, what about whether", TextToSpeech.QUEUE_FLUSH, null)
            AsyncRequest().execute("123", "/ajax", "foo=bar")
            play.setImageResource(R.drawable.micro)
            isRecording = false
        }
        else{
            if(checkPermissions()){
                startRecording()
                play.setImageResource(R.drawable.nomicro)
                dynamic.setImageResource(R.drawable.stop)
                isRecording = true
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return if(ActivityCompat.checkSelfPermission(requireContext(), recordPermission) == PackageManager.PERMISSION_GRANTED){
            true
        }
        else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(recordPermission),
                    PERMISSIOM_CODE
                )
            }
            false
        }
    }

    private fun startRecording() {
        val format = SimpleDateFormat("yyyy_dd_hh_mm_ss", Locale.CANADA)
        val date = Date()
        recordPath = activity?.getExternalFilesDir("/")?.absolutePath.toString()
        recordFile = "Recording_forBot"+ format.format(date)+ ".wav"
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

    private fun stopRecording() {
        mediaRecorder.stop()
        mediaRecorder.release()
    }

    inner class AsyncRequest : AsyncTask<String?, Int?, String?>() {
        override fun doInBackground(vararg arg: String?): String {
            putObjectToBucket()
            return "hello"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }

    private fun putObjectToBucket() {
        var inputStream: InputStream? = null;
        var httpClient = HttpClients.createDefault()
        var httpResponse: CloseableHttpResponse? = null
        var requesttime = DateUtils().formateDate(System.currentTimeMillis())
        var httpPut = HttpPut("http://jura.obs.ru-moscow-1.hc.sbercloud.ru/" + recordFile); httpPut.addHeader("Date", requesttime);
        /**Calculate the signature based on the request.**/
        var contentMD5 = "";
        var contentType = "";
        var canonicalizedHeaders = "";
        var canonicalizedResource = "/jura/" + recordFile;
        // Content-MD5 and Content-Type fields do not contain line breaks. The data format is RFC 1123, which is the same as the time in the request.
        var canonicalString =
            "PUT\n$contentMD5\n$contentType\n$requesttime\n$canonicalizedHeaders$canonicalizedResource";
        System.out.println("StringToSign:[$canonicalString]");
        var signature:String? = null;
        try {
            signature = Signature().signWithHmacSha1(securityKey, canonicalString) // Directory for storing uploaded files
            inputStream = FileInputStream(recordPath + "/" + recordFile) //TODO
            var entity: InputStreamEntity = InputStreamEntity(inputStream, -1) //TODO??
            httpPut.setEntity(entity);
            httpPut.addHeader("Authorization", "OBS " + accessKey + ":" + signature);
            // Added the Authorization: OBS AccessKeyID:signature field to the header. httpPut.addHeader("Authorization", "OBS " + accessKey + ":" + signature);
            httpResponse = httpClient.execute(httpPut);
            // Prints the sending request information and the received response message. System.out.println("Request Message:"); System.out.println(httpPut.getRequestLine());
            for (header in httpPut.allHeaders) {
                println(header.name + ":" + header.value);
            }
            println("Response Message:"); System.out.println(httpResponse.statusLine);
            for (header in httpResponse.allHeaders) {
                println(header.name + ":" + header.value);
            }

            val reader: BufferedReader = BufferedReader(InputStreamReader( httpResponse.entity.content));
            var inputLine = ""
            val response:StringBuffer = StringBuffer()

            while (inputLine != reader.readLine()) {
                response.append(inputLine);
            }

            reader.close();
            println(response.toString());
        }
        catch (e: UnsupportedEncodingException) {
            e.printStackTrace();
        }
        catch (e: IOException) {
            e.printStackTrace();
        }
        finally {
            try {
                httpClient.close()
            } catch (e: IOException) {
                e.printStackTrace(); }
        }

    }

}

