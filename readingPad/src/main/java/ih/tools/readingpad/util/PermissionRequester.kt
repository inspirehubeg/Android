package ih.tools.readingpad.util

interface PermissionRequester {
    fun requestStoragePermission(callback: (Boolean) -> Unit)
}