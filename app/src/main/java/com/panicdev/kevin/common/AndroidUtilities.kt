/*
 * This is the source code of Telegram for Android v. 1.4.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */
package com.panicdev.kevin.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.util.DisplayMetrics
import android.util.StateSet
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.panicdev.fast_clien.base.BaseApplication
import java.io.*
import java.math.BigDecimal
import java.net.URLDecoder
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

object AndroidUtilities {
    private val typefaceCache = Hashtable<String, Typeface>()
    private var prevOrientation = -10
    var isWaitingForSms = false
        get() {
            var value = false
            synchronized(smsLock) { value = field }
            return value
        }
        set(value) {
            synchronized(smsLock) { field = value }
        }
    private val smsLock = Any()
    var statusBarHeight = 0
    var softKeyHeight = 0
    var density = 1f
    var displaySize = Point()
    var photoSize: Int? = null

    val isTablet: Boolean? = null

    fun checkSize() {
        checkDisplaySize()
        checkStatusBarHeight()
    }

    fun lockOrientation(activity: Activity?) {
        if (activity == null || prevOrientation != -10) {
            return
        }
        try {
            prevOrientation = activity.requestedOrientation
            val manager = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
            if (manager != null && manager.defaultDisplay != null) {
                val rotation = manager.defaultDisplay.rotation
                val orientation = activity.resources.configuration.orientation
                var SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8
                var SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9
                if (Build.VERSION.SDK_INT < 9) {
                    SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                if (rotation == Surface.ROTATION_270) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    }
                } else if (rotation == Surface.ROTATION_90) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    } else {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                } else if (rotation == Surface.ROTATION_0) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                } else {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    } else {
                        activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun unlockOrientation(activity: Activity?) {
        if (activity == null) {
            return
        }
        try {
            if (prevOrientation != -10) {
                activity.requestedOrientation = prevOrientation
                prevOrientation = -10
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTypeface(assetPath: String): Typeface? {
        synchronized(typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    val t = Typeface.createFromAsset(BaseApplication.context.assets, assetPath)
                    typefaceCache[assetPath] = t
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }
            }
            return typefaceCache[assetPath]
        }
    }

    fun showKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val inputManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            view,
            0
        )
    }

    fun isKeyboardShowed(view: View?): Boolean {
        if (view == null) {
            return false
        }
        val inputManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputManager.isActive(view)
    }

    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    val cacheDir: File
        get() {
            var state: String? = null
            try {
                state = Environment.getExternalStorageState()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
                try {
                    val file = BaseApplication.context.externalCacheDir
                    if (file != null) {
                        return file
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            try {
                val file = BaseApplication.context.cacheDir
                if (file != null) {
                    return file
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return File("")
        }

    fun dp(value: Float): Int {
        return Math.ceil((density * value).toDouble()).toInt()
    }

    fun dpf2(value: Float): Float {
        return density * value
    }

    fun dpFromResource(res: Int): Float {
        return resources.getDimensionPixelSize(res).toFloat()
    }

    private fun checkDisplaySize() {
        try {
            val manager =
                BaseApplication.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (manager != null) {
                val display = manager.defaultDisplay
                if (display != null) {
                    if (Build.VERSION.SDK_INT < 13) {
                        displaySize[display.width] = display.height
                    } else {
                        display.getSize(displaySize)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkStatusBarHeight() {
        try {
            var result = 0
            val resourceId = BaseApplication.context.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
            if (resourceId > 0) {
                result = BaseApplication.context.resources.getDimensionPixelSize(resourceId)
            }
            statusBarHeight = result
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkSoftKeyHeight(act: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val metrics = DisplayMetrics()
                act.windowManager.defaultDisplay.getMetrics(metrics)
                val usableHeight = metrics.heightPixels
                act.windowManager.defaultDisplay.getRealMetrics(metrics)
                val realHeight = metrics.heightPixels
                if (realHeight > usableHeight) softKeyHeight =
                    realHeight - usableHeight else softKeyHeight = 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun makeBroadcastId(id: Int): Long {
        return 0x0000000100000000L or (id.toLong() and 0x00000000FFFFFFFFL)
    }

    fun getMyLayerVersion(layer: Int): Int {
        return layer and 0xffff
    }

    fun getPeerLayerVersion(layer: Int): Int {
        return layer shr 16 and 0xffff
    }

    fun setMyLayerVersion(layer: Int, version: Int): Int {
        return layer and -0x10000 or version
    }

    fun setPeerLayerVersion(layer: Int, version: Int): Int {
        return layer and 0x0000ffff or (version shl 16)
    }

    @JvmOverloads
    fun runOnUIThread(runnable: Runnable?, delay: Long = 0) {
        if (delay == 0L) {
            BaseApplication.applicationHandler.post(runnable!!)
        } else {
            BaseApplication.applicationHandler.postDelayed(runnable!!, delay)
        }
    }

    fun cancelRunOnUIThread(runnable: Runnable?) {
        BaseApplication.applicationHandler.removeCallbacks(runnable!!)
    }

    val isSmallTablet: Boolean
        get() {
            val minSide = Math.min(displaySize.x, displaySize.y) / density
            return minSide <= 700
        }
    val minTabletSide: Int
        get() = if (!isSmallTablet) {
            val smallSide =
                Math.min(displaySize.x, displaySize.y)
            var leftSide = smallSide * 35 / 100
            if (leftSide < dp(320f)) {
                leftSide = dp(320f)
            }
            smallSide - leftSide
        } else {
            val smallSide =
                Math.min(displaySize.x, displaySize.y)
            val maxSide =
                Math.max(displaySize.x, displaySize.y)
            var leftSide = maxSide * 35 / 100
            if (leftSide < dp(320f)) {
                leftSide = dp(320f)
            }
            Math.min(smallSide, maxSide - leftSide)
        }

    fun getPhotoSize(): Int {
        if (photoSize == null) {
            if (Build.VERSION.SDK_INT >= 16) {
                photoSize = 1280
            } else {
                photoSize = 800
            }
        }
        return photoSize!!
    }

    fun formatTTLString(ttl: Int): String? {
        /*if (ttl < 60) {
            return LocaleController.formatPluralString("Seconds", ttl);
        } else if (ttl < 60 * 60) {
            return LocaleController.formatPluralString("Minutes", ttl / 60);
        } else if (ttl < 60 * 60 * 24) {
            return LocaleController.formatPluralString("Hours", ttl / 60 / 60);
        } else if (ttl < 60 * 60 * 24 * 7) {
            return LocaleController.formatPluralString("Days", ttl / 60 / 60 / 24);
        } else {
            int days = ttl / 60 / 60 / 24;
            if (ttl % 7 == 0) {
                return LocaleController.formatPluralString("Weeks", days / 7);
            } else {
                return String.format("%s %s", LocaleController.formatPluralString("Weeks", days / 7), LocaleController.formatPluralString("Days", days % 7));
            }
        }*/
        return null
    }

    fun clearCursorDrawable(editText: EditText?) {
        if (editText == null || Build.VERSION.SDK_INT < 12) {
            return
        }
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.setInt(editText, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getViewInset(view: View?): Int {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0
        }
        try {
            val mAttachInfoField = View::class.java.getDeclaredField("mAttachInfo")
            mAttachInfoField.isAccessible = true
            val mAttachInfo = mAttachInfoField[view]
            if (mAttachInfo != null) {
                val mStableInsetsField = mAttachInfo.javaClass.getDeclaredField("mStableInsets")
                mStableInsetsField.isAccessible = true
                val insets = mStableInsetsField[mAttachInfo] as Rect
                return insets.bottom
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    val currentActionBarHeight: Int
        get() = if (isTablet!!) {
            dp(64f)
        } else if (BaseApplication.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dp(48f)
        } else {
            dp(56f)
        }
    val realScreenSize: Point
        get() {
            val size = Point()
            try {
                val windowManager =
                    BaseApplication.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    windowManager.defaultDisplay.getRealSize(size)
                } else {
                    try {
                        val mGetRawW = Display::class.java.getMethod("getRawWidth")
                        val mGetRawH = Display::class.java.getMethod("getRawHeight")
                        size[(mGetRawW.invoke(windowManager.defaultDisplay) as Int)] =
                            (mGetRawH.invoke(windowManager.defaultDisplay) as Int)
                    } catch (e: Exception) {
                        size[windowManager.defaultDisplay.width] =
                            windowManager.defaultDisplay.height
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return size
        }

    fun setListViewEdgeEffectColor(listView: AbsListView?, color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                var field = AbsListView::class.java.getDeclaredField("mEdgeGlowTop")
                field.isAccessible = true
                val mEdgeGlowTop = field[listView] as EdgeEffect
                if (mEdgeGlowTop != null) {
                    mEdgeGlowTop.color = color
                }
                field = AbsListView::class.java.getDeclaredField("mEdgeGlowBottom")
                field.isAccessible = true
                val mEdgeGlowBottom = field[listView] as EdgeEffect
                if (mEdgeGlowBottom != null) {
                    mEdgeGlowBottom.color = color
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearDrawableAnimation(view: View?) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return
        }
        var drawable: Drawable? = null
        if (view is ListView) {
            drawable = view.selector
            if (drawable != null) {
                drawable.state = StateSet.NOTHING
            }
        } else {
            drawable = view.background
            if (drawable != null) {
                drawable.state = StateSet.NOTHING
                drawable.jumpToCurrentState()
            }
        }
    }

    fun replaceBold(str: String): Spannable {
        var str = str
        var start: Int
        val bolds: ArrayList<Int?> = ArrayList()
        while (str.indexOf("<b>").also { start = it } != -1) {
            val end = str.indexOf("</b>") - 3
            str = str.replaceFirst("<b>".toRegex(), "").replaceFirst("</b>".toRegex(), "")
            bolds.add(start)
            bolds.add(end)
        }
        val stringBuilder = SpannableStringBuilder(str)
        for (a in 0 until bolds.size / 2) {
            val span = TypefaceSpan(getTypeface("fonts/rmedium.ttf").toString())
            stringBuilder.setSpan(
                span,
                bolds[a * 2]!!,
                bolds[a * 2 + 1]!!,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        return stringBuilder
    }



    val locale: Locale
        get() = BaseApplication.context.resources.configuration.locale
    val language: String
        get() = locale.language
    val country: String
        get() = locale.country

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     *
     * @param uri
     * @return
     */
    fun getImageFile(uri: Uri?): File? {
        var uri = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var mCursor = BaseApplication.context.contentResolver.query(
            uri!!, projection, null, null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )
        if (mCursor == null || mCursor.count < 1) {
            return null // no cursor or no record
        }
        val column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        mCursor.moveToFirst()
        val path = mCursor.getString(column_index)
        if (mCursor != null) {
            mCursor.close()
            mCursor = null
        }
        return File(path)
    }

    /**
     * 파일 복사
     *
     * @param srcFile  : 복사할 File
     * @param destFile : 복사될 File
     * @return
     */
    fun copyFile(srcFile: File?, destFile: File): Boolean {
        var result = false
        result = try {
            val `in`: InputStream = FileInputStream(srcFile)
            try {
                copyToFile(`in`, destFile)
            } finally {
                `in`.close()
            }
        } catch (e: IOException) {
            false
        }
        return result
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    private fun copyToFile(inputStream: InputStream, destFile: File): Boolean {
        return try {
            val out: OutputStream = FileOutputStream(destFile)
            try {
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
                    out.write(buffer, 0, bytesRead)
                }
            } finally {
                out.close()
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    val testPath = Environment.getExternalStorageDirectory().absolutePath + "/wy_test"
    private fun mkDir(file: File) {
        if (!file.exists()) file.mkdirs()
    }

    fun setSaveTestImagePNG(bitmap: Bitmap): String? {
        var path: String? = null
        try {
            val f = File(testPath)
            mkDir(f)
            path = f.absolutePath + File.separator + "tmp_" + System.currentTimeMillis() + ".png"
            val fo = FileOutputStream(path)
            bitmap.compress(CompressFormat.PNG, 100, fo)
            fo.flush()
            fo.close()
            setMediaScanningToFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }

    fun setSaveTestImageJPG(bitmap: Bitmap): String? {
        var path: String? = null
        try {
            val f = File(testPath)
            mkDir(f)
            path = f.absolutePath + File.separator + "tmp_" + System.currentTimeMillis() + ".jpg"
            val fo = FileOutputStream(path)
            bitmap.compress(CompressFormat.JPEG, 100, fo)
            fo.flush()
            fo.close()
            setMediaScanningToFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }

    fun setSaveImage(bitmap: Bitmap, quality: Int, isPNG: Boolean): File? {
        var path: String? = null
        var format: String? = null
        var compressFormat: CompressFormat? = null
        return try {
            val f =
                File(BaseApplication.context.filesDir.toString() + File.pathSeparator + "shoptrip")
            mkDir(f)
            if (isPNG) {
                format = "png"
                compressFormat = CompressFormat.PNG
            } else {
                format = "jpeg"
                compressFormat = CompressFormat.JPEG
            }
            path =
                f.absolutePath + File.separator + "passport_img" + System.currentTimeMillis() + "." + format
            val fo = FileOutputStream(path)
            bitmap.compress(compressFormat, quality, fo)
            fo.flush()
            fo.close()
            setMediaScanningToFile(path)
            f
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun setSaveImage(bitmap: Bitmap, path: String, fileName: String): Boolean {
        try {
            val f = File(path)
            mkDir(f)
            val fo = FileOutputStream(path + File.separator + fileName)
            bitmap.compress(CompressFormat.JPEG, 50, fo)
            fo.flush()
            fo.close()
            setMediaScanningToFile(path)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun setSaveImageToAlbum(bitmap: Bitmap, fileName: String?, quality: Int): File? {
        return try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                fileName
            )
            val fo = FileOutputStream(file.absolutePath)
            bitmap.compress(CompressFormat.JPEG, quality, fo)
            fo.flush()
            fo.close()
            setMediaScanningToFile(file.absolutePath)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Throws(Exception::class)
    fun setSaveImageToAlbum(bytes: ByteArray?, fileName: String?): String {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            fileName
        )
        var fo: FileOutputStream? = null
        return try {
            fo = FileOutputStream(file.absolutePath)
            fo.write(bytes)
            fo.flush()
            fo.close()
            setMediaScanningToFile(file.absolutePath)
            file.absolutePath
        } finally {
            fo?.close()
        }
    }

    fun removeFile(path: String?): Boolean {
        return try {
            val f = File(path)
            f.deleteOnExit()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Media Scanning
     *
     * @param fullpath
     * @throws Exception
     */
    @Throws(Exception::class)
    fun setMediaScanningToFile(fullpath: String?) {
        ScanListener(BaseApplication.context, fullpath)
    }

    val uUID: String
        get() = UUID.randomUUID().toString()
    val androidID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(
            BaseApplication.context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    val iMEI: String
        @SuppressLint("MissingPermission")
        get() {
            val telephonyManager =
                BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.deviceId
        }
    val iMSI: String
        @SuppressLint("MissingPermission")
        get() {
            val telephonyManager =
                BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.subscriberId
        }
    val macaddress: String
        get() {
            val wifiManager =
                BaseApplication.context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            return wifiInfo.macAddress
        }
    val uDID: String
        @SuppressLint("MissingPermission", "HardwareIds")
        get() {
            val tm =
                BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmDevice: String
            val tmSerial: String
            val androidId: String
            tmDevice = "" + tm.deviceId
            tmSerial = "" + tm.simSerialNumber
            androidId = "" + Settings.Secure.getString(
                BaseApplication.context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val deviceUuid = UUID(
                androidId.hashCode().toLong(),
                tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode()
                    .toLong()
            )
            return deviceUuid.toString()
        }

    /**
     * gms only
     *
     * @param context
     * @return
     */
    fun checkPlayServices(context: Context?): Boolean {

//        try {
//            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
//
//            return resultCode == ConnectionResult.SUCCESS;
//        } catch (Exception e) {
//        }
        return false
    }

    @JvmOverloads
    fun moveStore(packageName: String = BaseApplication.context.packageName) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("market://details?id=$packageName")
        BaseApplication.context.startActivity(intent)
    }

    /**
     * 웹 이동
     *
     * @param url
     */
    fun moveWeb(url: String?) {
        if (url == null) return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse(if (url.startsWith("http://") || url.startsWith("https://")) url else "http://$url")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        BaseApplication.context.startActivity(intent)
    }

    /**
     * scheme 앱이동
     * scheme 등록시 잘 움직일 것이다!
     *
     * @param url
     */
    fun moveApp(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.context.startActivity(intent)
    }

    /**
     * 웹 이동
     *
     * @param context
     * @param url
     */
    fun moveWeb(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse(if (url.startsWith("http://") || url.startsWith("https://")) url else "http://$url")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 앱 실행 및 스토어이동
     *
     * @param packageName
     * @return
     */
    fun setCheckAppAndMoveStore(packageName: String) {
        val startLink =
            BaseApplication.context.packageManager.getLaunchIntentForPackage(packageName)
        if (startLink == null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=$packageName")
            BaseApplication.context.startActivity(intent)
        } else {
            BaseApplication.context.startActivity(startLink)
        }
    }

    /**
     * 앱 실행 및 스토어이동
     *
     * @param context
     * @param packageName
     */
    fun setCheckAppAndMoveStore(context: Context, packageName: String) {
        val startLink = context.packageManager.getLaunchIntentForPackage(packageName)
        if (startLink == null) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("market://details?id=$packageName")
                context.startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                moveWeb(context, "https://play.google.com/store/apps/details?id=$packageName")
            }
        } else {
            context.startActivity(startLink)
        }
    }

    val filterdLanguage: String
        get() {
            val locale = BaseApplication.context.resources.configuration.locale
            return if (locale.language == Locale.KOREAN.language) {
                "ko"
            } else {
                "en"
            }
        }
    val versionName: String?
        get() {
            try {
                val i = BaseApplication.context.packageManager.getPackageInfo(
                    BaseApplication.context.packageName,
                    0
                )
                return i.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }
    val versionCode: Int
        get() {
            try {
                val i = BaseApplication.context.packageManager.getPackageInfo(
                    BaseApplication.context.packageName,
                    0
                )
                return i.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return -1
        }

    fun getInstalledIcon(packageName: String?): Drawable? {
        val manager = BaseApplication.context.packageManager
        var d: Drawable? = null
        try {
            d = manager.getApplicationIcon(packageName!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return d
    }

    fun sendSimpleShareText(context: Context, str: String?, title: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(Intent.EXTRA_TEXT, str)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.type = "text/plan"
        context.startActivity(Intent.createChooser(intent, title))
    }

    @SuppressLint("MissingPermission")
    fun isCheckNetworkState(context: Context): Boolean {
        var result = true
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        // boolean isWifiAvail = ni.isAvailable();
        val isWifiConn = ni!!.isConnected
        var isMobileConn = false
        try {
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            // boolean isMobileAvail = ni.isAvailable();
            isMobileConn = ni!!.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var isAirplaneMode = false
        try {
            isAirplaneMode = Settings.System.getInt(
                context.contentResolver,
                Settings.System.AIRPLANE_MODE_ON,
                0
            ) != 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isWifiConn == false && isMobileConn == false || isAirplaneMode) {
            result = false
        }
        return result
    }

    fun firstUpperString(data: String): String {
        var trans = data.substring(0, 1)
        trans = trans.toUpperCase()
        trans += data.substring(1)
        return trans
    }

    /**
     * 숫자 3자리씩 끊기
     *
     * @param str
     * @return
     */
    fun makeStringComma(str: String): String {
        if (str.length == 0) return ""
        val value = str.toLong()
        val format = DecimalFormat("###,###")
        return format.format(value)
    }

    val isCheckNetworkState: Boolean
        get() = isCheckNetworkState(BaseApplication.context)

    fun getString(res: Int, vararg obj: Any?): String {
        return BaseApplication.context.resources.getString(res, *obj)
    }

    fun getString(res: Int): String {
        return BaseApplication.context.resources.getString(res)
    }

    fun getStringArray(res: Int): Array<String> {
        return BaseApplication.context.resources.getStringArray(res)
    }

    val resources: Resources
        get() = BaseApplication.context.resources

    fun getColor(resId: Int): Int {
        try {
            return BaseApplication.context.resources.getColor(resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    val randomColor: Int
        get() {
            val r = Random().nextInt(255)
            val g = Random().nextInt(255)
            val b = Random().nextInt(255)
            return Color.rgb(r, g, b)
        }

    fun getDrawable(resId: Int): Drawable? {
        return BaseApplication.context.getDrawable(resId)
    }

    fun decodeUrlString(str: String): String {
        try {
            return URLDecoder.decode(str, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }

    /**
     * 이메일 정규식
     */
    private val VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    /**
     * 동적 자릿수
     */
    fun roundToDecimal(value: Double, fractionDigits: Int): String {
        val multiplier = Math.pow(10.0, fractionDigits.toDouble())
        return BigDecimal.valueOf(Math.round(value * multiplier)).divide(BigDecimal(multiplier))
            .toPlainString()
    }

    /**
     * 이메일 체크
     *
     * @param emailStr
     * @return
     */
    fun validateEmail(emailStr: String?): Boolean {
        val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }

    fun startOfYear(dayCount: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.MONTH] = 0
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal.add(Calendar.DAY_OF_YEAR, dayCount)
        return cal.time
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    /**
     * Media Scanning for 'kitkat'
     *
     * @author SU
     */
    internal class ScanListener(context: Context?, private val path: String?) :
        MediaScannerConnectionClient {
        private val mMs: MediaScannerConnection
        override fun onMediaScannerConnected() {
            mMs.scanFile(path, null)
        }

        override fun onScanCompleted(path: String, uri: Uri) {
            mMs.disconnect()
        }

        init {
            mMs = MediaScannerConnection(context, this)
            mMs.connect()
        }
    }

    interface ADIDCallback {
        fun getId(id: String?)
    }

    init {
        density = BaseApplication.context.resources.displayMetrics.density
        checkSize()
    }
}