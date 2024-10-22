package ih.tools.readingpad.feature_book_fetching.domain.book_reader



class Image(private val imageName: String, var encodingImage: String?) {

    fun getMyData(mainPath: String) {
        val localFile = LocalFile.instance()
        val imageData = localFile.encode(localFile.readBytes(mainPath + imageName))
        this.encodingImage = imageData
    }

    companion object {
        const val IMAGE_SPLITTER = "!@D%#^$@@#BFSA#$"
        const val IMAGE_NAME_SPLITTER = "!@D%#^$#BFSA#$"
    }

}