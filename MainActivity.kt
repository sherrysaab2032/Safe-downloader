package com.example.safedownloader

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var sharedUrl = ""
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        }

        setContent {
            MaterialTheme {
                DownloaderScreen(this, sharedUrl)
            }
        }
    }
}

@Composable
fun DownloaderScreen(context: Context, initialUrl: String) {
    var url by remember { mutableStateOf(initialUrl) }
    var message by remember { mutableStateOf("Paste a direct, publicly downloadable media URL.") }

    fun download() {
        val value = url.trim()
        val parsed = Uri.parse(value)
        if (!value.startsWith("https://") && !value.startsWith("http://")) {
            message = "Please enter a valid http/https URL."
            return
        }
        if (parsed.host.isNullOrBlank()) {
            message = "The URL does not look valid."
            return
        }

        val request = DownloadManager.Request(parsed)
            .setTitle(URLUtil.guessFileName(value, null, null))
            .setDescription("Safe Downloader")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(value, null, null)
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        message = "Download started. Check your Downloads folder."
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Safe Downloader") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Download media you are authorized to download.",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Media URL") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = { download() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download")
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    message,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                "This first version downloads direct HTTP/HTTPS files. " +
                "It does not bypass DRM, private access, paywalls, or platform restrictions.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
