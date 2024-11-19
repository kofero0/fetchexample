package mdrew.jsonexample.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.chevron_down
import kotlinproject.composeapp.generated.resources.chevron_up
import kotlinproject.composeapp.generated.resources.populate_button_text
import mdrew.jsonexample.component.ListComponent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListRoute(component: ListComponent) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        component.onStart()
    }

    val uiState by component.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            component.onAction(ListComponent.Action.Populate)
        }) {
            Text(stringResource(Res.string.populate_button_text))
        }
        ExpandableList(moves = mutableListOf<Data>().apply {
            uiState.exampleJsonObjects.entries.forEach { entry ->
                add(Data(entry.key, entry.value.map { "id:${it.id},name:${it.name}" }))
            }
        }.sortedBy { it.id })
    }
}


data class Data(val id: Int, val attributes: List<String>)

@Composable
fun ObjectItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)
            .padding(start = 50.dp)
    )
}

@Composable
fun ObjectHeader(text: String, isExpanded: Boolean, onClick: () -> Unit) {
    Row(modifier = Modifier.clickable { onClick() }.background(Color.White)
        .padding(vertical = 20.dp, horizontal = 20.dp)) {
        val chevron = if (isExpanded) {
            Res.drawable.chevron_up
        } else {
            Res.drawable.chevron_down
        }
        Icon(
            painter = painterResource(chevron),
            contentDescription = "Localized description",
            modifier = Modifier.weight(0.1f)
        )
        Text(
            text = text,
            fontSize = 20.sp,
            modifier = Modifier.weight(1.0f).wrapContentHeight(Alignment.CenterVertically)
        )
    }
}

fun LazyListScope.ObjectSection(
    data: Data, isExpanded: Boolean, onClick: () -> Unit
) {

    item {
        ObjectHeader(
            text = "${data.id}", isExpanded = isExpanded, onClick = onClick
        )
    }

    if (isExpanded) {
        items(data.attributes.size) {
            val entry = data.attributes[it]
            ObjectItem(text = entry)
        }
    }
}

@Composable
fun ExpandableList(moves: List<Data>) {
    val isExpandedMap = List(moves.size) { index: Int -> index to false }.toMutableStateMap()

    LazyColumn(content = {
        moves.onEachIndexed { index, moveData ->
            ObjectSection(data = moveData, isExpanded = isExpandedMap[index] ?: false, onClick = {
                isExpandedMap[index] = !(isExpandedMap[index] ?: true)
            })
        }
    })
}