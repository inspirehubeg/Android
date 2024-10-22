package ih.tools.readingpad.feature_book_fetching.domain.book_reader

import android.util.Log


class Decryption {
    companion object {
        private const val KEY = 5

        fun decryption(txt: String): String {
            Log.d("pagesScreen", "decryption is called")
            val contents = txt.split(Image.IMAGE_SPLITTER)
            val decryptionTxt = StringBuilder("")
            Log.d("pagesScreen", "contents[i] = ${contents.size}")
            for (i in (contents.indices) step 2) {
                decryptionTxt.append( decrypt(contents[i]))
                if (i < contents.size - 1) {
                    decryptionTxt.append( contents[i + 1]) // image
                }
            }
            return decryptionTxt.toString()
        }
        fun decryption(txt: String, id:Int): String {
            Log.d("pagesScreen", "decryption is called")
            val contents = txt.split(Image.IMAGE_NAME_SPLITTER)
            val decryptionTxt = StringBuilder("")
            Log.d("pagesScreen", "contents[i] = ${contents.size}")
            for (i in (contents.indices) step 2) {
                decryptionTxt.append( decrypt(contents[i]))
                if (i < contents.size - 1) {
                    decryptionTxt.append( contents[i + 1]) // image
                }
            }
            return decryptionTxt.toString()
        }

        private fun decrypt(text: String): String {
            val encryptedText = StringBuilder("")
            Log.d("pagesScreen", "decrypt is called text size = ${text.length}")
            for (i in text){
                encryptedText.append (i - KEY)
               // Log.d("pagesScreen", "encryptedText = $encryptedText")
            }

            return encryptedText.toString()
        }
    }
}