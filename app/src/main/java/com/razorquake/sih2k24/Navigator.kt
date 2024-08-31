package com.razorquake.sih2k24

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.razorquake.sih2k24.sign_translation.SignTranslatorScreen
import com.razorquake.sih2k24.text_translation.TTViewModel
import com.razorquake.sih2k24.text_translation.TextTranslatorScreen
import com.razorquake.sih2k24.ui.theme.SIH2K24Theme

@Composable
fun Navigator(){
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                title = "Home",
                icon = R.drawable.ic_home,
            ),
            BottomNavigationItem(
                title = "History",
                icon = R.drawable.ic_history,
            ),
            BottomNavigationItem(
                title = "Settings",
                icon = R.drawable.ic_settings,
            ),
        )
    }
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    selectedItem = when(backStackState?.destination?.route){
        Route.HomeScreen.route -> 0
        Route.HistoryScreen.route -> 1
        Route.SettingsScreen.route -> 2
        else -> 0
    }
    val isBottomBarVisible = remember(key1 = backStackState){
        backStackState?.destination?.route in listOf(
            Route.HomeScreen.route,
            Route.HistoryScreen.route,
            Route.SettingsScreen.route,
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible){
                BottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when(index){
                            0 -> navigateTo(navController, Route.HomeScreen.route)
                            1 -> navigateTo(navController, Route.HistoryScreen.route)
                            2 -> navigateTo(navController, Route.SettingsScreen.route)
                        }
                    }
                )
            }
        }
    ) {
        val bottomPaddingValues = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.statusBarsPadding()
        ){
            composable(Route.HomeScreen.route){
                HomeScreen(
                    onTextTranslationClick = {navController.navigate(Route.TextTranslationScreen.route)},
                    onSignTranslationClick = {navController.navigate(Route.SignTranslationScreen.route)},
                    modifier = Modifier
                )
            }
            composable(Route.HistoryScreen.route){
                HistoryScreen(
                    modifier = Modifier.padding(bottomPaddingValues)
                )
            }
            composable(Route.SettingsScreen.route){
                SettingsScreen(
                    modifier = Modifier.padding(bottomPaddingValues)
                )
            }
            composable(Route.SignTranslationScreen.route){
                SignTranslatorScreen()
            }
            composable(Route.TextTranslationScreen.route){
                val viewModel: TTViewModel = viewModel()
                val state = viewModel.state
                TextTranslatorScreen(
                    state = state.value,
                    onEvent = viewModel::onEvent,
                    onBackClick = {navController.navigateUp()},
                    onTranslationComplete = {}
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize()) {}
}

@Composable
fun HistoryScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize()) {

    }
}

@Composable
fun BottomNavigation(
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = item.title,
                            style = MaterialTheme.typography.labelSmall)
                    }
                },
                selected = index == selected,
                onClick = { onItemClick(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor  = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.body),
                    unselectedTextColor = colorResource(id = R.color.body),
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    }
}
private fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreenRoute ->
            popUpTo(homeScreenRoute) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}
data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val title: String,
)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsBottomNavigationPreview() {
    SIH2K24Theme {
        BottomNavigation(
            items = listOf(
                BottomNavigationItem(
                    icon = R.drawable.ic_home,
                    title = "Home",
                ),
                BottomNavigationItem(
                    icon = R.drawable.ic_history,
                    title = "Search",
                ),
                BottomNavigationItem(
                    icon = R.drawable.ic_settings,
                    title = "Bookmarks",
                ),
            ),
            selected = 0,
            onItemClick = { /*TODO*/ },
        )
    }

}