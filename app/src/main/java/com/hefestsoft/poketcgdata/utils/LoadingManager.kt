package com.hefestsoft.poketcgdata.utils

import android.view.View
import android.widget.TextView
import androidx.activity.result.launch
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Manages the display of a rotating list of loading messages in a TextView.
 *
 * @param lifecycleScope The LifecycleCoroutineScope to launch the coroutine.
 * @param loadingTextView The TextView where the loading messages will be displayed.
 * @param loadingContainer The container view that determines if the loading is active.
 */
class LoadingManager(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val loadingTextView: TextView,
    private val loadingContainer: View
) {

    private var loadingJob: Job? = null
    private var loadingIndex = 0

    /**
     * Starts displaying the loading messages.
     * The messages will cycle as long as the loadingContainer is visible.
     *
     * @param messages The list of messages to display.
     */
    fun startLoading(messages: List<String>) {
        // Cancel any previous loading job to avoid multiple loops
        loadingJob?.cancel()

        // Start a new loading job
        loadingJob = lifecycleScope.launch {
            while (loadingContainer.isVisible) {
                loadingTextView.text = messages[loadingIndex % messages.size]
                loadingIndex++
                delay(1600)
            }
        }
    }

    /**
     * Stops the loading message animation.
     */
    fun stopLoading() {
        loadingJob?.cancel()
        loadingIndex = 0
    }
}