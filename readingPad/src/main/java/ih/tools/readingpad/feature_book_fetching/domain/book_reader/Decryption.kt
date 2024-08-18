package book_generation.feature.book_creation.security

import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Image


class Decryption {
    companion object {
        private const val KEY = 5

        fun decryption(txt: String): String {
            val contents = txt.split(Image.IMAGE_SPLITTER)
            var decryptionTxt = ""
            for (i in (contents.indices) step 2) {
                decryptionTxt += decrypt(contents[i])
                if (i < contents.size - 1) {
                    decryptionTxt += contents[i + 1] // image
                }
            }
            return decryptionTxt
        }

        private fun decrypt(text: String): String {
            var encryptedText = ""
            for (i in text) encryptedText += (i - KEY)
            return encryptedText
        }
    }
}