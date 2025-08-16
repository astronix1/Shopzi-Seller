package com.example.shopziseller

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginPage(modifier: Modifier = Modifier, authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val context = LocalContext.current
    var isL = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Great to see you again!",
            modifier.fillMaxWidth(),
            style = TextStyle(
                color = Color.Black,
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif
            )
        )

        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(R.drawable.act),
            contentDescription = "image",
            modifier
                .fillMaxWidth()
                .height(258.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Enter email address", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Enter password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val allowedEmail = "vishal@shopzi.com"

                if (email.value.trim().equals(allowedEmail, ignoreCase = true)) {
                    isL.value = true
                    authViewModel.login(email.value, password.value) { success, errorMsg ->
                        isL.value = false
                        if (success) {
                            GlobalNavigation.navController.navigate("homescreen") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            AppUtil.showToast(context, errorMsg ?: "Something went wrong")
                        }
                    }
                } else {
                    AppUtil.showToast(context, "You are not allowed to login as an admin")
                }
            }
            ,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isL.value
        ) {
            Text(
                text = if (isL.value) "Logging in.." else "Login",
                style = TextStyle(fontSize = 17.sp)
            )
        }
    }
}
