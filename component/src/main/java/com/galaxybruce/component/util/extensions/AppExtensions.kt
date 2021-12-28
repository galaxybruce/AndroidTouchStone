package com.galaxybruce.component.util.extensions

import android.app.*
import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.Intent.*
import android.content.res.Configuration
import android.graphics.*
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.galaxybruce.component.util.ToastUtils
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)
/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}
/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}
/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 * @author Antonio Leiva
 * @see <a href="https://antonioleiva.com/kotlin-ongloballayoutlistener/>Kotlin recipes: OnGlobalLayoutListener</a>
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}
/**
 * Extension method to get ClickableSpan.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */
fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int,
                                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            if (onLayout(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}
/**
 * Extension method to provide quicker access to the [LayoutInflater] from a [View].
 */
fun View.getLayoutInflater() = context.getLayoutInflater()
/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}
/**
 * Extension method to update padding of view.
 *
 */
fun View.updatePadding(paddingStart: Int = getPaddingStart(),
                       paddingTop: Int = getPaddingTop(),
                       paddingEnd: Int = getPaddingEnd(),
                       paddingBottom: Int = getPaddingBottom()) {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

/**
 * Extension method to set View's left padding.
 */
fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

/**
 * Extension method to set View's right padding.
 */
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's top padding.
 */
fun View.setPaddingTop(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

/**
 * Extension method to set View's bottom padding.
 */
fun View.setPaddingBottom(value: Int) = setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)

/**
 * Extension method to set View's start padding.
 */
fun View.setPaddingStart(value: Int) = setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

/**
 * Extension method to set View's end padding.
 */
fun View.setPaddingEnd(value: Int) = setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's horizontal padding.
 */
fun View.setPaddingHorizontal(value: Int) = setPaddingRelative(value, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's vertical padding.
 */
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)

/**
 * Extension method to set View's height.
 */
fun View.setHeight(value: Int): View {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
    return this
}

/**
 * Extension method to set View's width.
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * Extension method to resize View with height & width.
 */
fun View.resize(width: Int, height: Int): View {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
    return this
}

/**
 * Extension method to access the view's children as a list
 */
val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }


/**
 * Set an onclick listener
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

/**
 * Extension method to set OnClickListener on a view.
 */
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }


fun <T: View> T.ifElse(condition: (T) -> Boolean, result: (T) -> Unit, block: (T) -> Unit): T {
    if (condition(this)) result(this) else block(this)
    return this
}

fun <T: View> T.setSelected(condition: () -> Boolean): T {
    isSelected = condition()
    return this
}

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.showDelayed(delayMillis: Long = 300) {
    postDelayed({
        show()
    },delayMillis)
}

/**
 * Show the view if [condition] returns true
 * (visibility = View.VISIBLE)
 */
inline fun View.showIf(condition: () -> Boolean) : View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}

inline fun View.inVisibilityIf(condition: () -> Boolean) : View {
    if (visibility != View.INVISIBLE && condition()) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * 拦截指定 keyCode 事件
 */
inline fun View.setOnKeyListener(keyCodeList: List<Int> = arrayListOf(), crossinline condition: () -> Unit) : View {
    setOnKeyListener { _, keyCode, event ->
        if (keyCode in keyCodeList) {
            if (event.action == KeyEvent.ACTION_UP) {
                condition()
            }
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
    return this
}

/**
 * 拦截指定 actionId 事件
 */
inline fun EditText.setOnEditorActionListener(actionIdList: List<Int> = arrayListOf(),
                                              crossinline condition: () -> Unit) : View {
    setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId in actionIdList) {
            //关闭软键盘
            condition()
            return@OnEditorActionListener true
        }
        false
    })

    return this
}

/**
 * 拦截 确认实体键
 */
inline fun View.setOnKeyInterceptEnterListener(crossinline condition: () -> Unit) : View {
    setOnKeyListener(arrayListOf(KeyEvent.KEYCODE_ENTER), condition)
    if (this is EditText) {
        setOnEditorActionListener(arrayListOf(EditorInfo.IME_ACTION_DONE), condition)
    }
    return this
}



/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide() : View {
    remove()
    return this
}

/**
 * Hide the view if [predicate] returns true
 * (visibility = View.INVISIBLE)
 */
inline fun View.hideIf(predicate: () -> Boolean) : View {
    removeIf(predicate)
    return this
}

inline fun View.visibility(condition: () -> Boolean): View? {
    return if (condition()) show() else hide()
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.remove() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Remove the view if [predicate] returns true
 * (visibility = View.GONE)
 */
inline fun View.removeIf(predicate: () -> Boolean) : View {
    if (visibility != View.GONE && predicate()) {
        visibility = View.GONE
    }
    return this
}
/**
 * Toggle a view's visibility
 */
fun View.toggleVisibility() : View {
    if (visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    } else {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Extension method to get a view as bitmap.
 */
fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}

/**
 * Find a parent of type [parentType], assuming it exists
 */
tailrec fun <T : View> View.findParent(parentType: Class<T>): T {
    return if (parent.javaClass == parentType) parent as T else (parent as View).findParent(parentType)
}
/**
 * Like findViewById but with type interference, assume the view exists
 */
inline fun <reified T : View> View.find(@IdRes id: Int) : T = findViewById(id) as T
/**
 *  Like findViewById but with type interference, or null if not found
 */
inline fun <reified T : View> View.findOptional(@IdRes id: Int) : T? = findViewById(id) as? T
/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)
/**
 * Extension method to find a device width in pixels
 */
inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels
/**
 * Extension method to find a device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels
/**
 * Extension method to get displayMetrics in Context.displayMetricks
 */
inline val Context.displayMetricks: DisplayMetrics
    get() = resources.displayMetrics
/**
 * Extension method to get LayoutInflater
 */
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)
/**
 * Extension method to get a new Intent for an Activity class
 */
inline fun <reified T : Any> Context.intent() = Intent(this, T::class.java)
/**
 * Create an intent for [T] and apply a lambda on it
 */
inline fun <reified T : Any> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}
/**
 * Extension method to startActivity for Context.
 */
inline fun <reified T : Activity> Context?.startActivity() = this?.startActivity(Intent(this, T::class.java))
/**
 * Extension method to start Service for Context.
 */
inline fun <reified T : Service> Context?.startService() = this?.startService(Intent(this, T::class.java))
/**
 * Extension method to startActivity with Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(enterResId: Int = 0, exitResId: Int = 0) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}
/**
 * Extension method to startActivity with Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(enterResId: Int = 0, exitResId: Int = 0, intentBody: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.intentBody()
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}
/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) = this?.let { ToastUtils.showToast(it, text.toString()) }
/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) = this?.let { Toast.makeText(it, textId, duration).show() }
/**
 * Extension method to Get Integer resource for Context.
 */
fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)
/**
 * Extension method to Get Boolean resource for Context.
 */
fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)
/**
 * Extension method to Get Color for resource for Context.
 */
fun Context.getColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)
/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)
/**
 * InflateLayout
 */
fun Context.inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)
/**
 * Extension method to get inputManager for Context.
 */
inline val Context.inputManager: InputMethodManager?
    get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
/**
 * Extension method to get notificationManager for Context.
 */
inline val Context.notificationManager: NotificationManager?
    get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
/**
 * Extension method to get keyguardManager for Context.
 */
inline val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager
/**
 * Extension method to get telephonyManager for Context.
 */
inline val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(TELEPHONY_SERVICE) as? TelephonyManager
/**
 * Extension method to get devicePolicyManager for Context.
 */
inline val Context.devicePolicyManager: DevicePolicyManager?
    get() = getSystemService(DEVICE_POLICY_SERVICE) as? DevicePolicyManager
/**
 * Extension method to get connectivityManager for Context.
 */
inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
/**
 * Extension method to get alarmManager for Context.
 */
inline val Context.alarmManager: AlarmManager?
    get() = getSystemService(ALARM_SERVICE) as? AlarmManager
/**
 * Extension method to get clipboardManager for Context.
 */
inline val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
/**
 * Extension method to show notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(this)
    builder.body()
    return builder.build()
}
/**
 * Extension method to share for Context.
 */
fun Context.share(text: String, subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    try {
        startActivity(createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        return false
    }
}
/**
 * Extension method to provide quicker access to the [LayoutInflater] from [Context].
 */
fun Context.getLayoutInflater() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Extension method to send sms for Context.
 */
fun Context.sms(phone: String?, body: String = "") {
    val smsToUri = Uri.parse("smsto:" + phone)
    val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
    intent.putExtra("sms_body", body)
    startActivity(intent)
}

/**
 * Extension method to dail telephone number for Context.
 */
fun Context.dial(tel: String?) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)))
/**
 * Extension method to get theme for Context.
 */
fun Context.isDarkTheme(): Boolean
        = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
/**
 * Extension method to display toast text for Fragment.
 */
fun Fragment?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) = this?.let { activity.toast(text, duration) }
/**
 * Extension method to display toast text for Fragment.
 */
fun Fragment?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) = this?.let { activity.toast(textId, duration) }
/**
 * Extension method to display notification text for Fragment.
 */
inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit) = activity.notification(body)
/**
 * Extension method to share text for Fragment.
 */
fun Fragment.share(text: String, subject: String = "") = activity.share(text, subject)
/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}
/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}
/**
 * Extension method to get ContentView for ViewGroup.
 */
fun Activity.getContentView(): ViewGroup {
    return this.findViewById(android.R.id.content) as ViewGroup
}
/**
 * Extension method to inflate layout for ViewGroup.
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
/**
 * Extension method to get views by tag for ViewGroup.
 */
fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}
/**
 * Extension method to remove views by tag ViewGroup.
 */
fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}

/**
 * Extension method underLine for TextView.
 */
fun TextView.underLine() : TextView{
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
    return this
}

/**
 * Extension method deleteLine for TextView.
 */
fun TextView.deleteLine() : TextView{
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
    return this
}

/**
 * Extension method bold for TextView.
 */
fun TextView.bold() : TextView{
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
    return this
}

fun TextView.textNull(str: String?, default: String? = ""): TextView {
    text = if (!TextUtils.isEmpty(str)) str else default
    return this
}

/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int): TextView {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    } catch (e: Exception) {
    }
    return this
}
/**
 * Extension method to set font for TextView.
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}
/**
 * Extension method to set a drawable to the left of a TextView.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * Extension method to get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Extension method to get sha1 string.
 */
fun String.sha1() = encrypt(this, "SHA-1")

/**
 * Extension method to check if String is Phone Number.
 */
fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Email.
 */
fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

/**
 * Extension method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Extension method to convert byteArray to String.
 */
private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}
/**
 * Extension method to cast a char with a decimal value to an [Int].
 */
fun Char.decimalValue(): Int {
    if (!isDigit())
        throw IllegalArgumentException("Out of range")
    return this.toInt() - '0'.toInt()
}
/**
 * Extension method to simplify the code needed to apply spans on a specific sub string.
 */
inline fun SpannableStringBuilder.withSpan(vararg spans: Any, action: SpannableStringBuilder.() -> Unit):
        SpannableStringBuilder {
    val from = length
    action()

    for (span in spans) {
        setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}
/**
 * Extension method to int time to 2 digit String
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()
/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue].
 */
fun Editable.replaceAll(newValue: String) {
    replace(0, length, newValue)
}
/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue] while
 * ignoring any [android.text.InputFilter] set on the [Editable].
 */
fun Editable.replaceAllIgnoreFilters(newValue: String) {
    val currentFilters = filters
    filters = emptyArray()
    replaceAll(newValue)
    filters = currentFilters
}
/**
 * Extension method to get Date for String with specified format.
 */
fun String.dateInFormat(format: String): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: Date? = null
    try {
        parsedDate = dateFormat.parse(this)
    } catch (ignored: ParseException) {
        ignored.printStackTrace()
    }
    return parsedDate
}
/**
 * Extension method to get ClickableSpan.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */
fun getClickableSpan(color: Int, action: (view: View) -> Unit): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(view: View) {
            action(view)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = color
        }
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

/**
 * Extension method to get value from EditText.
 */
val EditText.value
    get() = text.toString()

/**
 * Extension method to check is aboveApi.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Extension method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}


/**
 * Extension method to resize Bitmap to specified height and width.
 */
fun Bitmap.resize(w: Number, h: Number): Bitmap {
    val width = width
    val height = height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    return this
}

/**
 * Extension method check if is Main Thread.
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/**
 * Extension method to get SpannableString with specific size text from start to end.
 */
fun spannableSize(text: String, textSize: Int, isDip: Boolean, start: Int, end: Int): SpannableString {
    val sp = SpannableString(text)
    sp.setSpan(AbsoluteSizeSpan(textSize, isDip), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    return sp
}

/**
 * Extension method to get SpannableString with Bold text from start to end.
 */
fun spannableBold(text: String, start: Int, end: Int): SpannableString {
    val sp = SpannableString(text)
    sp.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    return sp
}

/**
 * Extension method to provide handler and mainThread.
 */
private object ContextHandler {
    val handler = Handler(Looper.getMainLooper())
    val mainThread = Looper.getMainLooper().thread
}
/**
 * Extension method to run block of code on UI Thread.
 */
fun runOnUiThread(action: () -> Unit){
    if (ContextHandler.mainThread == Thread.currentThread()) action() else ContextHandler.handler.post { action() }
}
/**
 * Extension method to run block of code after specific Delay.
 */
fun runDelayed(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    Handler().postDelayed(action, timeUnit.toMillis(delay))
}
/**
 * Extension method to run block of code on UI Thread after specific Delay.
 */
fun runDelayedOnUiThread(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    ContextHandler.handler.postDelayed(action, timeUnit.toMillis(delay))
}
/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName

/**
 * map 添加数据 过滤value 为null的数据
 */
fun <K, V, T : MutableMap<K, V>> T.putIfNotNull(key: K, value: V): T {
    value?.let { this.put(key, value) }
    return this
}

/**
 *  生成 自动过滤value为null的 map
 */
fun <K, V> mutableMapIfNotNull(vararg pairs: Pair<K, V?>): MutableMap<K, V?> {
    return mutableMapOf<K, V?>().apply { putAll(pairs.filter { it.second != null }) }
}

/**
 *  生成 自动过滤 指定条件 的 map
 */
fun <K, V> mutableMapByFilter(vararg pairs: Pair<K, V?>, block: (K, V?) -> Boolean): MutableMap<K, V?> {
    return mutableMapOf<K, V?>().apply { putAll(pairs.filter { block(it.first, it.second) }) }
}

/**
 * list 添加数据 过滤value 为null的数据
 */
fun <V, T : MutableList<V>> T.addIfNotNull(value: V): T {
    value?.let { this.add(value) }
    return this
}

/**
 *  生成 自动过滤value为null的 list
 */
fun <T> mutableListIfNotNull(vararg elements: T?): MutableList<T> {
    return mutableListOf<T>().apply { elements.filter { it != null }.forEach { this.add(it!!) } }
}

/**
 *  生成 自动过滤 指定条件 的 list
 */
fun <T> mutableListByFilter(vararg elements: T?, block: (T?) -> Boolean): MutableList<T> {
    return mutableListOf<T>().apply { elements.filter { block(it) }.forEach { this.add(it!!) } }
}
