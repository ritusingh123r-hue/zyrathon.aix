package com.example.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.components.GlowCard
import com.example.ui.components.PulsatingCore
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun LandingPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Premium Zyrathon corporate AI Logo
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(2.dp, Brush.linearGradient(listOf(NeonBlue, NeonPurple)), RoundedCornerShape(24.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_zyrathon_logo),
                            contentDescription = "Zyrathon Premium Logo",
                            modifier = Modifier.fillMaxSize().padding(4.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pulsating neural core alongside/beneath
                    PulsatingCore(modifier = Modifier.size(36.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ZYRATHON-X AI",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "QUANTUM PHYSIOLOGICAL SAAS MATRIX",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        letterSpacing = 3.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Text(
                        text = "Optimize your physical biomass via enterprise-grade bio-mentorship, active muscular logs, and direct neural Gemini telemetry.",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 500.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Premium Hero Banner image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Brush.linearGradient(listOf(NeonBlue, CyberMagenta)), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_matrix),
                    contentDescription = "Quantum Physiological Matrix Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                // Minimal dark overlay with neon info text at bottom left
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "SYS_ACTIVE // NEURAL_GRID_CALIBRATED",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CTA Enter Gateway
            CyberButton(
                text = "INITIALIZE NEURAL GATEWAY",
                onClick = { viewModel.navigateTo(AppScreen.LOGIN) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .widthIn(max = 400.dp),
                testTagStr = "launch_gateway_btn"
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Showcase Core Features
            SectionHeader(title = "Core Synapse Nodes", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(16.dp))

            FeaturePromoItem(
                icon = Icons.Default.FitnessCenter,
                title = "AI Neural Fitness Split",
                desc = "Custom muscular calibration. Switch between high-yield Gym Mode arrays and tactical home callisthenics supported by dynamic interval trackers.",
                glowColor = NeonBlue
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeaturePromoItem(
                icon = Icons.Default.Restaurant,
                title = "Precision Nutrition Engine",
                desc = "Synchronous macronutrient tracking. Slider-based BMI evaluation triggers instant macro updates logged directly to local Room DB.",
                glowColor = CyberMagenta
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeaturePromoItem(
                icon = Icons.Default.Chat,
                title = "Gemini Conversational Bio-Mentor",
                desc = "Connect live to Gemini LLM for professional dietary advice, customized plans, or motivation. Persistent chats logged chronologically.",
                glowColor = NeonPurple
            )

            Spacer(modifier = Modifier.height(32.dp))

            // SaaS tiers teaser
            SectionHeader(title = "SaaS Access Matrix", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                StaticTierTeaserCard("ORIGIN", "Free core tracking & limited chatbot slots.")
                Spacer(modifier = Modifier.height(8.dp))
                StaticTierTeaserCard("NEXUS", "$14/mo. Unlimited bio-chats, home/gym exercise builders.")
                Spacer(modifier = Modifier.height(8.dp))
                StaticTierTeaserCard("TITAN-X", "$39/mo. Deep biometrics, charts & wearable telemetry arrays.")
                Spacer(modifier = Modifier.height(8.dp))
                StaticTierTeaserCard("OMEGA INFINITY", "$99/mo. VIP neural dashboard + private bio-mentor guidance.")
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Testimonial
            GlowCard(borderColor = NeonPurple) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = NeonPurple,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "\"The metabolic integration was instant. Leveling up to OMEGA INFINITY unlocked customized macronutrient splits that bio-optimized my physical training parameters within weeks!\"",
                    color = TextWhite,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "— CYBERNETIC SPECIALIST VENITH, OMEGA NODE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Future Footer
            Text(
                text = "ZYRATHON-X AI • GLOBAL BIOGENIC NODE COORDINATES\nCODENAME: SECURE SAAS V1.0",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun FeaturePromoItem(
    icon: ImageVector,
    title: String,
    desc: String,
    glowColor: Color
) {
    GlowCard(borderColor = glowColor) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(glowColor.copy(alpha = 0.15f))
                    .border(1.dp, glowColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = glowColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = desc,
                    color = TextGray,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun StaticTierTeaserCard(title: String, benefit: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D0D14), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF1E1E2C), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = FontFamily.Monospace,
            color = NeonBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = benefit,
            color = TextWhite,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
