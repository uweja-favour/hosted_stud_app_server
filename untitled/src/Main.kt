import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    // Hardcoded video list
    val videoList = listOf(
//        "https://youtu.be/Su_jxP_8obw?si=619tlUfeLPMpvVEM",
        "https://youtu.be/a-RAltgH8tw?si=AEk7mexqtHO1lev6",
        "https://youtu.be/M8YtV47kaqA?si=fT_e1AuXYSd-ZGF3",
        "https://youtu.be/ZX8VsqNO_Ss?si=u19NY2qsbkQ7kq_K",
        "https://youtu.be/sk3svS_fzZM?si=-JOqEtaUGp7caM-9",
        "https://youtu.be/a3agLJQ6vt8?si=OBixWAD3YpzjPdeW",
        "https://youtu.be/za-EEkqJLCQ?si=1BxA9PYgVqEZYENA",
        "https://youtu.be/rk6aKkWqqcI?si=bH0KmUD6KjwChuf8",
        "https://youtu.be/JyBq76N4Zc4?si=qBllRZpxqTW5im-y"
        // Add more links here
    )

    if (videoList.isEmpty()) {
        println("⚠️ No videos found in videoList. Exiting.")
        return
    }

    // --- Step 1: Ask download type (mp3 or mp4)
    println("Choose download type:")
    println("1. Audio only (MP3)")
    println("2. Full video (MP4)")
    print("Your choice (1 or 2): ")
    val typeChoice = readlnOrNull()?.trim()

    val downloadAsAudio = when (typeChoice) {
        "1" -> true
        "2" -> false
        else -> {
            println("Invalid choice. Defaulting to MP3 audio only.")
            true
        }
    }

    // --- Step 2: Ask video quality if MP4
    val videoFormat = if (!downloadAsAudio) {
        println("Choose video quality:")
        println("1. 480p")
        println("2. 720p")
        println("3. 1080p")
        print("Your choice (1, 2, or 3): ")
        when (readlnOrNull()?.trim()) {
            "1" -> "bestvideo[height<=480]+bestaudio/best[height<=480]"
            "2" -> "bestvideo[height<=720]+bestaudio/best[height<=720]"
            "3" -> "bestvideo[height<=1080]+bestaudio/best[height<=1080]"
            else -> {
                println("Invalid choice. Defaulting to 480p.")
                "bestvideo[height<=480]+bestaudio/best[height<=480]"
            }
        }
    } else null

    var count = 0
    val mutex = Mutex()

    fun downloadYoutubeVideo(videoLink: String, index: Int): Int {
        val command = if (downloadAsAudio) {
            listOf(
                "yt-dlp",
                "-x", "--audio-format", "mp3",
                "-o", "$index - %(title)s.%(ext)s",
                videoLink
            )
        } else {
            listOf(
                "yt-dlp",
                "-f", videoFormat ?: "best",
                "-o", "$index - %(title)s.%(ext)s",
                "--merge-output-format", "mp4",
                videoLink
            )
        }

        return try {
            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }

            val exitCode = process.waitFor()
            if (exitCode == 0) {
                println("✅ Download completed successfully for video #$index")
            } else {
                println("❌ Download failed for video #$index (exit code $exitCode)")
            }
            exitCode
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            -1
        }
    }

    fun handleResult(result: Int, startTime: Long, totalVideos: Int) {
        if (result == 0) {
            count++
            println("Downloaded $count/$totalVideos video(s) successfully.")
        }
        if (count == totalVideos) {
            val timeSpent = System.currentTimeMillis() - startTime
            val hours = (timeSpent / 1000) / 3600
            val minutes = ((timeSpent / 1000) % 3600) / 60
            val seconds = (timeSpent / 1000) % 60
            println("⏳ Total time spent: $hours hr(s) $minutes min(s) $seconds sec(s)")
        }
    }

    fun startDownloads() = runBlocking(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        var coroutineCount = 1
        for ((index, video) in videoList.withIndex()) {
            launch {
                println("🚀 Launching coroutine #${coroutineCount++} for video #${index + 1}")
                val result = downloadYoutubeVideo(video, index + 1)
                mutex.withLock {
                    handleResult(result, startTime, videoList.size)
                    if (result != 0) {
                        println("⚠️ Cancelling further downloads due to failure.")
                        return@launch
                    }
                }
            }
        }
    }

    // Start
    startDownloads()
}
