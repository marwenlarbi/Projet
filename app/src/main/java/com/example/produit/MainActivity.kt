package com.example.produit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.produit.ui.theme.PRODUITTheme
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRODUITTheme {
                ProductListScreen()
            }
        }
    }
}

@Composable
fun ProductListScreen() {
    val context = LocalContext.current
    val dbHelper = SQLiteHelper(context)

    // Liste des produits depuis la base de données
    var products by remember { mutableStateOf(dbHelper.getProducts()) }
    var isAddingProduct by remember { mutableStateOf(false) }

    // Variables pour ajouter un nouveau produit
    var newName by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Liste des produits
        if (products.isEmpty()) {
            Text("No products available", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onDelete = { productId ->
                            dbHelper.deleteProduct(productId)
                            products = dbHelper.getProducts() // Recharger après suppression
                        },
                        onUpdate = { updatedProduct ->
                            dbHelper.updateProduct(updatedProduct)
                            products = dbHelper.getProducts() // Recharger après mise à jour
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ajouter un produit
        if (isAddingProduct) {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = newPrice,
                onValueChange = { newPrice = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = newDescription,
                onValueChange = { newDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = {
                    if (newName.isNotEmpty() && newPrice.isNotEmpty() && newDescription.isNotEmpty()) {
                        val newProduct = Product(
                            name = newName,
                            price = newPrice,
                            description = newDescription
                        )
                        dbHelper.addProduct(newProduct)
                        products = dbHelper.getProducts() // Recharger après ajout
                        newName = ""
                        newPrice = ""
                        newDescription = ""
                        isAddingProduct = false
                    }
                }) {
                    Text("Add Product")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { isAddingProduct = false }) {
                    Text("Cancel")
                }
            }
        } else {
            Button(onClick = { isAddingProduct = true }) {
                Text("Add Product")
            }
        }
    }
}
