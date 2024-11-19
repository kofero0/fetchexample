package mdrew.jsonexample.route

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.Indicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.tab_0_name
import kotlinproject.composeapp.generated.resources.tab_1_name
import mdrew.jsonexample.component.TabPageComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun TabPageRoute(component:TabPageComponent) {
    LifecycleEventEffect(Lifecycle.Event.ON_START){
        component.onStart()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TabBar(component, modifier = Modifier.fillMaxWidth())
        ChildrenTabs(
            stack = component.stack,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabBar(component: TabPageComponent, modifier: Modifier = Modifier){
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance
    val tabIndex by derivedStateOf { mutableStateOf(isActiveTab(activeComponent)) }

    val tabs = listOf(stringResource(Res.string.tab_0_name),stringResource(Res.string.tab_1_name))

    TabRow(
        modifier = modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp),
        selectedTabIndex = tabIndex.value,
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex.value])
            )
        },
        divider = { },
    ){
        tabs.forEachIndexed{ index, title ->
            Tab(
             selected = tabIndex.value == index,
                onClick = { tabClicked(index, component) }
            ){ Text(text = title, fontSize = 25.sp, color = Color.Black) }
        }

    }
}


private fun <T> isActiveTab(instance: T): Int = when(instance){
    is TabPageComponent.Child.ListChild -> 0
    is TabPageComponent.Child.AboutChild -> 1
    else -> 0
}

private fun tabClicked(index:Int, component:TabPageComponent) = when(index){
    0 -> component.onAction(TabPageComponent.Action.ListClicked)
    1 -> component.onAction(TabPageComponent.Action.AboutClicked)
    else -> {}
}

@Composable
private fun ChildrenTabs(stack: Value<ChildStack<*, TabPageComponent.Child>>, modifier: Modifier = Modifier){
    Children(
        stack = stack,
        modifier = modifier,
        animation = stackAnimation(fade())
    ){
        when(val child = it.instance){
            is TabPageComponent.Child.AboutChild -> AboutRoute(component = child.component)
            is TabPageComponent.Child.ListChild -> ListRoute(component = child.component)
        }
    }
}