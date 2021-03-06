package com.stylingandroid.midipad.ui

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.stylingandroid.midipad.R
import com.stylingandroid.midipad.midi.MidiController

class MidiPad @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var midiController: MidiController? = null

    init {
        View.inflate(context, R.layout.midi_pad, this)
    }

    override fun onFinishInflate() =
            super.onFinishInflate().run {
                filter { it is PadView }.forEachIndexed { index, view ->
                    view.setOnTouchListener { _, motionEvent ->
                        touch(NOTES[index], motionEvent)
                    }
                }
            }

    private fun filter(predicate: (View) -> Boolean): List<View> =
            ArrayList<View>().apply {
                for (index in 0 until childCount) {
                    getChildAt(index)?.takeIf(predicate)?.also {
                        add(it)
                    }
                }
            }

    private fun touch(note: Int, motionEvent: MotionEvent): Boolean =
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    midiController?.noteOn(note, motionEvent.pressure)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    midiController?.noteOff(note, motionEvent.pressure)
                    false
                }
                else -> false
            }

    companion object {
        private const val START_NOTE = 44
        private const val END_NOTE = 55
        private val NOTES = (START_NOTE..END_NOTE).toList()
    }
}
