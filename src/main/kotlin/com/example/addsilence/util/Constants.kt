package com.example.addsilence.util

object Constants {

    const val NUM_CHANNELS: Short = 1
    const val BITS_PER_SAMPLE: Short = 16
    const val BUFFER_SIZE_SECONDS = 1
    const val SAMPLE_RATE_HERTZ = 16_000
    const val BUFFER_SIZE = SAMPLE_RATE_HERTZ * 2

    const val OUTPUT_FOLDER = "output/"
    const val DATASET_FOLDER = "data/marvin/"
    const val SUPPORTED_AUDIO_FILE_FORMAT = "wav"
    const val FILE_SILENT = "data/silence-16kHz-16bit.pcm"
}