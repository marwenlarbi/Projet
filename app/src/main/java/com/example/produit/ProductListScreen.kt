package com.example.produit

import com.example.produit.ui.theme.PRODUITTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRODUITTheme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProductListScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dbHelper = SQLiteHelper(context)
    var products by remember { mutableStateOf(dbHelper.getProducts()) }
    var isAddingProduct by remember { mutableStateOf(false) }

    // Variables pour les champs du produit
    var newName by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Afficher la liste des produits
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onUpdate = { updatedProduct ->
                        dbHelper.updateProduct(updatedProduct)
                        products = dbHelper.getProducts()
                    },
                    onDelete = { id ->
                        dbHelper.deleteProduct(id)
                        products = dbHelper.getProducts()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Afficher un formulaire d'ajout de produit si l'utilisateur souhaite ajouter un nouveau produit
        if (isAddingProduct) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newPrice,
                    onValueChange = { newPrice = it },
                    label = { Text("Product Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Product Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = {
                        if (newName.isNotEmpty() && newPrice.isNotEmpty() && newDescription.isNotEmpty()) {
                            val newProduct = Product(
                                name = newName,
                                price = newPrice,
                                description = newDescription
                            )
                            dbHelper.addProduct(newProduct)
                            products = dbHelper.getProducts()
                            // Réinitialiser les champs après l'ajout
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
            }
        } else {
            // Afficher le bouton "Add Product" pour ajouter un nouveau produit
            Button(
                onClick = { isAddingProduct = true }
            ) {
                Text("Add Product")
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onUpdate: (Product) -> Unit,
    onDelete: (Int) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var updatedName by remember { mutableStateOf(product.name) }
    var updatedPrice by remember { mutableStateOf(product.price) }
    var updatedDescription by remember { mutableStateOf(product.description) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                // Formulaire pour modifier les données du produit
                OutlinedTextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = updatedPrice,
                    onValueChange = { updatedPrice = it },
                    label = { Text("Product Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = updatedDescription,
                    onValueChange = { updatedDescription = it },
                    label = { Text("Product Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Boutons pour sauvegarder ou annuler
                Row {
                    Button(onClick = {
                        // Sauvegarder les modifications
                        onUpdate(
                            product.copy(
                                name = updatedName,
                                price = updatedPrice,
                                description = updatedDescription
                            )
                        )
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                // Affichage du produit
                Text("Name: ${product.name}", style = MaterialTheme.typography.headlineSmall)
                Text("Price: ${product.price}", style = MaterialTheme.typography.bodyLarge)
                Text("Description: ${product.description}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                // Boutons pour modifier ou supprimer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { isEditing = true }) {
                        Text("Update")
                    }
                    Button(onClick = { onDelete(product.id) }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
