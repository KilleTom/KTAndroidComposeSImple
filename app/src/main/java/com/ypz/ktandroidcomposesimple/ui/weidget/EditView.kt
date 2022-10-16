package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.ypz.ktandroidcomposesimple.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginNameEditView(
    text: String,
    labelText: String,
    hintText: String?,
    onValueChanged: (String) -> Unit,
    onRightIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    controlColor:Color = MaterialTheme.colorScheme.onPrimary
) {
    BasicLabelEditView(
        modifier = modifier,
        text = text,
        labelText = labelText,
        labelTextColor = controlColor,
        hintText = hintText ?: "",
        onValueChanged = { onValueChanged.invoke(it) },
        rightIconColor = controlColor,
        onRightIconClick = { onRightIconClick.invoke() },
        inputCursorColor = controlColor,
        inputTextColor = controlColor,
        borderColor = controlColor,
        hintTextColor = controlColor,
        keyboardType = KeyboardType.Text,
        labelTextStyle = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic,textAlign = TextAlign.Justify)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPasswordEditView(
    text: String,
    labelText: String,
    hintText: String?,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    controlColor:Color = MaterialTheme.colorScheme.onPrimary
) {
    val showPasswordStatus = remember { mutableStateOf(true) }
    BasicLabelEditView(
        modifier = modifier,
        text = text,
        labelText = labelText,
        labelTextColor = controlColor,
        hintText = hintText ?: "",
        onValueChanged = { onValueChanged.invoke(it) },
        rightIconPainter =
        if (!showPasswordStatus.value) painterResource(id = R.drawable.ic_seen_normal)
        else painterResource(id = R.drawable.ic_unseen_normal),
        rightIconColor = controlColor,
        onRightIconClick = {
            val lastValue = showPasswordStatus.value
            showPasswordStatus.value = !lastValue
        },
        inputCursorColor = controlColor,
        inputTextColor = controlColor,
        borderColor = controlColor,
        hintTextColor = controlColor,
        keyboardType =  KeyboardType.Password,
        visualTransformation =
        if (showPasswordStatus.value) PasswordVisualTransformation()
        else VisualTransformation.None,
        labelTextStyle = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic,textAlign = TextAlign.Justify)
    )
}

@Composable
fun LabelEditView(
    text: String,
    labelText: String,
    hintText: String?,
    onValueChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicLabelEditView(
        modifier = modifier,
        text = text,
        labelText = labelText,
        hintText = hintText ?: "",
        onValueChanged = { onValueChanged.invoke(it) },
        onRightIconClick = { onDeleteClick.invoke() }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BasicLabelEditView(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    labelTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    hintText: String = "",
    hintTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    rightIcon: ImageVector = Icons.Default.Clear,
    rightIconColor: Color = MaterialTheme.colorScheme.onPrimary,
    onValueChanged: (String) -> Unit,
    onRightIconClick: () -> Unit,
    inputCursorColor: Color = MaterialTheme.colorScheme.onPrimary,
    inputTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = MaterialTheme.colorScheme.onSecondary,
    keyboardType: KeyboardType = KeyboardType.Text,
    isHideKeyboard: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelTextStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val keyboardService = LocalTextInputService.current

    TextField(
        visualTransformation = visualTransformation,
        value = text,
        onValueChange = { onValueChanged(it) },
        textStyle = TextStyle(lineHeight = 24.sp, fontSize = 16.sp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .pointerInteropFilter { false },
        label = {
            MediumTitle(
                labelText,
                color = labelTextColor,
                modifier = Modifier.padding(bottom = 10.dp),
                textStyle = labelTextStyle
            )
        },
        placeholder = {
            TextContent(hintText, color = hintTextColor, modifier = Modifier.padding(top = 5.dp))
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = rightIconColor,
                    modifier = Modifier.clickable { onRightIconClick() }
                )
            }
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            textColor = inputTextColor,
            placeholderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = inputCursorColor
        ),
        keyboardActions = if (!isHideKeyboard) KeyboardActions.Default
        else KeyboardActions { keyboardService?.hideSoftwareKeyboard() },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BasicLabelEditView(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    labelTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    hintText: String = "",
    hintTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    rightIconPainter: Painter ,
    rightIconColor: Color = MaterialTheme.colorScheme.onPrimary,
    onValueChanged: (String) -> Unit,
    onRightIconClick: () -> Unit,
    inputCursorColor: Color = MaterialTheme.colorScheme.onPrimary,
    inputTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = MaterialTheme.colorScheme.onSecondary,
    keyboardType: KeyboardType = KeyboardType.Text,
    isHideKeyboard: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelTextStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val keyboardService = LocalTextInputService.current

    TextField(
        visualTransformation = visualTransformation,
        value = text,
        onValueChange = { onValueChanged(it) },
        textStyle = TextStyle(lineHeight = 24.sp, fontSize = 16.sp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .pointerInteropFilter { false },
        label = {
            MediumTitle(
                labelText,
                color = labelTextColor,
                modifier = Modifier.padding(bottom = 10.dp),
                textStyle = labelTextStyle
            )
        },
        placeholder = {
            TextContent(hintText, color = hintTextColor, modifier = Modifier.padding(top = 5.dp))
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                Icon(
                    painter = rightIconPainter,
                    contentDescription = null,
                    tint = rightIconColor,
                    modifier = Modifier.clickable { onRightIconClick() }
                )
            }
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            textColor = inputTextColor,
            placeholderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = inputCursorColor
        ),
        keyboardActions = if (!isHideKeyboard) KeyboardActions.Default
        else KeyboardActions { keyboardService?.hideSoftwareKeyboard() },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        )
    )
}
