package com.example.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun LoginPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    var nameInput by remember { mutableStateOf("Zenith Cipher") }
    var emailInput by remember { mutableStateOf("janmayjaysinghrathor786@gmail.com") }
    var passwordInput by remember { mutableStateOf("********") }

    var localError by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Premium Corporate Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, androidx.compose.ui.graphics.Brush.linearGradient(listOf(NeonBlue, NeonPurple)), RoundedCornerShape(16.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_zyrathon_logo),
                    contentDescription = "Zyrathon Premium Logo",
                    modifier = Modifier.fillMaxSize().padding(2.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SECURE PORTAL",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextWhite,
                letterSpacing = 2.sp
            )
            Text(
                text = "NODE DEPLOYMENT ACCESS CONFIGURATION",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = NeonBlue,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Dynamic Auth Card
            GlowCard(borderColor = if (localError != null) CyberMagenta else NeonBlue) {
                Text(
                    text = "JWT VALIDATION PROMPT",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (localError != null) CyberMagenta else NeonBlue,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Name Input
                CyberTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                        localError = null
                    },
                    label = "Operator Name",
                    leadingIcon = Icons.Default.Person,
                    testTagStr = "login_name_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Input
                CyberTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        localError = null
                    },
                    label = "SaaS Matrix Email",
                    leadingIcon = Icons.Default.Email,
                    testTagStr = "login_email_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Password Simulation Overlay
                CyberTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = "Token Passkey",
                    leadingIcon = Icons.Default.Lock,
                    testTagStr = "login_pass_input"
                )

                localError?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it.uppercase(),
                        color = CyberMagenta,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isAuthenticating) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonBlue, modifier = Modifier.size(24.dp))
                    }
                } else {
                    // SECURE JWT BUTTON
                    CyberButton(
                        text = "Initialize Encrypted JWT Login",
                        onClick = {
                            // Validation checks
                            if (nameInput.length < 3) {
                                localError = "ERR: OPERATOR NAME CONFLICT (MIN 3 CHARS Required)"
                            } else if (!emailInput.contains("@") || !emailInput.contains(".")) {
                                localError = "ERR: INVALID SAAS EMAIL NODE COORDINATES"
                            } else {
                                isAuthenticating = true
                                viewModel.loginWithCredentials(emailInput, nameInput)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTagStr = "jwt_login_btn"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Google Direct SSO Mock button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF13131C))
                        .border(1.dp, Color(0xFF232333), RoundedCornerShape(8.dp))
                        .clickable {
                            isAuthenticating = true
                            viewModel.loginWithCredentials(
                                "janmayjaysinghrathor786@gmail.com",
                                "Zenith SingleSignOn"
                            )
                        }
                        .testTag("google_sso_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "AUTHENTICATE VIA OAUTH GOOGLE HUB",
                            color = Color.LightGray,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Platform Warning Label
            Text(
                text = "BY ENTERING, YOU VERIFY SYNC PROTOCOLS TO LOCAL ROOM INSTANCES.",
                color = Color.DarkGray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Return to Landing Page
            Text(
                text = "ABORT NODE EXPEDITIONS (RETURN)",
                color = NeonBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .clickable { viewModel.navigateTo(AppScreen.LANDING) }
                    .padding(8.dp)
                    .testTag("abort_login_btn")
            )
        }
    }
}
