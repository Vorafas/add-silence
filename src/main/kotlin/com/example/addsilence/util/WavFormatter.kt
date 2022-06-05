package com.example.addsilence.util

import java.io.File
import java.io.OutputStream
import java.io.RandomAccessFile
import kotlin.experimental.and

object WavFormatter {

    fun writeHeader(out: OutputStream, numChannels: Short, sampleRate: Int, bitsPerSample: Short) {
        // Chunk id
        out.write("RIFF".encodeToByteArray())
        // Chunk data size
        out.write(ByteArray(4))
        // Format
        out.write("WAVE".encodeToByteArray())
        // Subchunk 1 id
        out.write("fmt ".encodeToByteArray())
        // Subchunk 1 size (length of pcm format declaration area)
        out.write(16.toByteArray())
        // Audio format (1 = PCM)
        out.write(1.toShort().toByteArray())
        // Number of channels
        out.write(numChannels.toByteArray())
        // Sample Rate
        out.write(sampleRate.toByteArray())
        // Byte rate
        out.write((sampleRate * numChannels * (bitsPerSample / 8)).toByteArray())
        // Block align
        out.write((numChannels * (bitsPerSample / 8)).toShort().toByteArray())
        // Bits per sample
        out.write(bitsPerSample.toByteArray())
        // Subchunk 1 id (data section label)
        out.write("data".encodeToByteArray())
        // Subchunk 1 size (length of raw pcm data in bytes)
        out.write(ByteArray(4))
    }

    fun updateHeader(wav: File) {
        val length = wav.length().toInt()
        val sizes = (length - 8).toByteArray() + (length - 44).toByteArray()

        RandomAccessFile(wav, "rw").use { accessWave ->
            accessWave.seek(4)
            accessWave.write(sizes, 0, 4)
            accessWave.seek(40)
            accessWave.write(sizes, 4, 4)
        }
    }
}

private fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this shr 0).toByte(),
        (this shr 8).toByte(),
        (this shr 16).toByte(),
        (this shr 24).toByte()
    )
}

private fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (this and 255).toByte(),
        ((this and -256).toInt() shr 8).toByte()
    )
}