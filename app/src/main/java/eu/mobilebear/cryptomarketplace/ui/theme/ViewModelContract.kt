package eu.mobilebear.cryptomarketplace.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

data class StateEffectDispatch<STATE, EFFECT, EVENT>(
    val state: STATE,
    val effectFlow: Flow<EFFECT>,
    val dispatch: (EVENT) -> Unit,
)

@Composable
inline fun <reified STATE, EFFECT, EVENT> use(
    viewModel: ViewModelContract<EVENT, EFFECT, STATE>,
): StateEffectDispatch<STATE, EFFECT, EVENT> {
  val state by viewModel.state.collectAsState()

  val dispatch: (EVENT) -> Unit = { event ->
    viewModel.event(event)
  }
  return StateEffectDispatch(
      state = state,
      effectFlow = viewModel.effect,
      dispatch = dispatch
  )
}

interface ViewModelContract<EVENT, EFFECT, STATE> {
  val state: StateFlow<STATE>
  val effect: Flow<EFFECT>
  fun event(event: EVENT)
}

@Suppress("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffect(collector: FlowCollector<T>) {
  val flow = this
  LaunchedEffect(key1 = flow) {
    flow.collect(collector)
  }
}