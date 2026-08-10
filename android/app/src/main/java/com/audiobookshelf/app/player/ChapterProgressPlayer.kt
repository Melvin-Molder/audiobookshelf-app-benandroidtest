package com.audiobookshelf.app.player

import com.audiobookshelf.app.data.BookChapter
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.Player

class ChapterProgressPlayer(
  player: Player,
  private val chapterProvider: () -> BookChapter?,
  private val absolutePositionProvider: () -> Long,
  private val absoluteBufferedPositionProvider: () -> Long,
  private val totalDurationProvider: () -> Long
) : ForwardingPlayer(player) {
  override fun getCurrentPosition(): Long {
    val chapter = chapterProvider() ?: return absolutePositionProvider()
    return (absolutePositionProvider() - chapter.startMs).coerceIn(0L, chapter.durationMs)
  }

  override fun getBufferedPosition(): Long {
    val chapter = chapterProvider() ?: return absoluteBufferedPositionProvider()
    return (absoluteBufferedPositionProvider() - chapter.startMs).coerceIn(0L, chapter.durationMs)
  }

  override fun getDuration(): Long {
    return chapterProvider()?.durationMs ?: totalDurationProvider()
  }
}

private val BookChapter.durationMs: Long
  get() = (endMs - startMs).coerceAtLeast(0L)
