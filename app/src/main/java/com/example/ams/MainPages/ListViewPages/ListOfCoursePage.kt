package com.example.ams.MainPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.Navigation.NEW_COURSE
import com.example.ams.Navigation.Screen
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel


@Composable
fun ListOfCoursePage(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel
) {

    val listOfClasses by viewModel.courseNames.observeAsState(initial = emptyList())
    var isDropDownVisible by remember { mutableStateOf(false) }
    val bungeeStyle = FontFamily(Font(R.font.bungee))
    val addIcon = painterResource(id = R.drawable.add_icon_black)
    val importIcon = painterResource(id = R.drawable.import_icon)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = Color.Black
        ) {
            Text(
                text = "Ams",
                fontFamily = bungeeStyle,
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(0.5f))
            TextButton(onClick = { isDropDownVisible = !isDropDownVisible }) {
                Text(text = "+New Class", color = Color.White, fontFamily = bungeeStyle)
                DropdownMenu(expanded = isDropDownVisible, onDismissRequest = { isDropDownVisible = false }) {
                    TextButton(onClick = { navHostController.navigate(Screen.NewCourse.route )}) {
                        Text(text = "Create", color = Color.White, fontFamily = bungeeStyle)
                    }
                    TextButton(onClick = { navHostController.navigate(Screen.ImportCourse.route) }) {
                        Text(text = "Import", color = Color.White, fontFamily = bungeeStyle)
                    }
                }
            }

        }
        if (listOfClasses.isEmpty()) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { navHostController.navigate(NEW_COURSE) }) {
                    Icon(painter = addIcon, contentDescription = "", modifier = Modifier.size(80.dp))
                }
                Text(text = "Create a new class", fontFamily = bungeeStyle)
                Text(text = "or", fontFamily = bungeeStyle)
                IconButton(
                    onClick = { navHostController.navigate(Screen.ImportCourse.route) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black)
                ) {
                    Image(
                        painter = importIcon,
                        contentDescription = "",
                        modifier = Modifier.size(65.dp)
                    )
                }
                Text(text = "Import an existing class", fontFamily = bungeeStyle)
            }
        }else {
            LazyColumn() {
               items(items = listOfClasses) { name ->
                   ClassItemModel(name.first, onClick = { navHostController.navigate(Screen.ViewCourse.passCourseName(name.first, name.second))})
               }
            }
        }
    }
}


@Composable
fun ClassItemModel(name: String, onClick: () -> Unit) {
    val bungeeStyle = FontFamily(Font(R.font.bungee))

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .size(130.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(20))
            .background(Color.Black)
            .clickable { onClick() }
    ) {
        Text(
            text = name,
            fontFamily = bungeeStyle,
            color = Color.White,
            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
        )
    }
}