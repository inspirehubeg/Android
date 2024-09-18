package alexschool.bookreader

import alexschool.bookreader.ui.theme.AlexSchoolTheme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AlexSchoolViewModel by viewModels()
    //private val PERMISSION_REQUEST_CODE = 123


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
                    Navigation(viewModel)

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

//    override fun onStart() {
//        super.onStart()
//        if (needToAccessExternalStorage()) {
//            requestStoragePermissions()
//        }
//    }
//    private fun needToAccessExternalStorage(): Boolean {
//        // Check if your app logic requires accessing external storage
//        // ... your logic ...
//        return true // For this example, we assume you need access
//    }
//
//    private fun requestStoragePermissions() {
//        // Permission request code from previous example
//        // ...
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ),
//                PERMISSION_REQUEST_CODE
//            )
//        } else {
//            hasStoragePermissions = true
//        }
//    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                hasStoragePermissions = true
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message)
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}



