package com.headway.bablicabdriver.screen.dashboard.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewModelScope

// ─────────────────────────────────────────────
// DATA MODELS
// ─────────────────────────────────────────────

data class ReelModel(
    val id: String,
    val videoThumbnail: String,  // Using image as video placeholder
    val videoUrl: String,
    val user: ReelUser,
    val description: String,
    val audioName: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isFollowing: Boolean = false,
    val isMuted: Boolean = false
)

data class ReelUser(
    val id: String,
    val username: String,
    val profilePicUrl: String,
    val isVerified: Boolean = false
)

// ─────────────────────────────────────────────
// DUMMY API / REPOSITORY
// ─────────────────────────────────────────────

object ReelsDummyApi {
    fun getReels(): List<ReelModel> = listOf(
        ReelModel(
            id = "1",
            videoThumbnail = "https://picsum.photos/seed/reel1/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u1", "rahul_photography", "https://i.pravatar.cc/150?img=1", true),
            description = "Golden hour magic ✨ Jab light perfect ho toh photo toh banta hai 📸 #photography #goldenhour #reels",
            audioName = "🎵 Tum Hi Ho - Arijit Singh",
            likesCount = 124500,
            commentsCount = 2341,
            sharesCount = 890
        ),
        ReelModel(
            id = "2",
            videoThumbnail = "https://picsum.photos/seed/reel2/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u2", "priya_dances", "https://i.pravatar.cc/150?img=5"),
            description = "New dance challenge 🔥💃 Try karo aur tag karo! #dance #trending #viral #reels",
            audioName = "🎵 Kesariya - Brahmastra",
            likesCount = 89200,
            commentsCount = 4523,
            sharesCount = 3100
        ),
        ReelModel(
            id = "3",
            videoThumbnail = "https://picsum.photos/seed/reel3/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u3", "foodie_amit", "https://i.pravatar.cc/150?img=8", true),
            description = "Ghar pe restaurant wali biryani 🍛😍 Recipe comment mein! #food #biryani #cooking #homefood",
            audioName = "🎵 Phir Bhi Tumko Chahunga",
            likesCount = 210000,
            commentsCount = 8900,
            sharesCount = 5600
        ),
        ReelModel(
            id = "4",
            videoThumbnail = "https://picsum.photos/seed/reel4/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u4", "travel_with_neha", "https://i.pravatar.cc/150?img=9"),
            description = "Manali snowfall 2024 ❄️🏔️ Ek baar toh jaana chahiye! #travel #manali #snow #himachal",
            audioName = "🎵 Channa Mereya - Ae Dil Hai Mushkil",
            likesCount = 56700,
            commentsCount = 1230,
            sharesCount = 670
        ),
        ReelModel(
            id = "5",
            videoThumbnail = "https://picsum.photos/seed/reel5/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u5", "tech_with_ravi", "https://i.pravatar.cc/150?img=12", true),
            description = "Android 15 ka ye feature nahi dekha? 🤯📱 #android #tech #coding #programming",
            audioName = "🎵 Original Audio",
            likesCount = 34500,
            commentsCount = 987,
            sharesCount = 2100
        ),
        ReelModel(
            id = "6",
            videoThumbnail = "https://picsum.photos/seed/reel6/400/700",
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            user = ReelUser("u6", "comedy_king_rohan", "https://i.pravatar.cc/150?img=15"),
            description = "Bhai ye toh ho hi jaata hai 😂🤣 Like karo agar relatable laga! #comedy #funny #memes #relatable",
            audioName = "🎵 Trending Audio",
            likesCount = 450000,
            commentsCount = 12000,
            sharesCount = 8900
        ),
    )
}

// ─────────────────────────────────────────────
// VIEW MODEL
// ─────────────────────────────────────────────

class ReelsViewModel : ViewModel() {
    private val _reels = MutableStateFlow<List<ReelModel>>(emptyList())
    val reels: StateFlow<List<ReelModel>> = _reels

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadReels()
    }

    private fun loadReels() {
        viewModelScope.launch {
            delay(800) // Simulate network call
            _reels.value = ReelsDummyApi.getReels()
            _isLoading.value = false
        }
    }

    fun toggleLike(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) {
                reel.copy(
                    isLiked = !reel.isLiked,
                    likesCount = if (reel.isLiked) reel.likesCount - 1 else reel.likesCount + 1
                )
            } else reel
        }
    }

    fun toggleFollow(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) reel.copy(isFollowing = !reel.isFollowing) else reel
        }
    }

    fun toggleMute(reelId: String) {
        _reels.value = _reels.value.map { reel ->
            if (reel.id == reelId) reel.copy(isMuted = !reel.isMuted) else reel
        }
    }
}

// ─────────────────────────────────────────────
// HELPER: Format Count (1.2K, 4.5M etc.)
// ─────────────────────────────────────────────

fun formatCount(count: Int): String = when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
    count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
    else -> count.toString()
}

// ─────────────────────────────────────────────
// MAIN REELS SCREEN
// ─────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsScreen() {
    val viewModel : ReelsViewModel = viewModel()
    val reels by viewModel.reels.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            ReelsLoadingScreen()
        } else {
            val pagerState = rememberPagerState(pageCount = { reels.size })

            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSize = PageSize.Fill,
                beyondViewportPageCount = 1
            ) { page ->
                val reel = reels[page]
                ReelItem(
                    reel = reel,
                    isVisible = pagerState.currentPage == page,
                    onLikeClick = { viewModel.toggleLike(reel.id) },
                    onFollowClick = { viewModel.toggleFollow(reel.id) },
                    onMuteClick = { viewModel.toggleMute(reel.id) }
                )
            }

            // Top bar
            ReelsTopBar(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

// ─────────────────────────────────────────────
// TOP BAR
// ─────────────────────────────────────────────

@Composable
fun ReelsTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Reels",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────
// SINGLE REEL ITEM
// ─────────────────────────────────────────────

@Composable
fun ReelItem(
    reel: ReelModel,
    isVisible: Boolean,
    onLikeClick: () -> Unit,
    onFollowClick: () -> Unit,
    onMuteClick: () -> Unit
) {
    var showHeart by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    val heartScale by animateFloatAsState(
        targetValue = if (showHeart) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heart"
    )

    LaunchedEffect(showHeart) {
        if (showHeart) {
            delay(800)
            showHeart = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (!reel.isLiked) onLikeClick()
                        showHeart = true
                    },
                    onTap = { isPaused = !isPaused }
                )
            }
    ) {
        // Video / Thumbnail
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(reel.videoThumbnail)
                .crossfade(true)
                .build(),
            contentDescription = "Reel Video",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark gradient overlay (bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Top gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
        )

        // Pause Icon (center)
        AnimatedVisibility(
            visible = isPaused,
            modifier = Modifier.align(Alignment.Center),
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Paused",
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .padding(12.dp)
            )
        }

        // Double tap heart animation
        if (showHeart) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(heartScale * 1.5f)
                    .size(100.dp)
            )
        }

        // Right side action buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Profile picture with follow button
            Box(contentAlignment = Alignment.BottomCenter) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(reel.user.profilePicUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .offset(y = 10.dp)
                        .size(20.dp)
                        .background(
                            if (reel.isFollowing) Color.Gray else Color(0xFFE1306C),
                            CircleShape
                        )
                        .clickable { onFollowClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (reel.isFollowing) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Follow",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Like Button
            ActionButton(
                icon = if (reel.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                count = formatCount(reel.likesCount),
                tint = if (reel.isLiked) Color(0xFFE1306C) else Color.White,
                onClick = onLikeClick
            )

            // Comment Button
            ActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                count = formatCount(reel.commentsCount),
                tint = Color.White,
                onClick = {}
            )

            // Share Button
            ActionButton(
                icon = Icons.Outlined.Send,
                count = formatCount(reel.sharesCount),
                tint = Color.White,
                onClick = {}
            )

            // More options
            ActionButton(
                icon = Icons.Default.MoreVert,
                count = "",
                tint = Color.White,
                onClick = {}
            )

            // Rotating music disc
            RotatingMusicDisc(profilePicUrl = reel.user.profilePicUrl, isVisible = isVisible)
        }

        // Bottom info: user + description + audio
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, end = 70.dp, bottom = 28.dp)
                .navigationBarsPadding()
        ) {
            // Username
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = reel.user.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                if (reel.user.isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Color(0xFF1DA1F2),
                        modifier = Modifier.size(16.dp)
                    )
                }
                // Follow button (text)
                if (!reel.isFollowing) {
                    Text(
                        text = "• Follow",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onFollowClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = reel.description,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Audio Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = reel.audioName,
                    color = Color.White,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Mute / Unmute button (top right corner near bottom)
        IconButton(
            onClick = onMuteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 70.dp, end = 12.dp)
        ) {
            Icon(
                imageVector = if (reel.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = "Mute",
                tint = Color.White,
                modifier = Modifier
                    .size(22.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .padding(4.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────
// ACTION BUTTON (Like, Comment, Share)
// ─────────────────────────────────────────────

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val scale = remember { Animatable(1f) }
        val scope = rememberCoroutineScope()

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(30.dp)
                .scale(scale.value)
                .clickable {
                    scope.launch {
                        scale.animateTo(0.8f, spring(stiffness = Spring.StiffnessHigh))
                        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    }
                    onClick()
                }
        )
        if (count.isNotEmpty()) {
            Text(
                text = count,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─────────────────────────────────────────────
// ROTATING MUSIC DISC
// ─────────────────────────────────────────────

@Composable
fun RotatingMusicDisc(profilePicUrl: String, isVisible: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(44.dp)
            .rotate(if (isVisible) rotation else 0f)
            .background(Color(0xFF1A1A1A), CircleShape)
            .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePicUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Audio",
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        // Center dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color(0xFF1A1A1A), CircleShape)
                .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
        )
    }
}

// ─────────────────────────────────────────────
// LOADING SCREEN
// ─────────────────────────────────────────────

@Composable
fun ReelsLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = Color(0xFFE1306C))
            Text("Loading Reels...", color = Color.White, fontSize = 14.sp)
        }
    }
}