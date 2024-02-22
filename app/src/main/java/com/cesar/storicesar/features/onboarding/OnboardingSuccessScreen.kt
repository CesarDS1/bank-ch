package com.cesar.storicesar.features.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.storicesar.R
import com.cesar.storicesar.entities.User

@Composable
fun OnboardingSuccessScreen(user: User, onFinish: () -> Unit) {

    BackHandler {
        onFinish()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.welcome),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "${user.name} ${user.surName}",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = Color.Green,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        Button(onClick = onFinish, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)) {
            Text(text = stringResource(id = R.string.go_to_login))
        }
    }
}