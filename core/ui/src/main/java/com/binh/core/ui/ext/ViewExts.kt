package com.binh.core.ui.ext

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.RippleDrawable
import android.os.SystemClock
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.EditText
import android.widget.SearchView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.binh.core.ui.util.Event
import com.binh.core.ui.util.collectEvent
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


fun View.updateWindowInsetsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
        // Apply the insets as padding to the view. Here we're setting all of the
        // dimensions, but apply as appropriate to your layout. You could also
        // update the views margin if more appropriate.
        view.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

        // Return CONSUMED if we don't want the window insets to keep being passed
        // down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}

fun View.updateWindowInsetsVerticalPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
        // Apply the insets as padding to the view. Here we're setting all of the
        // dimensions, but apply as appropriate to your layout. You could also
        // update the views margin if more appropriate.
        view.updatePadding(top = insets.top, bottom = insets.bottom)

        // Return CONSUMED if we don't want the window insets to keep being passed
        // down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}

fun View.updateWindowInsetsHorizontalPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
        // Apply the insets as padding to the view. Here we're setting all of the
        // dimensions, but apply as appropriate to your layout. You could also
        // update the views margin if more appropriate.
        view.updatePadding(left = insets.top, right = insets.bottom)

        // Return CONSUMED if we don't want the window insets to keep being passed
        // down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun DatePicker.getDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    return calendar.time
}

fun CheckBox.removeRippleEffect() {
    var drawable = background
    if (drawable is RippleDrawable) {
        drawable = drawable.findDrawableByLayerId(0)
        background = drawable
    }
}

fun RecyclerView.removeAllItemDecoration() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = textPaint.linkColor
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
                // Prevent CheckBox state from being toggled when link is clicked
                view.cancelPendingInputEvents()
                // Do action for link text...
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan,
            startIndexOfLink,
            startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}


fun View.setSafeOnClickListener(interval: Long = 500L, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(interval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Long = 500L,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        Log.d("View.OnClick", (SystemClock.elapsedRealtime() - lastTimeClicked).toString())
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun CompoundButton.setSafeCheckChangeListener(
    interval: Long = 500L,
    onSafeCheck: (buttonView: CompoundButton, isChecked: Boolean) -> Unit
) {
    val safeCheckChangeListener = SafeCheckChangeListener(interval, onSafeCheck)
    setOnCheckedChangeListener(safeCheckChangeListener)
}

class SafeCheckChangeListener(
    private val defaultInterval: Long = 500L,
    private val onSafeCheck: (buttonView: CompoundButton, isChecked: Boolean) -> Unit
) : CompoundButton.OnCheckedChangeListener {
    private var lastTimeChecked: Long = 0
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (SystemClock.elapsedRealtime() - lastTimeChecked < defaultInterval) {
            buttonView.isChecked = !isChecked
            return
        }
        Log.d(
            "CompoundButton.CheckedChange",
            (SystemClock.elapsedRealtime() - lastTimeChecked).toString()
        )
        lastTimeChecked = SystemClock.elapsedRealtime()
        onSafeCheck(buttonView, isChecked)
    }

}

fun EditText.onDone(callback: () -> Unit) {
    // These lines optional if you don't want to set in Xml
    imeOptions = EditorInfo.IME_ACTION_DONE
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        } else {
            false
        }

    }
}

fun SeekBar.handleStopTrackingListener(callback: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seek: SeekBar, progress: Int, fromUser: Boolean
        ) {
        }

        override fun onStartTrackingTouch(seek: SeekBar) {}

        override fun onStopTrackingTouch(seek: SeekBar) {
            callback.invoke(seek.progress)
        }
    })
}

fun SearchView.onQuerySubmit(callback: (String?) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            callback.invoke(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }

    })
}


fun View.showToast(
    lifecycleCoroutineScope: LifecycleCoroutineScope,
    event: StateFlow<Event<Any>>,
    timeLength: Int
) {

    lifecycleCoroutineScope.launch(SupervisorJob()) {
        event.collectEvent { content ->
            when (content) {
                is String -> Toast.makeText(context, content, timeLength).show()
                is Int -> Toast.makeText(
                    context, resources.getStringOrNull(content) ?: "", timeLength
                ).show()

                else -> {
                }
            }

        }
    }
}

inline fun View.onSizeChange(crossinline runnable: () -> Unit) =
    addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        val rect = Rect(left, top, right, bottom)
        val oldRect = Rect(oldLeft, oldTop, oldRight, oldBottom)
        if (rect.width() != oldRect.width() || rect.height() != oldRect.height()) {
            post { runnable() }
        }
    }

fun View.setBackgroundTint(color: Int) {
    background?.mutate()?.setTint(color)
}

