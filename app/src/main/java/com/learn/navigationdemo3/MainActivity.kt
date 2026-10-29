package com.learn.navigationdemo3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.learn.navigationdemo3.ui.theme.NavigationDemo3Theme


// Screen1 -> Screen2 -> screen3 > back -> back
// skipping a screen: screen3 -> screen1

// Destinations implementing NavKey
data class Screen1(
    val message: String? = null
) : NavKey

data object Screen2 : NavKey
data object Screen3 : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationDemo3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun NavigationApp(modifier: Modifier = Modifier) {
    val backStack = remember {
        mutableStateListOf<NavKey>(Screen1())
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen1> {
                Screen1UI(
                    message = it.message, onNext = {
                        backStack.add(Screen2)
                    })
            }
            entry<Screen2> {
                Screen2UI(onNext = {
                    backStack.add(Screen3)
                })
            }
            entry<Screen3> {
                Screen3UI(onBackToScreen2 = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, onGoToScreen1stScreen = {
                    backStack.clear()
                    backStack.add(Screen1("Hello I am from screen 1"))
                })
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun Screen1UI(message: String? = null, onNext: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen 1", style = MaterialTheme.typography.headlineLarge
        )

        if (message != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Received data:\n$message",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onNext) { Text("Go to Screen 2") }
    }
}

@Composable
fun Screen2UI(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen 2",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onNext) { Text("Go to Screen 3") }
    }
}

@Composable
fun Screen3UI(
    onBackToScreen2: () -> Unit,
    onGoToScreen1stScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen 3",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(16.dp))
        Text("Choose 1 of the navigation options:")
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onBackToScreen2,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Button 2 : Go to screen 2")
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onGoToScreen1stScreen,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Button 1 : Go to screen 1 with data")
        }
    }
}