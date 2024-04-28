package eu.mobilebear.cryptomarketplace.ui.widgets

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieScreen(@RawRes animation: Int) {
    Box(
        modifier = Modifier.fillMaxSize().padding(36.dp),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            composition = composition,
            progress = { progress },
        )
    }

}