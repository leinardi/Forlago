package com.leinardi.forlago.feature.foo.ui.bottomnavigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leinardi.forlago.library.ui.component.MainNavigationBarItem

@Composable
fun RowScope.FooNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MainNavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = Icons.Default.Inbox,
        modifier = modifier,
        label = "Foo",
    )
}
