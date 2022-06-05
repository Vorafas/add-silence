package com.example.addsilence

import com.example.addsilence.util.Constants
import com.example.addsilence.util.WavFormatter
import com.example.addsilence.wave.WavHeaderReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

private val silentBuffer: ByteArray by lazy { silentBuffer() }

fun main() {
    File(Constants.DATASET_FOLDER).listFiles()?.forEach { file ->
        if (file.extension == Constants.SUPPORTED_AUDIO_FILE_FORMAT) {
            try {
                addSilence(file, createFile(Constants.OUTPUT_FOLDER + file.name))
            } catch (exc: IOException) {
                println(exc.message)
            }
        }
    }
}

private fun createFile(fileName: String) = File(fileName).apply {
    parentFile.mkdirs()
    createNewFile()
}

private fun silentBuffer(): ByteArray = FileInputStream(File(Constants.FILE_SILENT)).use(InputStream::readBytes)

private fun isValidWavFile(input: InputStream): Boolean {
    val wavHeader = WavHeaderReader.read(input)
    return wavHeader.run {
        sampleRate == Constants.SAMPLE_RATE_HERTZ &&
                numChannels == Constants.NUM_CHANNELS &&
                bitsPerSample == Constants.BITS_PER_SAMPLE
    }
}

private fun addSilence(inputFile: File, outputFile: File) {
    FileInputStream(inputFile).use { input ->
        if (isValidWavFile(input)) {
            val audioData = input.readBytes()
            FileOutputStream(outputFile).use { output ->
                WavFormatter.writeHeader(output, Constants.NUM_CHANNELS, Constants.SAMPLE_RATE_HERTZ, Constants.BITS_PER_SAMPLE)
                if (audioData.size >= Constants.BUFFER_SIZE) {
                    println("File must be no longer than ${Constants.BUFFER_SIZE_SECONDS} sec. '${inputFile.name}'")
                    output.write(audioData)
                } else {
                    val chunkSize = (Constants.BUFFER_SIZE - audioData.size) / 2
                    val firstChunk = ByteArray(if (chunkSize % 2 == 0) chunkSize else chunkSize - 1)
                    val lastChunk = ByteArray(if (chunkSize % 2 == 0) chunkSize else chunkSize + 1)

                    System.arraycopy(silentBuffer, 0, firstChunk, 0, firstChunk.size)
                    System.arraycopy(silentBuffer, firstChunk.size, lastChunk, 0, lastChunk.size)

                    output.write(firstChunk)
                    output.write(audioData)
                    output.write(lastChunk)
                }
                WavFormatter.updateHeader(outputFile)
            }
        } else {
            println("The input file is not valid '${inputFile.name}'")
        }
    }
}