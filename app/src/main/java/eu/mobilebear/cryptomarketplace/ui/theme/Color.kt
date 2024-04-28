package eu.mobilebear.cryptomarketplace.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFE9E9E9)
val Gray300 = Color(0xFFE0E0E0)
val Gray400 = Color(0xFFC6C6C6)
val Gray500 = Color(0xFFB8B8B8)
val Gray600 = Color(0xFF757575)
val Gray700 = Color(0xFF6A6A6A)
val Gray800 = Color(0xFF3B3B3B)
val Gray900 = Color(0xFF212121)

val Blue100 = Color(0xFFF2F8FE)
val Blue200 = Color(0xFF90C7FF)
val Blue300 = Color(0xFF92BBF8)
val Blue400Light = Color(0xFF2F80ED)
val Blue400Dark = Color(0xFF2F80ED)
val Blue500Light = Color(0xFF0868F3)
val Blue500Dark = Color(0xFF207BFF)

val Green100 = Color(0xFFEBF8E4)
val Green200 = Color(0xFF9BDC7C)
val Green300 = Color(0xFF8BD966)
val Green400Light = Color(0xFF41C303)
val Green400Dark = Color(0xFF41C303)
val Green500Light = Color(0xFF00C873)
val Green500Dark = Color(0xFF00E081)

val Red100 = Color(0xFFFAE9E7)
val Red200 = Color(0xFFE99288)
val Red300 = Color(0xFFE98175)
val Red400Light = Color(0xFFDA291C)
val Red400Dark = Color(0xFFFA3F20)
val Red500Light = Color(0xFFC91800)
val Red500Dark = Color(0xFFDA291C)

val Orange100 = Color(0xFFFFEADD)
val Orange200 = Color(0xFFFFB482)
val Orange300 = Color(0xFFFFA265)
val Orange400Light = Color(0xFFED8B00)
val Orange400Dark = Color(0xFFF19602)
val Orange450 = Color(0xFFEE732E)
val Orange500Light = Color(0xFFE3630B)
val Orange500Dark = Color(0xFFE3630B)

val Yellow100 = Color(0xFFFEF6E4)
val Yellow200 = Color(0xFFFAD47A)
val Yellow300 = Color(0xFFFDC84A)
val Yellow400Light = Color(0xFFFFB400)
val Yellow400Dark = Color(0xFFF0B608)
val Yellow500Light = Color(0xFFECAA00)
val Yellow500Dark = Color(0xFFE3A808)

val DarkColorScheme = darkColorScheme(
    primary = Blue400Dark,
    secondary = Blue500Dark,
    tertiary = Green400Dark,
    background = Gray900,
    surface = Gray700,
    error = Red500Dark
)

val LightColorScheme = lightColorScheme(
    primary = Blue400Light,
    secondary = Blue500Light,
    tertiary = Green500Light,
    background = Gray200,
    surface = Gray400,
    error = Red500Light,

)
