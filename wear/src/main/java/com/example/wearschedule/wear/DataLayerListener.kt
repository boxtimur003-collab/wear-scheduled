package com.example.wearschedule.wear

import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson

class DataLayerListener : WearableListenerService() {
    override fun onDataChanged(events: DataEventBuffer) {
        for (event in events) {
            if (event.dataItem.uri.path == "/schedule") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val json = dataMap.getString("json") ?: continue
                getSharedPreferences("prefs", MODE_PRIVATE).edit().putString("json", json).apply()
            }
        }
    }
}
