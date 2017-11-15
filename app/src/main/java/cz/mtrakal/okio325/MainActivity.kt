package cz.mtrakal.okio325

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val json = MediaType.parse("application/json; charset=utf-8")

    private val debug = if (BuildConfig.DEBUG) {
        "DEBUG"
    } else {
        "RELEASE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vDebug.text = debug

        okhttp()
    }

    private fun okhttp() {
        val client = OkHttpClient.Builder()
                .authenticator { route, response ->
                    val credential = Credentials.basic("d85d5b39-01c4-41e9-bc97-05c19a007335", "9203ed97-de25-4aea-994c-29631b311ff1")
                    response.request().newBuilder().header("Authorization", credential).build()
                }
                .build()

        val request = Request.Builder()
                .addHeader("Accept", json.toString())
                .get()
                .url("http://okio.mtrakal.cz/")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call", "On failure")
                onOutputUi(e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.d("Call", "On response")
                onOutputUi(response.body()?.string())
            }
        })
    }

    private fun onOutputUi(output: String?) {
        runOnUiThread({
            vOutput.text = output
        })
    }
}
