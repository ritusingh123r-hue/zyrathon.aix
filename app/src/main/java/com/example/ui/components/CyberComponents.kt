package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.CyberSurfaceCard
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = NeonBlue,
    testTagStr: String = "cyber_btn"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .testTag(testTagStr)
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.horizontalGradient(listOf(glowColor, NeonPurple))
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Blur background glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(8.dp)
                .background(glowColor.copy(alpha = glowAlpha * 0.2f))
        )
        Text(
            text = text.uppercase(),
            color = TextWhite,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    }
}

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NeonBlue,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(listOf(borderColor.copy(alpha = 0.8f), borderColor.copy(alpha = 0.2f)))
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    testTagStr: String = "cyber_field"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label.uppercase(),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
        },
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null, tint = NeonBlue) }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            focusedBorderColor = NeonBlue,
            unfocusedBorderColor = Color(0xFF242435),
            focusedContainerColor = Color(0xFF0F0F16),
            unfocusedContainerColor = Color(0xFF0F0F16)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTagStr),
        singleLine = true
    )
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonBlue
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 20.dp)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun PulsatingCore(
    modifier: Modifier = Modifier,
    coreColor: Color = NeonPurple
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing halo
        Box(
            modifier = Modifier
                .size(100.dp)
                .blur(20.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(coreColor.copy(alpha = pulseScale * 0.3f), Color.Transparent)
                        ),
                        radius = size.minDimension / 1.5f
                    )
                }
        )
        // Solid core
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(BorderStroke(2.dp, NeonBlue), shape = RoundedCornerShape(50))
                .drawBehind {
                    drawCircle(
                        brush = Brush.linearGradient(listOf(coreColor, CyanGlow)),
                        radius = size.minDimension / 2.2f
                    )
                }
        )
    }
}

val CyanGlow = Color(0xFF00F2FE)
