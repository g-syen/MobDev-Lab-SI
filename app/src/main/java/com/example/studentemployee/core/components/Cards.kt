package com.example.studentemployee.core.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentemployee.features.leadership.model.Leader
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.studentemployee.R
import com.example.studentemployee.core.model.BottomNavItem
import com.example.studentemployee.features.devotion.model.Devotion
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.features.facilities.model.Facilities
import com.example.studentemployee.features.members.model.Anggota
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.features.members.model.Kelompok
import com.example.studentemployee.features.members.model.StudentEmployee
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.features.teaching.model.Teaching
import com.example.studentemployee.features.members.model.User
import com.example.studentemployee.features.profile.ui.CustomButton
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMenu(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF19253F)
        ),
        title = {
            Text(
                text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
    )
}

@Preview
@Composable
private fun TopAppBarMenuPreview() {
    TopAppBarMenu(onClick = {}, text = "Profil Lab Sistem Informasi")
}

@Composable
fun RoundedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(24.dp) // Tambahkan padding agar konten tidak menempel

    ) {
        content()
    }
}

@Preview
@Composable
private fun RoundedCardPreview() {
    RoundedCard { Text("RoundedCard") }
}


@Composable
fun DropDownProfile(title: String, description: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color(0xffF37619),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xffF37619)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color(0xff195693),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropDownProfilePreview() {
    DropDownProfile(
        title = "Dropdown Title",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed venenatis, nunc eu consequat egestas, dui erat hendrerit justo, et fringilla nisl dui a turpis. Mauris tortor libero, semper at tortor at, tincidunt porta ipsum. Curabitur ac porta tellus."
    )
}

@Composable
fun LeadershipCard(modifier: Modifier = Modifier, leader: Leader, navController: NavController) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            onClickViewPersonal(leader.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    leader.position,
                    fontSize = 14.sp,
                    color = Color(0xffF37619),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    leader.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff195693)
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xffF37619)
            )
        }
    }
}

@Composable
fun LeadershipAdminCard(
    modifier: Modifier = Modifier,
    leader: Leader,
    navController: NavController,
    onClickEdit: (Leader) -> Unit,
    onClickDelete: (Leader) -> Unit
) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            onClickViewPersonal(leader.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    leader.position,
                    fontSize = 14.sp,
                    color = Color(0xffF37619),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    leader.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff195693)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onClickEdit(leader) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = { onClickDelete(leader) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun MemberCard(modifier: Modifier = Modifier, member: User, navController: NavController) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            onClickViewPersonal(member.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                member.nama,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xffF37619)
            )
        }
    }
}

@Composable
fun StudentEmployeeCard(studemp: StudentEmployee, navController: NavController) {

    fun onClickViewDivision(yearBatch: String) {
        navController.navigate("divisi/${yearBatch}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            onClickViewDivision("${studemp.year}${studemp.batch}")
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Student Employee ${studemp.year} (Batch ${studemp.batch})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xffF37619)
            )
        }
    }
}

@Composable
fun StudentEmployeeCardAdmin(
    studemp: StudentEmployee,
    navController: NavController,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun onClickViewDivisionAdmin(yearBatch: String) {
        navController.navigate("divisiadmin/${yearBatch}")
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete Student Employee ${studemp.year} (Batch ${studemp.batch})?") },
            confirmButton = {
                TextButton(onClick = {
                    studemp.id?.let { onDelete(it) }
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    RoundedCard(
        modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
            onClickViewDivisionAdmin("${studemp.year}${studemp.batch}")
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Student Employee ${studemp.year} (Batch ${studemp.batch})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { studemp.id?.let { onEdit(it) } }) {
                Icon(Icons.Filled.Edit, "Edit")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Filled.Delete, "Delete")
            }
        }
    }
}

@Composable
fun DivisiCard(divisi: Divisi, studemp: StudentEmployee, navController: NavController) {

    fun onClickViewKelompok(studemp: String, divisi: String) {
        navController.navigate("kelompok/${studemp},${divisi}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            divisi.id?.let { studemp.id?.let { it1 -> onClickViewKelompok(it1, it) } }
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            divisi.divisi?.let {
                Text(
                    it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff195693),
                    modifier = Modifier.weight(1f)
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xffF37619)
            )
        }
    }
}

@Composable
fun DivisiAdminCard(
    divisi: Divisi,
    studemp: StudentEmployee,
    onEdit: (Divisi) -> Unit,
    onDelete: (Divisi) -> Unit,
    navController: NavController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun onClickViewKelompokAdmin(studemp: String, divisi: String) {
        navController.navigate("kelompokAdmin/${studemp}/${divisi}")
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete Divisi '${divisi.divisi ?: "N/A"}'?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(divisi)
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    RoundedCard(
        modifier = Modifier.fillMaxWidth().height(80.dp).clickable {
            divisi.id?.let { studemp.id?.let { it1 -> onClickViewKelompokAdmin(studemp = it1, divisi = it) } }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            Text(
                text = divisi.divisi ?: "Unnamed Division",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEdit(divisi) }) {
                Icon(Icons.Filled.Edit, "Edit Divisi")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Filled.Delete, "Delete Divisi")
            }
        }
    }
}

@Composable
fun KelompokAdminCard(
    kelompok: Kelompok,
    onEdit: (Kelompok) -> Unit,
    onDelete: (Kelompok) -> Unit,
    onManageAnggota: (Kelompok) -> Unit // For future Anggota management
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete Kelompok ${kelompok.kelompok?.toInt() ?: "N/A"}'?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(kelompok)
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    RoundedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "Kelompok ${kelompok.kelompok?.toInt() ?: "N/A"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            // Optional: Button to manage Anggota
            // IconButton(onClick = { onManageAnggota(kelompok) }) {
            //     Icon(Icons.Filled.People, "Manage Anggota", tint = Color.Gray)
            // }
            IconButton(onClick = { onEdit(kelompok) }) {
                Icon(Icons.Filled.Edit, "Edit Kelompok", tint = Color(0xFFE2640D))
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Filled.Delete, "Delete Kelompok", tint = Color.Red)
            }
        }
    }
}

@Composable
fun AnggotaAdminCard(
    anggota: Anggota,
    onEdit: (Anggota) -> Unit,
    onDelete: (Anggota) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete Anggota '${anggota.nama ?: "N/A"}' (NIM: ${anggota.nim ?: "N/A"})?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(anggota)
                    showDeleteDialog = false
                }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }

    var initials: String? = null
    if(anggota.nama!= null) {
        initials = anggota.nama
            .split(" ")
            .filter { it.isNotBlank() && it.length > 0 }
            .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
            .take(2)
            .joinToString("")
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 4.dp),
        backgroundColor = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0XFFEAEAEA)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (initials != null) {
                        Text(
                            initials,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0XFF195693)
                        )
                    }
                }
                Text(
                    text = anggota.nama ?: "No Name",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "NIM: ${anggota.nim ?: "N/A"}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { onEdit(anggota) }) {
                Icon(Icons.Filled.Edit, "Edit Anggota", tint = Color(0xFF007BFF)) // Blue for edit
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Filled.Delete, "Delete Anggota", tint = Color.Red)
            }
        }
    }
}


@Composable
fun KelompokCard(kelompok: Kelompok) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(48.dp).background(Color(0xFF048DC8)), contentAlignment = Alignment.Center) {
            Text(text = "Kelompok ${kelompok.kelompok?.toInt().toString()}" ,textAlign = TextAlign.Center, style = TextStyle(fontSize = 16.sp), color = Color.White)
        }
        kelompok.anggota.forEach { anggota ->
            var initials: String? = null
            if(anggota.nama!= null) {
                initials = anggota.nama
                    .split(" ")
                    .filter { it.isNotBlank() && it.length > 0 }
                    .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
                    .take(2)
                    .joinToString("")
            }

            Row (
              modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0XFFEAEAEA)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (initials != null) {
                        Text(
                            initials,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0XFF195693)
                        )
                    }
                }
                Column (
                    modifier = Modifier.padding(10.dp)
                ){
                    anggota.nama?.let { Text(text = it, textAlign = TextAlign.Left, color = Color(0xFF195693), fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                    anggota.nim?.let { Text (text = it, textAlign = TextAlign.Left, fontSize = 16.sp)}
                }
            }
            Divider(thickness = 1.dp, color = Color(0xFF195693))
        }
    }
}

@Composable
fun MemberAdminCard(
    modifier: Modifier = Modifier,
    member: User,
    navController: NavController,
    onClickEdit: (User) -> Unit,
    onClickDelete: (User) -> Unit
) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard(
        modifier = Modifier.clickable {
            onClickViewPersonal(member.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                member.nama,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onClickEdit(member) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = { onClickDelete(member) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun LeadershipCardPreview() {
    LeadershipCard(
        leader = Leader(
            id = "1",
            name = "Riswan Septriayadi Sianturi, S.Si.., MM., M.Sc., Ph.D.",
            position = "Kepala Lab"
        ), navController = rememberNavController()
    )
}

@Composable
fun ImageCarousel(images: List<Facilities>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.size }
    )
    val context = LocalContext.current

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val facility = images[page]

        AsyncImage(
            model = facility.imageUrl,
            contentDescription = "Carousel Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facility.link))
                    context.startActivity(intent)
                },
            contentScale = ContentScale.Crop,

            )
    }
}

@Preview
@Composable
fun ImageCarouselPreview() {
    ImageCarousel(emptyList())
}

@Composable
fun FacilitiesAdminCard(
    facilities: Facilities,
    onDelete: (Facilities) -> Unit,
    onEdit: (Facilities) -> Unit
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (facilities.link.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facilities.link))
                    context.startActivity(intent)
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = facilities.imageUrl,
                    contentDescription = "Facility Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = facilities.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF195693),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onEdit(facilities) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton(
                    onClick = { onDelete(facilities) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "homepageguest"),
        BottomNavItem("Search", Icons.Default.Search, "searchguest"),
        BottomNavItem("Publikasi", Icons.AutoMirrored.Filled.MenuBook, "content")
    )

    NavigationBar(containerColor = Color(0xFF19253F)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { androidx.compose.material3.Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color(0xFF394E89)
                )
            )
        }
    }
}

@Composable
fun BottomNavBarMember(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "homepagemember"),
        BottomNavItem("Dashboard", Icons.Default.Widgets, "menumember"),
        BottomNavItem("Search", Icons.Default.Search, "searchmember"),
        BottomNavItem("Publikasi", Icons.AutoMirrored.Filled.MenuBook, "content"),
        BottomNavItem("Profil", Icons.Outlined.AccountBox, "profile")
    )

    NavigationBar(containerColor = Color(0xFF19253F)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { androidx.compose.material3.Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color(0xFF394E89)
                )
            )
        }
    }
}

@Composable
fun BottomNavBarAdmin(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "homepageadmin"),
        BottomNavItem("Dashboard", Icons.Default.Widgets, "menuadmin"),
        BottomNavItem("Search", Icons.Default.Search, "searchadmin"),
        BottomNavItem("Publikasi", Icons.AutoMirrored.Filled.MenuBook, "content"),
    )

    NavigationBar(containerColor = Color(0xFF19253F)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { androidx.compose.material3.Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color(0xFF394E89)
                )
            )
        }
    }
}


@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    onClickButton: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.size(54.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF048dc8)),
        onClick = { onClickButton() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White
            )
            androidx.compose.material3.Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    axis: Boolean,
    editDelete: Boolean = false,
    onEditEvent: () -> Unit = {},
    onDeleteEvent: () -> Unit = {}
) {
    val context = LocalContext.current
    if (axis)
        ElevatedCard(
            modifier = Modifier
                .width(180.dp)
                .height(220.dp)
                .padding(8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Image size
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(8.dp)) {
                    androidx.compose.material3.Text(
                        text = event.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    androidx.compose.material3.Text(
                        text = event.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    androidx.compose.material3.Text(
                        text = event.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    else
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(8.dp)) {
                    Row {
                        androidx.compose.material3.Text(
                            text = event.date + "\t\t-",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2640D),
                            fontWeight = FontWeight.Bold
                        )
                        androidx.compose.material3.Text(
                            text = event.time,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2640D),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF195693),
                            modifier = Modifier.weight(1f)
                        )

                        if (editDelete) {
                            Row {
                                IconButton(onClick = onEditEvent) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Edit Event",
                                        tint = Color(0xFF426193)
                                    )
                                }
                                IconButton(onClick = onDeleteEvent) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete Event",
                                        tint = Color(0xFFE55304)
                                    )
                                }
                            }
                        }
                    }

                }


            }
        }
}

@Composable
fun NewsCard(
    news: News,
    axis: Boolean,
    editDelete: Boolean = false,
    onEditNews: () -> Unit = {},
    onDeleteNews: () -> Unit = {}
) {
    val context = LocalContext.current

    if (axis)
        ElevatedCard(
            modifier = Modifier
                .width(180.dp)
                .height(200.dp)
                .padding(8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column {
                AsyncImage(
                    model = news.imageUrl,
                    contentDescription = "News Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Image size
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(8.dp)) {
                    androidx.compose.material3.Text(
                        text = news.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    androidx.compose.material3.Text(
                        text = news.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    else
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row {
                AsyncImage(
                    model = news.imageUrl,
                    contentDescription = "News Image",
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = news.title,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = news.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    if (editDelete) {
                        Row {
                            IconButton(onClick = onEditNews) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit News",
                                    tint = Color(0xFF426193)
                                )
                            }
                            IconButton(onClick = onDeleteNews) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete News",
                                    tint = Color(0xFFE55304)
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                            context.startActivity(intent)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForwardIos,
                                contentDescription = "Open News",
                                tint = Color(0xFF426193)
                            )
                        }
                    }
                }

            }
        }
}

@Composable
fun TeachingCard(
    teaching: Teaching,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.material3.Text(
                text = teaching.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = "Semester " + teaching.semester + " - " + teaching.year,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ResearchSmallCard(
    research: Research,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(research.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.material3.Text(
                text = research.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = research.authors,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ResearchCard(
    research: Research,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(research.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.material3.Text(
                text = research.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = research.authors,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DevotionCard(
    devotion: Devotion,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(devotion.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.material3.Text(
                text = devotion.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = devotion.contributors,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FacilitiesCard(
    facilities: Facilities
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (facilities.link.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facilities.link))
                    context.startActivity(intent)
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = facilities.imageUrl,
                contentDescription = "Facility Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material3.Text(
                text = facilities.title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF195693),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TabSection(selectedTab: String, onTabSelected: (String) -> Unit) {

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                listOf("Penelitian", "Pengabdian", "Pengajaran").forEach { tab ->
                    Button(
                        onClick = { onTabSelected(tab) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == tab) Color(0xFFFF6A00) else Color(
                                0xFF426193
                            )
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PenelitianContent(
    researches: List<Research>,
    kelolaPenelitian: String = "",
    navController: NavController? = null
) {
    if(kelolaPenelitian.isNotBlank() || kelolaPenelitian.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            CustomButton(
                onClick = { navController?.navigate(kelolaPenelitian) },
                text = "Kelola Penelitian"
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        if (researches.isEmpty()) {
            Text("Penelitian tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(researches) { research ->
                    ResearchCard(
                        research, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun PengajaranContent(
    teachings: List<Teaching>,
    kelolaPengajaran: String = "",
    navController: NavController? = null
) {
    if(kelolaPengajaran.isNotBlank() || kelolaPengajaran.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            CustomButton(
                onClick = { navController?.navigate(kelolaPengajaran) },
                text = "Kelola Pengajaran"
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        if (teachings.isEmpty()) {
            Text("Pengajaran tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(teachings) { teaching ->
                    TeachingCard(
                        teaching, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PengabdianContent(
    devotions: List<Devotion>,
    kelolaPengabdian: String = "",
    navController: NavController? = null
) {
    if(kelolaPengabdian.isNotBlank() || kelolaPengabdian.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            CustomButton(
                onClick = { navController?.navigate(kelolaPengabdian) },
                text = "Kelola Pengabdian"
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        if (devotions.isEmpty()) {
            Text("Pengabdian tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(devotions) { devotions ->
                    DevotionCard(
                        devotions, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CardMenu(modifier: Modifier = Modifier, text: String, icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(10.dp) // Tambahkan padding agar konten tidak menempel
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = icon,
                    contentDescription = "Icon $text",
                    tint = Color(0xff093376)
                )
                Spacer(Modifier.width(10.dp))
                androidx.compose.material3.Text(
                    text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xff093376)
                )
            }
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Icon Arrow Right",
                tint = Color(0xff093376)
            )

        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Logout", color = Color(0xFFF37619), fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to log out?", color = Color.Black) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF048dc8))
            ) {
                Text("Yes", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Cancel", color = Color.Black)
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun DatePickerDialogExample(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerDialog,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year, month, day
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = {
                Text(if (selectedDate.isEmpty()) "Select Date" else "Date")
            },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Pick date"
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF10375E),
                cursorColor = Color.Transparent,
                disabledTextColor = Color.Black
            )
        )
    }
}

@Composable
fun TimePickerDialogExample(selectedTime: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.CustomTimePickerDialog,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { timePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            label = {
                Text(if (selectedTime.isEmpty()) "Select Time" else "Time")
            },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Pick time"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFF10375E),
                cursorColor = Color.Transparent,
                disabledTextColor = Color.Black
            )
        )
    }
}




