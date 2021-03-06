package sgtmelon.scriptum.presentation.control.file

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import sgtmelon.extension.getTime
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import java.io.*

/**
 * Class for help control manipulations with files.
 */
class FileControl(private val context: Context) : IFileControl {

    override val saveDirectory: File get() = Environment.getExternalStorageDirectory()

    override fun getExternalFiles(): List<File> {
        return ContextCompat.getExternalFilesDirs(context, null).filterNotNull()
    }

    override fun getExternalCache(): List<File> {
        return ContextCompat.getExternalCacheDirs(context).filterNotNull()
    }

    override fun readFile(path: String): String? {
        try {
            val inputStream = FileInputStream(File(path))
            val result = readInputStream(inputStream)
            inputStream.close()

            return result
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        return null
    }

    private fun readInputStream(inputStream: InputStream) = StringBuilder().apply {
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            append(line).append("\n")
        }

        reader.close()
    }.toString()

    /**
     * Return path to created file.
     */
    override fun writeFile(name: String, data: String): String? {
        val file = File(saveDirectory, name)

        try {
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            writeOutputStream(outputStream, data)
            outputStream.flush()
            outputStream.close()

            return file.path
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        return null
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    override fun getTimeName(@FileType type: String): String = getTime().plus(type)


    override suspend fun getFileList(@FileType type: String): List<FileItem> {
        val list = mutableListOf<FileItem>()

        list.addAll(getFileList(saveDirectory, type))

        for (it in getExternalFiles()) {
            list.addAll(getFileList(it, type))
        }

        for (it in getExternalCache()) {
            list.addAll(getFileList(it, type))
        }

        return list.sortedByDescending { it.name }
    }

    private suspend fun getFileList(directory: File, @FileType type: String): List<FileItem> {
        val fileList = directory.listFiles() ?: return emptyList()

        return ArrayList<FileItem>().apply {
            for (it in fileList) {
                when {
                    it.isDirectory -> addAll(getFileList(it, type))
                    it.name.endsWith(type) -> add(FileItem(it.nameWithoutExtension, it.path))
                }
            }
        }
    }

    companion object {
        private val TAG = FileControl::class.java.simpleName
    }

}