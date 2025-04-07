package com.example.studentemployee.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Space
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentemployee.data.Leader
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.studentemployee.data.Article
import com.example.studentemployee.data.BottomNavItem
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Event
import com.example.studentemployee.data.Facilities
import com.example.studentemployee.data.News
import com.example.studentemployee.data.User


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

    RoundedCard (
        modifier = Modifier.clickable {
            onClickViewPersonal(leader.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ){
        Row(verticalAlignment = Alignment.CenterVertically, ) {
            Column(modifier=Modifier.weight(1f)) {
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
    onClickEdit : (Leader) -> Unit,
    onClickDelete : (Leader) -> Unit
) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard (
        modifier = Modifier.clickable {
            onClickViewPersonal(leader.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ){
        Row(verticalAlignment = Alignment.CenterVertically, ) {
            Column(modifier=Modifier.weight(1f)) {
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
                IconButton (onClick = { onClickEdit(leader) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton (onClick = { onClickDelete(leader) },
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

    RoundedCard (
        modifier = Modifier.clickable {
            onClickViewPersonal(member.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ){
        Row(verticalAlignment = Alignment.CenterVertically, ) {
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
fun MemberAdminCard(
    modifier: Modifier = Modifier,
    member: User,
    navController: NavController,
    onClickEdit : (User) -> Unit,
    onClickDelete : (User) -> Unit
) {

    fun onClickViewPersonal(personalId: String) {
        navController.navigate("personalmember/${personalId}")
    }

    RoundedCard (
        modifier = Modifier.clickable {
            onClickViewPersonal(member.id)
            Log.d("RoundedCard", "Card clicked!")
        }

    ){
        Row(verticalAlignment = Alignment.CenterVertically, ) {
            Text(
                member.nama,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff195693),
                modifier = Modifier.weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton (onClick = { onClickEdit(member) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton (onClick = { onClickDelete(member) },
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
){
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
    LeadershipCard(leader = Leader(id = "1", name = "Riswan Septriayadi Sianturi, S.Si.., MM., M.Sc., Ph.D.", position = "Kepala Lab"), navController = rememberNavController())
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
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if(facilities.link.isNotEmpty()){
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
                IconButton (onClick = { onEdit(facilities) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
                IconButton (onClick = { onDelete(facilities) },
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
        BottomNavItem("Search", Icons.Default.Search, "search"),
        BottomNavItem("Konten", Icons.AutoMirrored.Filled.MenuBook, "content")
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
        BottomNavItem("Menu", Icons.Default.Widgets, "menumember"),
        BottomNavItem("Search", Icons.Default.Search, "search"),
        BottomNavItem("Konten", Icons.AutoMirrored.Filled.MenuBook, "content"),
        BottomNavItem("Profil", Icons.Outlined.AccountBox, "profile")
    )

    NavigationBar(containerColor = Color(0xFF19253F)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(imageVector = item.icon, contentDescription = item.label) },
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
        BottomNavItem("Menu", Icons.Default.Widgets, "menu"),
        BottomNavItem("Search", Icons.Default.Search, "search"),
        BottomNavItem("Konten", Icons.AutoMirrored.Filled.MenuBook, "content"),
        BottomNavItem("Profil", Icons.Outlined.AccountBox, "profile")
    )

    NavigationBar(containerColor = Color(0xFF19253F)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(imageVector = item.icon, contentDescription = item.label) },
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
    onClickButton : () -> Unit
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
    event: Event
){
    val context = LocalContext.current
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
}

@Composable
fun NewsCard(
    news: News
){
    val context = LocalContext.current
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
}

@Composable
fun ArticleCard(
    article: Article,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.material3.Text(
                text = article.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = article.authors,
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
){
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
                text = devotion.description,
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
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if(facilities.link.isNotEmpty()){
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
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                listOf("Artikel", "Publikasi", "Pengabdian").forEach { tab ->
                    Button(
                        onClick = { onTabSelected(tab) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == tab) Color(0xFFFF6A00) else Color(0xFF426193)
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
fun ArtikelContent(articles: List<Article>) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    ) {
        if (articles.isEmpty()) {
            Text("Artikel tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(articles) { articles ->
                    ArticleCard(articles, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun PublikasiContent(publications: List<Article>) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    ) {
        if (publications.isEmpty()) {
            Text("Publikasi tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(publications) { publications ->
                    ArticleCard(publications, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun PengabdianContent(devotions: List<Devotion>) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    ) {
        if (devotions.isEmpty()) {
            Text("Pengabdian tidak tersedia", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(devotions) { devotions ->
                    DevotionCard(devotions, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                }
            }
        }
    }
}
