package com.automixx

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.DisposableEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MusicPlayerScreen()
            }
        }
    }
}

@Composable
fun MusicPlayerScreen() {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    var selectedSong by remember { mutableStateOf("No song selected") }
    var playing by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedSong = it.lastPathSegment ?: "Selected audio"
            player.setMediaItem(MediaItem.fromUri(it))
            player.prepare()
            player.play()
            playing = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "AutoMixX",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(30.dp))

            Text("Now Playing")

            Text(selectedSong)

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                picker.launch(arrayOf("audio/*"))
            }) {
                Text("Choose Music")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (player.isPlaying) {
                        player.pause()
                        playing = false
                    } else {
                        player.play()
                        playing = true
                    }
                }
            ) {
                Text(if (playing) "Pause" else "Play")
            }

            Spacer(Modifier.height(30.dp))

            Text("AutoMix Engine: Coming next")
        }
    }
}
