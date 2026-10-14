package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.component.CustomBottomNavigationBar
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun ClassTabScaffold(
    onTabClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            CustomBottomNavigationBar(
                selectedRoute = BottomNavItem.Class.route,
                onItemClick = onTabClick
            )
        },
        containerColor = PickeTheme.colors.backgroundBeige
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassTabScaffoldPreview() {
    PickeTheme {
        ClassTabScaffold(onTabClick = {}) { contentModifier ->
            Box(
                modifier = contentModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "클래스 화면", style = PickeTheme.typography.headingMd)
            }
        }
    }
}