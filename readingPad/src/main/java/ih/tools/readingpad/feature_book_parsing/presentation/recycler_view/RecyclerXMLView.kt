package ih.tools.readingpad.feature_book_parsing.presentation.recycler_view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_fetching.domain.book_reader.Page
import ih.tools.readingpad.feature_book_fetching.domain.use_cases.getMetadata
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.convertPagesToSpannedPages
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XMLView(
    context: Context,
    viewModel: BookContentViewModel,
    modifier: Modifier
) {
    val book = viewModel.book
    val state by viewModel.state
    var lastDarkStatus = false

    val spannableString = remember {
        mutableStateOf(state.spannableContent)
    }
    val recyclerPages = remember { mutableStateListOf<SpannedPage>() }
    val pages = mutableListOf<Page>()
    val metadata = getMetadata(context)
    val adapter = RecyclerViewAdapter(viewModel, recyclerPages)
    val view =
        LayoutInflater.from(context).inflate(R.layout.recycler_view, null, false)

    val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    var scrollPosition by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

//    LaunchedEffect(key1 = book) { // Launch a coroutine when 'book' changes
//        val page = book.chapters[book.chapters.size - 1].pages[4]
//        pages.add(page)
//        val spannedPages = this.async {
//            convertPagesToSpannedPages(pages, metadata, context, book, recyclerView, viewModel)
//        }
//        Log.d("BookContentView", "spanned pages = ${spannedPages.await().size}")
//
//        if (spannedPages.await().isNotEmpty()) {
//
//            recyclerPages.addAll(spannedPages.await()) // Populatethe list initially
//            Log.d("BookContentView", "spanned pages = {${recyclerPages.size}}")
//            adapter.notifyDataSetChanged()
//        }
//    }

    AndroidView(

        factory = { currentContext ->

            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(currentContext, LinearLayoutManager.VERTICAL, false)

            // Scroll listener (if you need to track scroll position)
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scrollPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }
            })
            view
        },
        update = { view ->
//                if (isDark != lastDarkStatus) {
//                    Log.d("XMLView", "isDark = $isDark")
//                    adapter.notifyDataSetChanged()
//                lastDarkStatus = isDark
//                }

            // This block is called on each recomposition
            coroutineScope.launch {
                val pages = book.chapters[book.chapters.size - 1].pages
                val spannedPages = convertPagesToSpannedPages(
                    pages,
                    metadata,
                    context,
                    book,
                    recyclerView,
                    viewModel
                )
                //recyclerPages.clear() // Clear the existing list
                recyclerPages.addAll(spannedPages) // Update the state with new pages

                adapter.notifyDataSetChanged()
            }

        }
    )
}