package mdrew.jsonexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import mdrew.jsonexample.component.RootComponentImpl
import mdrew.jsonexample.di.DefaultObjectGraph
import mdrew.jsonexample.route.RootRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootRoute(
                DefaultObjectGraph.rootComponent(componentContext = defaultComponentContext())
            )
        }
    }
}