package com.example.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CyberTextField
import com.example.ui.components.GlowCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun AiChatbotPage(viewModel: ZyrathonViewModel) {
    val chatLogs by viewModel.chatHistory.collectAsStateWithLifecycle()
    val isThinking by viewModel.isAiGenerating.collectAsStateWithLifecycle()

    var activeInputText by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    // Keep chat scrolled down to very bottom on new inputs
    LaunchedEffect(chatLogs.size, isThinking) {
        if (chatLogs.isNotEmpty()) {
            lazyListState.animateScrollToItem(chatLogs.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Upper Header showing quick tools
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GEMINI BOT ASSISTANT",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.testTag("ai_chatbot_title")
                    )
                    Text(
                        text = "SECURE COGNITIVE SAAS BIOMENTOR HUB",
                        color = NeonBlue,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Clear history button with instant sqlite deletion trigger
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF2A0D15))
                        .border(1.dp, CyberMagenta, RoundedCornerShape(4.dp))
                        .clickable { viewModel.clearHistory() }
                        .padding(8.dp)
                        .testTag("clear_history_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "clear logs",
                        tint = CyberMagenta,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chat Feed Container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0C0C14))
                    .border(1.dp, Color(0xFF1E1E2C), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                if (chatLogs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CONVERSATIONAL LOG STREAM INERT.\nPROMPT BIOMENTOR CORE COGNITION ABOVE.",
                            color = Color.DarkGray,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(chatLogs) { message ->
                            ChatBubbleItem(text = message.text, isAi = message.isAi)
                        }

                        if (isThinking) {
                            item {
                                ThinkingBubbleItem()
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Input Row Panel
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CyberTextField(
                        value = activeInputText,
                        onValueChange = { activeInputText = it },
                        label = "Query Biomentor Terminal...",
                        testTagStr = "chat_message_input"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Glowing send button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isThinking) Color.DarkGray else NeonBlue)
                        .border(1.dp, if (isThinking) Color.Transparent else Color.Cyan, RoundedCornerShape(8.dp))
                        .clickable(enabled = !isThinking && activeInputText.isNotBlank()) {
                            viewModel.sendUserChatMessage(activeInputText)
                            activeInputText = ""
                        }
                        .testTag("send_chat_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "send prompt",
                        tint = if (isThinking) Color.Gray else Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(text: String, isAi: Boolean) {
    val alignSelf = if (isAi) Alignment.Start else Alignment.End
    val baseColor = if (isAi) Color(0xFF141422) else Color(0xFF0F1E29)
    val borderStroke = if (isAi) BorderStroke(1.dp, NeonPurple.copy(alpha = 0.5f)) else BorderStroke(1.dp, NeonBlue.copy(alpha = 0.5f))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isAi) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = if (isAi) 0.dp else 10.dp,
                        bottomEnd = if (isAi) 10.dp else 0.dp
                    )
                )
                .background(baseColor)
                .border(borderStroke, shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomStart = if (isAi) 0.dp else 10.dp,
                    bottomEnd = if (isAi) 10.dp else 0.dp
                ))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = if (isAi) "ZYRATHON ASSISTANT CORE" else "USER METRICS LOG",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (isAi) NeonPurple else NeonBlue,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = text,
                    color = TextWhite,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ThinkingBubbleItem() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 240.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 10.dp))
                .background(Color(0xFF110D1B))
                .border(1.dp, Color.Yellow.copy(alpha = 0.5f), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 10.dp))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "COGNITIVE NODE SECURING RESPONSE...",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
            }
        }
    }
}
