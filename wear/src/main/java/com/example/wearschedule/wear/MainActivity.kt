package com.example.wearschedule.wear

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), DataClient.OnDataChangedListener {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        textView = TextView(this).apply {
            text = "Нет расписания"
            setPadding(16, 16, 16, 16)
            textSize = 14f
        }
        setContentView(textView)
        loadCache()
    }

    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(events: DataEventBuffer) {
        for (event in events) {
            if (event.dataItem.uri.path == "/schedule") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val json = dataMap.getString("json") ?: continue
                val lines = Gson().fromJson(json, Array<String>::class.java).toList()
                updateUI(lines)
                getSharedPreferences("prefs", MODE_PRIVATE).edit().putString("json", json).apply()
            }
        }
    }

    private fun loadCache() {
        val json = getSharedPreferences("prefs", MODE_PRIVATE).getString("json", null)
        if (json != null) {
            val lines = Gson().fromJson(json, Array<String>::class.java).toList()
            updateUI(lines)
        }
    }

    private fun updateUI(lines: List<String>) {
        if (lines.isEmpty()) {
            textView.text = "Нет уроков"
        } else {
            textView.text = lines.joinToString("\n")
        }
    }
}
