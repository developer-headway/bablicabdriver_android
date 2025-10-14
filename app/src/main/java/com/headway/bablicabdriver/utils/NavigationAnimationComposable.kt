package com.headway.bablicabdriver.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun scaleIntoContainer(
    direction: String = "INWARDS",
    initialScale: Float = if (direction == "OUTWARDS") 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: String = "OUTWARDS",
    targetScale: Float = if (direction == "INWARDS") 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}

fun NavGraphBuilder.composable2(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLink: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route,
        enterTransition = {
            scaleIntoContainer(direction = "OUTWARDS")
        },
        exitTransition = {
            scaleOutOfContainer()
        },
        popEnterTransition = {
            scaleIntoContainer()
        },
        popExitTransition = {
            scaleOutOfContainer(direction = "INWARDS")
        },
        arguments = arguments,
        deepLinks = deepLink
    ) {
        content(it)
    }
}