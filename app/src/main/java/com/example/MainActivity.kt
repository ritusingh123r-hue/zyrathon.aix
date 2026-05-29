package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.pages.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ZyrathonViewModel
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberSurfaceCard
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

class MainActivity : ComponentActivity() {
    private val viewModel: ZyrathonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val loggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

                // State controlling side menu drawer visibility
                var isSideMenuOpen by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CyberBackground)
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (currentScreen != AppScreen.LANDING && currentScreen != AppScreen.LOGIN) {
                                CyberTopAppBar(
                                    activeScreen = currentScreen,
                                    onMenuToggle = { isSideMenuOpen = !isSideMenuOpen }
                                )
                            }
                        },
                        bottomBar = {
                            if (loggedIn && currentScreen != AppScreen.LANDING && currentScreen != AppScreen.LOGIN) {
                                CyberBottomBar(
                                    currentScreen = currentScreen,
                                    onNavigate = { screen ->
                                        isSideMenuOpen = false
                                        viewModel.navigateTo(screen)
                                    }
                                )
                            }
                        },
                        contentWindowInsets = WindowInsets.safeDrawing,
                        containerColor = CyberBackground
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            // Page Switcher
                            AnimatedContent(
                                targetState = currentScreen,
                                transitionSpec = {
                                    (fadeIn() + scaleIn(initialScale = 0.95f))
                                        .togetherWith(fadeOut() + scaleOut(targetScale = 0.98f))
                                },
                                label = "nav_transition"
                            ) { screen ->
                                when (screen) {
                                    AppScreen.LANDING -> LandingPage(viewModel = viewModel)
                                    AppScreen.LOGIN -> LoginPage(viewModel = viewModel)
                                    AppScreen.DASHBOARD -> DashboardPage(viewModel = viewModel)
                                    AppScreen.AI_FITNESS -> AiFitnessPage(viewModel = viewModel)
                                    AppScreen.AI_NUTRITION -> AiNutritionPage(viewModel = viewModel)
                                    AppScreen.AI_CHAT -> AiChatbotPage(viewModel = viewModel)
                                    AppScreen.SUBSCRIPTION -> SubscriptionPage(viewModel = viewModel)
                                    AppScreen.ADMIN -> AdminPage(viewModel = viewModel)
                                }
                            }
                        }
                    }

                    // Cyberpunk Floating Side Panel Drawer (Overlay)
                    AnimatedVisibility(
                        visible = isSideMenuOpen,
                        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        CyberSidebarDrawer(
                            currentActiveScreen = currentScreen,
                            onClose = { isSideMenuOpen = false },
                            onItemClick = { target ->
                                isSideMenuOpen = false
                                viewModel.navigateTo(target)
                            },
                            onLogout = {
                                isSideMenuOpen = false
                                viewModel.logout()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CyberTopAppBar(
    activeScreen: AppScreen,
    onMenuToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .background(Color(0xFF07070F))
            .border(1.dp, Color(0xFF1E1E2C))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMenuToggle,
                modifier = Modifier.testTag("menu_drawer_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "menu",
                    tint = NeonBlue
                )
            }

            Text(
                text = activeScreen.name.replace("_", " "),
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            // Tech visual indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(NeonBlue)
            )
        }
    }
}

@Composable
fun CyberBottomBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(64.dp)
            .background(Color(0xFF07070F))
            .border(1.dp, Color(0xFF1E1E2C)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Dashboard,
                label = "DASH",
                isSelected = currentScreen == AppScreen.DASHBOARD,
                onClick = { onNavigate(AppScreen.DASHBOARD) },
                testTagStr = "bottom_tab_dash"
            )
            BottomNavItem(
                icon = Icons.Default.FitnessCenter,
                label = "FIT",
                isSelected = currentScreen == AppScreen.AI_FITNESS,
                onClick = { onNavigate(AppScreen.AI_FITNESS) },
                testTagStr = "bottom_tab_fit"
            )
            BottomNavItem(
                icon = Icons.Default.Restaurant,
                label = "DIET",
                isSelected = currentScreen == AppScreen.AI_NUTRITION,
                onClick = { onNavigate(AppScreen.AI_NUTRITION) },
                testTagStr = "bottom_tab_diet"
            )
            BottomNavItem(
                icon = Icons.Default.Chat,
                label = "BOT",
                isSelected = currentScreen == AppScreen.AI_CHAT,
                onClick = { onNavigate(AppScreen.AI_CHAT) },
                testTagStr = "bottom_tab_chat"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTagStr: String
) {
    Column(
        modifier = Modifier
            .testTag(testTagStr)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) NeonBlue else TextGray,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = if (isSelected) Color.White else TextGray,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun CyberSidebarDrawer(
    currentActiveScreen: AppScreen,
    onClose: () -> Unit,
    onItemClick: (AppScreen) -> Unit,
    onLogout: () -> Unit
) {
    // Semi-transparent overlay clickable background dismisses drawer
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClose() }
    ) {
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .background(Color(0xFF090913))
                .border(1.dp, NeonPurple)
                .clickable(enabled = false) { } // prevent clicks bubbling
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Brand Header
            Text(
                text = "ZYRATHON-X AI",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "OPERATOR PANEL v1.0",
                color = NeonBlue,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Divider(color = Color(0xFF1B1B2C), modifier = Modifier.padding(bottom = 16.dp))

            // Sidebar navigations
            SidebarDrawerItem(
                icon = Icons.Default.Payment,
                label = "SAAS BILLING MATRIX",
                isSelected = currentActiveScreen == AppScreen.SUBSCRIPTION,
                onClick = { onItemClick(AppScreen.SUBSCRIPTION) },
                testTagStr = "drawer_billing_btn"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SidebarDrawerItem(
                icon = Icons.Default.AdminPanelSettings,
                label = "ADMIN CONTROL PORT",
                isSelected = currentActiveScreen == AppScreen.ADMIN,
                onClick = { onItemClick(AppScreen.ADMIN) },
                testTagStr = "drawer_admin_btn"
            )

            Spacer(modifier = Modifier.weight(1f))

            Divider(color = Color(0xFF1B1B2C))

            Spacer(modifier = Modifier.height(12.dp))

            // Logout action item
            Row(
                modifier = Modifier
                    .testTag("drawer_logout_btn")
                    .fillMaxWidth()
                    .clickable { onLogout() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "logout",
                    tint = CyberMagenta,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "DISCONNECT TERMINAL",
                    color = CyberMagenta,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SidebarDrawerItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTagStr: String
) {
    Row(
        modifier = Modifier
            .testTag(testTagStr)
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Color(0xFF161324) else Color.Transparent)
            .border(1.dp, if (isSelected) NeonPurple else Color.Transparent, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) NeonPurple else Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.LightGray,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
