package alexschool.bookreader

import alexschool.bookreader.network.NetworkViewModel
import alexschool.bookreader.ui.theme.AlexSchoolTheme
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ih.tools.readingpad.util.PermissionRequester

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PermissionRequester {
    private val viewModel: AlexSchoolViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions.entries.all { it.value }
            permissionCallback?.invoke(isGranted)
            permissionCallback = null
        }

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private fun haveStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestStoragePermission(callback: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permission is granted automatically on older versions
            callback(true)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Handle scoped storage on Android 10 and higher
            val hasReadPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val hasWritePermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            if (hasReadPermission && hasWritePermission) {
                callback(true)
            } else {
                permissionCallback = callback
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_MEDIA_LOCATION
                    )
                )
            }
        } else {
            // Request permission on Android 6.0 to 9.0
            val hasReadPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            val hasWritePermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            if (hasReadPermission && hasWritePermission) {
                callback(true)
            } else {
                permissionCallback = callback
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this is used to access the provides of dagger hilt
//        val entryPoint = EntryPointAccessors.fromApplication(
//            applicationContext,
//            LibraryEntryPoint::class.java
//        )
        //example
//        val bookRepository = entryPoint.provideBookRepository()
//        // Use bookRepository as needed


        setContent {
            AlexSchoolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    Navigation(viewModel, networkViewModel)

                    //                  LaunchedEffect(currentScreen) {
//                        if (currentScreen == "ReadingPadScreen") {
//                            window.setFlags(
//                                WindowManager.LayoutParams.FLAG_SECURE,
//                                WindowManager.LayoutParams.FLAG_SECURE
//                            )
//                        } else {
//                            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
//                        }
//                  }
                }
            }
        }
    }
}





