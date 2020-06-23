package com.huawei.codelabs.hihealth.happysport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.text.SimpleDateFormat
import java.util.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage = AboutPage(this)
            .isRTL(false)
            .addItem(Element().setTitle("Version: 1.0.0"))
            .addItem(
                Element().setTitle(
                    "Build Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(
                        Calendar.getInstance().time
                    )}"
                )
            )
            .addWebsite("https://developer.huawei.com/consumer/cn/huaweihealth", "click me to visit our website")
            .setDescription("codelab for HiHealth Core")
            .create()

        setContentView(aboutPage)
    }
}