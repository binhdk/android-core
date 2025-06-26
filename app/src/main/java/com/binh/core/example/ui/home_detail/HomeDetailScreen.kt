package com.binh.core.example.ui.home_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeDetailScreen(
    modifier: Modifier = Modifier,
    homeDetailViewModel: HomeDetailViewModel = hiltViewModel()
) {
    HomeDetailScreenContent(modifier = modifier)
}

@Composable
internal fun HomeDetailScreenContent(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(text = "Home")
    }

}