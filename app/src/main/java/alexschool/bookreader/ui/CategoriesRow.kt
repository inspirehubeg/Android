package alexschool.bookreader.ui

import alexSchool.network.domain.Category
import alexschool.bookreader.network.NetworkViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoriesRow(networkViewModel: NetworkViewModel) {
    val categories by networkViewModel.categories.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (categories.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        } else {

            LazyRow() {
                items(categories) { category ->
                    CategoryItem(category = category)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp)
    ) {
        Column {
            Icon(imageVector = Icons.Default.Home, contentDescription = "category image")
            Text(text = category.name)
        }
    }
}