package ih.tools.readingpad.feature_book_parsing.presentation.recycler_view

import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.domain.model.SpannedPage
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_book_parsing.presentation.text_view.IHTextView

class RecyclerViewAdapter(
    private val viewModel: BookContentViewModel,
    private val pages: MutableList<SpannedPage>
) : RecyclerView.Adapter<ViewHolder>() {

    //listen for bookmarkClicks
//    private var bookmarkSpanListener: BookmarkSpanListener? = null
//    fun setBookmarkSpanListener(listener: BookmarkSpanListener) {
//        this.bookmarkSpanListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ih_text_view_layout, parent, false)
        (view as IHTextView).movementMethod = LinkMovementMethod()
        return ViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        holder.bind(viewModel, page)
        //viewModel.fetchHighlights(page.pageNumber) //
    }

    override fun getItemCount(): Int {
        return pages.size
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: IHTextView = itemView as IHTextView

    fun bind(viewModel: BookContentViewModel, page: SpannedPage) {
        Log.d("IHTextView", "bind is called")
        //textView.setText(page.content, viewModel, currentPageNumber = page.pageNumber)
        Log.d("BookContentViewModel", "page number in bind = ${page.pageNumber}")
        viewModel.setTextView(textView)

//        viewModel.viewModelScope.launch {
//            val highlightsDeferred = async {
//                viewModel.fetchHighlights(page.pageNumber)
//                viewModel.pageHighlights
//            } // Fetch highlights asynchronously
//            val newHighlights : List<Highlight> = highlightsDeferred.await().value // Wait for highlights to be fetched
//            Log.d("IHTextView", "page = ${page.pageNumber} ,newHighlights fetched = $newHighlights")
//            updateTextViewWithHighlights(newHighlights, page) // Combine fetched and existing highlights
//        }
    }


//    private fun updateTextViewWithHighlights(highlights: List<Highlight>, item: SpannedPage) {
//        Log.d("IHTextView", "updateTextViewWithHighlights is called")
//        // ... (Your logic to apply highlights to the TextView)
//        highlights.forEach {highlight ->
//            if (item.pageNumber == highlight.pageNumber+1) {
//                Log.d("IHTextView", "inside if")
//               textView.drawHighlights(highlights)
//            }
//           // textView.invalidate()
//           // textView.setText(item.content)
//        }
//    }

}

//class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    fun bind(viewModel: BookContentViewModel, recyclerView: RecyclerView, item: SpannedPage) {
//        // Bind your note stuff here
//        // val customHolder = holder as CustomTextViewHolder // Cast to your custom ViewHolder
//        val pageContent = item.content
//        val pageNumber = item.pageNumber
//        // val chapterNumber = items[position]
//        (itemView as IHTextView).setText (pageContent, viewModel, currentPageNumber = pageNumber)
//        // Trigger highlight fetch for the newly bound page
//        viewModel.viewModelScope.launch {
//            viewModel.fetchHighlights(pageNumber)
//        }
//    }
//}
