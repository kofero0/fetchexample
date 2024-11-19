package mdrew.jsonexample.route

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import mdrew.jsonexample.component.AboutComponent

@Composable
fun AboutRoute(component:AboutComponent){
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("By: Mitchell Drew")
        Text("mitch.drew@googlemail.com")
    }
}