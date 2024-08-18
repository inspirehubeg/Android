package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import java.io.File
import java.util.*

class LocalFile private constructor() {

    fun createFile(path: String, name: String): File {
        return File(path + name)
    }

    fun read(path: String): String {
        val file = File(path)
        return file.readText()
    }

    fun readBytes(path: String): ByteArray {
        val file = File(path)
        return file.readBytes()
    }

    fun write(path: String, content: String) {
        val directory = File(Path.getMainPath(path))
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(path)
        file.writeText(content)
    }

    fun encode(data: ByteArray): String {
        return Base64.getEncoder().encodeToString(data)
    }

    fun decode(data: String): ByteArray {
        return Base64.getDecoder().decode(data)
    }

    companion object {
        private var obj: LocalFile? = null
        fun instance(): LocalFile {
            if (obj == null) obj = LocalFile()
            return obj!!
        }
    }
}