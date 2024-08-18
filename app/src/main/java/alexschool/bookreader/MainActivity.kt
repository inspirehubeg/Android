package alexschool.bookreader

import alexschool.bookreader.data.AlexSchoolPrefManager
import alexschool.bookreader.ui.theme.AlexSchoolTheme
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AlexSchoolViewModel by viewModels()

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

                    LaunchedEffect(currentScreen) {
                        if (currentScreen == "ReadingPadScreen") {
                            window.setFlags(
                                WindowManager.LayoutParams.FLAG_SECURE,
                                WindowManager.LayoutParams.FLAG_SECURE
                            )
                        } else {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                        }
                    }
                }
            }
        }
    }
}



