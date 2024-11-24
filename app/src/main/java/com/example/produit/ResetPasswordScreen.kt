package com.example.produit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.produit.ui.theme.PRODUITTheme


class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRODUITTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResetPasswordScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ResetPasswordScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF6200EE)
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isEmpty()) {
                    message = "Please enter your email."
                } else {
                    // Simulate email being sent
                    message = "A password reset link has been sent to $email."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Send Reset Link", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.startsWith("A password reset")) Color.Green else Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    PRODUITTheme  {
        ResetPasswordScreen()
    }
}
