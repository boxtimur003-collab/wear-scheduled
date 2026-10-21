package com.example.wearschedule.phone

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val edit = EditText(this).apply {
            hint = "08:30 Математика\n09:25 Русский"
            minLines = 5
        }
        
        val btn = Button(this).apply { text = "Сохранить и отправить на часы" }
        val status = TextView(this).apply { text = "Готово" }

        layout.addView(edit)
        layout.addView(btn)
        layout.addView(status)
        setContentView(layout)

        btn.setOnClickListener {
            val lines = edit.text.toString().split("\n").filter { it.isNotBlank() }
            val json = Gson().toJson(lines)

            val req = PutDataMapRequest.create("/schedule").apply {
                dataMap.putString("json", json)
            }.asPutDataRequest()

            Wearable.getDataClient(this).putDataItem(req).addOnCompleteListener { task ->
                runOnUiThread {
                    status.text = if (task.isSuccessful) "Отправлено на часы!" else "Ошибка: проверь Bluetooth"
                }
            }
        }
    }
}
