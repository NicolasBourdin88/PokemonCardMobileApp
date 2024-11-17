package com.example.pokemonultimate.ui.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// Mobile
@Preview(
    name = "(1) Mobile portrait light",
    group = "Mobile",
)
@Preview(
    name = "(2) Mobile portrait dark",
    group = "Mobile",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "(3) Mobile landscape light",
    group = "Mobile",
    device = "spec:parent=pixel_5,orientation=landscape",
)
@Preview(
    name = "(4) Mobile landscape dark",
    group = "Mobile",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_5,orientation=landscape",
)
annotation class PreviewMobile

// Tablet
@Preview(
    name = "(1) Tablet portrait light",
    group = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
)
@Preview(
    name = "(2) Tablet portrait dark",
    group = "Tablet",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
)
@Preview(
    name = "(3) Tablet landscape light",
    group = "Tablet",
    device = "spec:id=reference_tablet,shape=Normal,width=1280,height=800,unit=dp,dpi=240",
)
@Preview(
    name = "(4) Tablet landscape dark",
    group = "Tablet",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:id=reference_tablet,shape=Normal,width=1280,height=800,unit=dp,dpi=240",
)
annotation class PreviewTablet