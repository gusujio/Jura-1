package com.example.jura20.fragments.HomeFragment

import SwipeAdapter
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jura20.R
import com.example.jura20.fragments.HomeFragment.Server.DateUtils
import com.example.jura20.fragments.HomeFragment.Server.Signature
import com.example.jura20.fragments.HomeFragment.data.Item
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.HttpClients
import java.io.*
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment(), View.OnClickListener {

    lateinit var root: View
    lateinit var title: TextView
    lateinit var image: ImageView
    lateinit var recording: ImageView
    var isRecording: Boolean = false
    var recordPermission: String = RECORD_AUDIO
    var PERMISSIOM_CODE: Int = 21
    lateinit var mediaRecorder: MediaRecorder
    lateinit var recordFile: String
    lateinit var timer: Chronometer
    lateinit var swipeAdapter: SwipeAdapter
    lateinit var viewPager: ViewPager2
    lateinit var recordPath: String
    var accessKey =
        "RJ2OXPFYGHU2JBYO5U2F" //The value of this parameter is the AK obtained.

    var securityKey =
        "hm9WlyVZ6efWYaoajuRtFRkxjANPWX3ebN4NTwal" //The value of this parameter is the SK obtained.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_detail, container, false)
        title = root.findViewById(R.id.title)
        image = root.findViewById(R.id.image)
        recording = root.findViewById(R.id.play)
        timer = root.findViewById(R.id.recording_timer)
        image.setImageResource(Integer.parseInt(arguments?.getString("IMG").toString()))
        title.text = arguments?.getString("TITLE")
        val id = arguments?.getInt("id")
        viewPager = root.findViewById(R.id.view_pager)
        viewPager.offscreenPageLimit
        swipeAdapter = id?.let { SwipeAdapter(context, it) }!!
        viewPager.adapter = swipeAdapter
        viewPager.currentItem
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                swipeAdapter.pagePosition = position
            }
        })
        recording.setOnClickListener(this)
        return root
    }


    companion object {
        fun newInstance(text: Item) : DetailFragment {
            val detail = DetailFragment()
            val bundle = Bundle()
            bundle.putString("TITLE", text.title)
            bundle.putInt("id", text.id)
            bundle.putString("IMG", text.image.toString())
            detail.arguments = bundle
            return detail
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        if(isRecording){
            stopRecording()
            val position = swipeAdapter.pagePosition
            AsyncRequest().execute("123", "/ajax", "foo=bar")
            requestServerPython(swipeAdapter.list[position]) // TODO
            val map = mapOf("Meet" to "red", "my" to "green", "family" to "green",
                "There" to "green", "are" to "green", "five" to "red", "of" to "green", "as" to "green",
                "my" to "green", "parents" to "green", "brother" to "green", "baby" to "green", "sister" to "green",
                "and" to "red", "me" to "green")
            swipeAdapter.list[position] = getFormattedHtmlText(map)
            swipeAdapter.notifyItemChanged(position)
            recording.setImageResource(R.drawable.micro)
            isRecording = false
        }
        else{
            if(checkPermissions()) {
                startRecording()
                recording.setImageResource(R.drawable.nomicro)
                isRecording = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestServerPython(text: String) {
        // ...
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        text.replace(' ', '_')
        val url = "https://3490d184757d4bae8efe35b67955dff1.apig.ru-moscow-1.hc.sbercloud.ru/api/getMap/?text=text&file=" + recordFile
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText( context, "Response is: ${response.substring(0, response.length)}", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {Toast.makeText( context, "error Server mzf", Toast.LENGTH_LONG).show()})
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun putObjectToBucket() {
        var inputStream: InputStream? = null;
        var httpClient = HttpClients.createDefault()
        var httpResponse: CloseableHttpResponse? = null
        var requesttime = DateUtils().formateDate(currentTimeMillis())
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
        catch (e:UnsupportedEncodingException) {
            e.printStackTrace();
        }
        catch (e:IOException ) {
            e.printStackTrace();
        }
        finally {
            try {
            httpClient.close()
        } catch (e:IOException) {
            e.printStackTrace(); }
        }

    }

    private fun startRecording() {
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        val format = SimpleDateFormat("yyyy_dd_hh_mm_ss", Locale.CANADA)
        val date = Date()
        recordPath = activity?.getExternalFilesDir("/")?.absolutePath.toString()
        recordFile = "Recording_"+ format.format(date)+ ".wav"
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }


    private fun stopRecording() {
        timer.stop()
        timer.clearComposingText()
        mediaRecorder.stop()
        mediaRecorder.release()
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

    private fun getFormattedHtmlText(stringMap: Map<String, String>): String {
        val stringBuilder = StringBuilder()
        for ((key, value) in stringMap) {
            if (value == "red") {
                stringBuilder.append("<font color='red'>").append(key).append(" ")
                    .append("</font>").append("\n")
            } else {
                stringBuilder.append(key).append(" ")
            }
        }
        return stringBuilder.toString()
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

}

