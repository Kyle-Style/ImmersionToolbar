package vip.superbrain.immersion.toolbar

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-11 15:42
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initEmoji()
    }

    fun initEmoji() {

        try {
            val packageInfo =
                this.packageManager.getPackageInfo(this.packageName, 0)
            val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.fonts_certs
            )
            val config =
                FontRequestEmojiCompatConfig(applicationContext, fontRequest)
                    .setReplaceAll(true)
                    .registerInitCallback(object : EmojiCompat.InitCallback() {
                        override fun onInitialized() {
                            Log.i(TAG, "EmojiCompat initialized")
                        }

                        override fun onFailed(@Nullable throwable: Throwable?) {
                            Log.e(TAG, "EmojiCompat initialization failed", throwable)
                        }
                    })
            EmojiCompat.init(config)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}