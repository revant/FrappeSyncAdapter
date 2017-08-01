package com.mntechnique.frappesyncadapter

import android.app.DownloadManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.mntechnique.frappesyncadapter.api.FrappeNoteApi
import com.mntechnique.frappesyncadapter.domain.Note
import com.mntechnique.oauth2authenticator.auth.AuthReqCallback
import com.mntechnique.oauth2authenticator.auth.RetrieveAuthTokenTask
import org.jetbrains.anko.button
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout
import org.json.JSONException
import org.json.JSONObject
import za.co.cporm.model.query.Select
import java.util.ArrayList
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var future : RequestFuture<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verticalLayout{
            val b = button("click")
            b.setOnClickListener({
                toast("Clicked!")
                var notes = Select.from(Note::class.java).queryAsList();
                Log.d("Notes",notes.toString());
                val api = FrappeNoteApi(applicationContext)
                val ratt = RetrieveAuthTokenTask(applicationContext, object : AuthReqCallback {
                    override fun onErrorResponse(e: String?) {
                    }

                    override fun onSuccessResponse(s: String?) {
                        api.makeAPICall(FrappeNoteApi.SERIAL, Request.Method.GET,
                                api.getServerURL() + "/api/resource/Note?fields=[\"*\"]",
                                JSONObject(), object : Response.Listener<String>{
                            override fun onResponse(response: String?) {
                                var notesIn = arrayListOf<Note>()
                                val jsonResp = JSONObject(response)
                                val rNotes = jsonResp.getJSONArray("data")
                                for (i in 0..rNotes.length() - 1) {
                                    val n = Note()
                                    n.serverId = rNotes.getJSONObject(i).getString("name")
                                    n.title = rNotes.getJSONObject(i).getString("name")
                                    n.position = rNotes.getJSONObject(i).getString("idx")
                                    n.content = rNotes.getJSONObject(i).getString("content")
                                    notesIn.add(n)
                                }
                                Log.d("LogInterpretedNotes", notesIn.toString())
                            }
                        }, object : Response.ErrorListener{
                            override fun onErrorResponse(error: VolleyError?) {
                                //To change body of created functions use File | Settings | File Templates.
                            }

                        }, s)



                    }

                })
                ratt.execute()
            })
        }
    }
}
