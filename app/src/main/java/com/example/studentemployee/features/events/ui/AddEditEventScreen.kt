package com.example.studentemployee.features.events.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.core.components.DatePickerDialogExample
import com.example.studentemployee.core.components.EventCard
import com.example.studentemployee.core.components.TimePickerDialogExample
import com.example.studentemployee.features.events.EventViewModel
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.features.profile.ui.CustomButton
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    eventId: String?,
    viewModel: EventViewModel = viewModel()
) {
    var eventTitle by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var imageUrl by remember {  mutableStateOf("")}
    var link by remember { mutableStateOf("") }
    var currentEventId by remember { mutableStateOf(eventId) }

    var titleError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var imageUrlError by remember { mutableStateOf(false) }
    var linkError by remember { mutableStateOf(false) }

    var isEdit by remember {mutableStateOf(false)}
    var eventsList by remember { mutableStateOf(emptyList<Event>()) }

    val context = LocalContext.current

    fun fetchEvents() {
        firestore.collection("events")
            .get()
            .addOnSuccessListener { result ->
                eventsList = result.documents.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.apply { id = doc.id } }
            }
    }

    LaunchedEffect(currentEventId) {
        if(currentEventId != null) {
            firestore.collection("events").document(currentEventId!!).get()
                .addOnSuccessListener { document ->
                    document?.data?.let { data ->
                        eventTitle = data["title"] as String
                        selectedDate = data["date"] as String
                        selectedTime = data["time"] as String
                        imageUrl = data["imageUrl"] as String
                        link = data["link"] as String
                        isEdit = true
                    }
                }
        } else {
            isEdit = false
        }

        fetchEvents()
    }

    fun clearField() {
        eventTitle = ""
        selectedDate = ""
        selectedTime = ""
        imageUrl = ""
        link = ""
        currentEventId = null
        isEdit = false

        eventsList = emptyList()
        fetchEvents()
    }



    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text( if(isEdit) {
                        "Edit Event"
                    } else {
                        "Add Event"
                    }, color = Color.White)
                }, navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F))
            )
        }, containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.Top
                ){
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tambahkan Event Baru",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Masukkan Nama Event",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    var isFocusedName by remember { mutableStateOf(false) }

                    TextField(
                        value = eventTitle,
                        onValueChange = { eventTitle = it },
                        label = {
                            Text(
                                text = "Event Title",
                                color = if (eventTitle.isNotEmpty() || isFocusedName) Color.Transparent else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                            .onFocusChanged { isFocusedName = it.isFocused },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Masukkan URL Gambar Event",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    var isFocusedImage by remember { mutableStateOf(false) }

                    TextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = {
                            Text(
                                text = "Image URL",
                                color = if (imageUrl.isNotEmpty() || isFocusedImage) Color.Transparent else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                            .onFocusChanged { isFocusedImage = it.isFocused },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Masukkan Link Website Detail Event",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    var isFocusedLink by remember { mutableStateOf(false) }

                    TextField(
                        value = link,
                        onValueChange = { link = it },
                        label = {
                            Text(
                                text = "Event Website Link",
                                color = if (link.isNotEmpty() || isFocusedLink) Color.Transparent else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                            .onFocusChanged { isFocusedLink = it.isFocused },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Masukkan Tanggal Event",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    DatePickerDialogExample(selectedDate) { selectedDate = it }

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Masukkan Jam Event (WIB)",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    TimePickerDialogExample(selectedTime) { selectedTime = it }

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomButton(
                        onClick = {
                            titleError = eventTitle.isBlank()
                            dateError = selectedDate.isBlank()
                            timeError = selectedTime.isBlank()
                            linkError = link.isBlank() || !isValidUrl(link)
                            imageUrlError =
                                imageUrl.isBlank() || !isValidUrl(imageUrl)

                            if (titleError || dateError || timeError || linkError || imageUrlError) {
                                Toast.makeText(
                                    context,
                                    "Mohon lengkapi semua field dengan benar.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@CustomButton
                            }
                            if (!isEdit) {
                                //add new event
                                val newEvent = Event(
                                    title = eventTitle,
                                    date = selectedDate,
                                    time = selectedTime,
                                    link = link,
                                    imageUrl = imageUrl
                                )
                                viewModel.addEvent(
                                    newEvent,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Berhasil disimpan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        clearField()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Gagal: ${it.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                val updatedEvent = Event(
                                    id = currentEventId ?: "",
                                    title = eventTitle,
                                    date = selectedDate,
                                    time = selectedTime,
                                    link = link,
                                    imageUrl = imageUrl
                                )
                                viewModel.updateEvent(
                                    event = updatedEvent,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Berhasil diupdate",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        clearField()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Gagal: ${it.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        },
                        text = if (isEdit) "Edit Event" else "Simpan Event"
                    )
                }

            }

            Text(
                "Event yang telah diunggah",
                modifier = Modifier.padding(start = 24.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFFF37619)
            )

            Box(
                modifier = Modifier.height(400.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn {
                    items(eventsList) { result ->
                        EventCard(
                            event = result,
                            axis = false,
                            editDelete = true,
                            onEditEvent = {
                                currentEventId = result.id
                                isEdit = true
                            },
                            onDeleteEvent = {
                                viewModel.deleteEvent(
                                    eventId = result.id,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Berhasil diupdate",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        clearField()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Gagal: ${it.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )
                    }
                }
            }

        }
    }
}

