package com.example.addsilence.wave

class WavHeader(
    val chunkId: ByteArray,
    val chunkSize: Int,
    val format: ByteArray,
    val subchunk1Id: ByteArray,
    val subchunk1Size: Int,
    val audioFormat: Short,
    val numChannels: Short,
    val sampleRate: Int,
    val byteRate: Int,
    val blockAlign: Short,
    val bitsPerSample: Short,
    val subchunk2Id: ByteArray,
    val subchunk2Size: Int
)