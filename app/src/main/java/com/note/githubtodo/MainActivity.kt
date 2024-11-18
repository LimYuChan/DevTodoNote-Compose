package com.note.githubtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.flab.core.navigator.NavigationAction
import com.flab.core.navigator.Navigator
import com.note.core.common.Logg
import com.note.core.designsystem.NoteTheme
import com.note.core.ui.ObserveAsEvents
import com.note.githubtodo.navigation.NavigationRoot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }

        setContent {
            NoteTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ObserveAsEvents(flow = navigator.navigationActions) { action ->
                        when(action) {
                            is NavigationAction.Navigate -> {
                                navController.navigate(action.destination){
                                    action.navOptions(this)
                                }
                            }
                            NavigationAction.NavigateUp -> navController.navigateUp()
                        }
                    }
                    if(!viewModel.state.isCheckingAuth){
                        NavigationRoot(
                            navController = navController,
                            startDestination = navigator.startDestination
                        )
                    }
                }
            }
        }
    }
}