package com.pira.ccloud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pira.ccloud.R
import com.pira.ccloud.VideoPlayerActivity
import com.pira.ccloud.data.model.PlaybackProgress
import com.pira.ccloud.utils.StorageUtils

@Composable
fun ContinueWatchingScreen(navController: NavController) {
    val context = LocalContext.current
    var entries by remember { mutableStateOf(emptyList<PlaybackProgress>()) }

    LaunchedEffect(Unit) {
        entries = StorageUtils.loadAllPlaybackProgress(context)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.continue_watching),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("هنوز ویدیویی برای ادامه تماشا ذخیره نشده است.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(entries, key = { it.mediaKey }) { entry ->
                    val percent = if (entry.durationMs > 0L)
                        ((entry.positionMs * 100L) / entry.durationMs).toInt().coerceIn(0, 100)
                    else 0
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            VideoPlayerActivity.startFromProgress(context, entry)
                        }.focusable(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = entry.posterUrl,
                                contentDescription = entry.title,
                                modifier = Modifier.size(width = 132.dp, height = 78.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.DarkGray)
                            )
                            Column(
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text("$percent٪ تماشا شده", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(7.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(5.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(percent / 100f).height(5.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                            Icon(Icons.Default.PlayArrow, contentDescription = "ادامه پخش", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                            IconButton(onClick = {
                                StorageUtils.removePlaybackProgress(context, entry.mediaKey)
                                entries = entries.filterNot { it.mediaKey == entry.mediaKey }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "حذف از لیست")
                            }
                        }
                    }
                }
            }
        }
    }
}
