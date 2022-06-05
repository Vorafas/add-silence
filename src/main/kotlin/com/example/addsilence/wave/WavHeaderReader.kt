package com.example.addsilence.wave

import java.io.IOException
import java.io.InputStream

object WavHeaderReader {

    private const val HEADER_SIZE = 44

    fun read(input: InputStream): WavHeader {
        val buffer = ByteArray(HEADER_SIZE)
        val numberRead = input.read(buffer)
        if (numberRead != HEADER_SIZE) {
            throw IOException("Could not read header")
        }

        val chunkId = buffer.copyOfRange(0, 4)
        if (chunkId.decodeToString() != "RIFF") {
            throw IOException("Illegal format")
        }

        return WavHeader(
            chunkId = chunkId,
            chunkSize = buffer.toInt(4),
            format = buffer.copyOfRange(8, 12),
            subchunk1Id = buffer.copyOfRange(12, 16),
            subchunk1Size = buffer.toInt(16),
            audioFormat = buffer.toShort(20),
            numChannels = buffer.toShort(22),
            sampleRate = buffer.toInt(24),
            byteRate = buffer.toInt(28),
            blockAlign = buffer.toShort(32),
            bitsPerSample = buffer.toShort(34),
            subchunk2Id = buffer.copyOfRange(36, 40),
            subchunk2Size = buffer.toInt(40)
        )
    }
}

private fun ByteArray.toInt(start: Int): Int {
    return ((this[start + 3].toInt() and 255) shl 8) +
            ((this[start + 2].toInt() and 255) shl 8) +
            ((this[start + 1].toInt() and 255) shl 8) +
            ((this[start].toInt() and 255))
}

private fun ByteArray.toShort(start: Int): Short {
    return (((this[start + 1].toInt() and 255) shl 8) + (this[start].toInt() and 255)).toShort()
}