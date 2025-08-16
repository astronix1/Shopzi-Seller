package com.example.shopziseller.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.shopziseller.model.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun UpdateProduct(modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(Unit) {
        try {
            val snapshot = db.collection("data")
                .document("stock")
                .collection("products")
                .get()
                .await()

            products = snapshot.documents.map { doc ->
                ProductModel(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    price = doc.getString("price") ?: "",
                    actualPrice = doc.getString("actualPrice") ?: "",
                    category = doc.getString("category") ?: "",
                    images = doc.get("images") as? List<String> ?: emptyList()
                )
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching products", e)
        }
    }

    if (selectedProduct == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F4F8))
        ) {
            // Header
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Products Dashboard",
                fontSize = 30.sp,
                color = Color(0xFF0D47A1),
                modifier = Modifier
                    .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(


                    top = 12.dp,
                    bottom = 40.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProduct = product },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if (product.images.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(product.images.first()),
                                    contentDescription = product.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 16.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Placeholder for image
                            else {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Color(0xFFBBDEFB), RoundedCornerShape(12.dp))
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = product.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color(0xFF0D47A1),
                                        fontSize = 20.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Category: ${product.category}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Price: ₹${product.price}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color(0xFF43A047)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        EditProductScreen(product = selectedProduct!!, onBack = { selectedProduct = null }) { updated ->
            db.collection("data")
                .document("stock")
                .collection("products")
                .document(updated.id)
                .set(updated)
                .addOnSuccessListener {
                    products = products.map { if (it.id == updated.id) updated else it }
                }
                .addOnFailureListener { Log.e("Firestore", "Error updating product", it) }
            selectedProduct = null
        }
    }
}

@Composable
fun EditProductScreen(
    product: ProductModel,
    onBack: () -> Unit,
    onSave: (ProductModel) -> Unit
) {
    var title by remember { mutableStateOf(product.title) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price) }
    var actualPrice by remember { mutableStateOf(product.actualPrice) }
    var category by remember { mutableStateOf(product.category) }

    // Scrollable Column for smaller screens
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Edit Product",
            fontSize = 28.sp,
            color = Color(0xFF0D47A1)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = actualPrice,
                    onValueChange = { actualPrice = it },
                    label = { Text("Actual Price") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", color = Color.Black)
            }

            Button(
                onClick = {
                    onSave(
                        ProductModel(
                            id = product.id,
                            title = title,
                            description = description,
                            price = price,
                            actualPrice = actualPrice,
                            category = category,
                            images = product.images
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}
