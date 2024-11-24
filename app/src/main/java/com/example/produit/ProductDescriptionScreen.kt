package com.example.produit

import com.example.produit.ui.theme.PRODUITTheme
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productDescription = intent.getStringExtra("productDescription")
        Log.d("MainActivity6", "Received product description: $productDescription")

        setContent {
            PRODUITTheme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductDescriptionScreen(
                        modifier = Modifier.padding(innerPadding),
                        description = productDescription ?: "No description available"
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDescriptionScreen(modifier: Modifier = Modifier, description: String) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Product Description",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = description,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDescriptionPreview() {
    PRODUITTheme  {
        ProductDescriptionScreen(description = "Example product description")
    }
}
