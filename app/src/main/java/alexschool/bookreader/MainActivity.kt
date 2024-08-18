package alexschool.bookreader

import alexschool.bookreader.data.AlexSchoolPrefManager
import alexschool.bookreader.ui.settings.updateAppLocale
import alexschool.bookreader.ui.theme.AlexSchoolTheme
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefManager = AlexSchoolPrefManager(this)
        val language = prefManager.getLanguage()
        updateAppLocale(this, language)

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
                    //Text(text = "Hello world")
                   Navigation()
//                    Log.d("MainActivity", "${book.chapters[0].pages}")
                }
            }
            }
        }


    }



