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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ams.Navigation.Screen
import com.example.ams.R
import com.example.ams.data.ViewModel.FirebaseViewModel
import com.example.ams.ui.theme.pri
import com.example.ams.ui.theme.sec
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun ListOfCoursePage(
    navHostController: NavHostController,
    viewModel: FirebaseViewModel
) {

    val listOfClasses by viewModel.courseNames.observeAsState(initial = emptyList())
    var isDropDownVisible by remember { mutableStateOf(false) }
    val quickSand = FontFamily(Font(R.font.quicksand_medium))

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = pri)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = pri
        ) {
            Text(
                text = "AMS",
                fontFamily = quickSand,
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(0.5f))

            Button(
                onClick = { isDropDownVisible = !isDropDownVisible },
                colors = ButtonDefaults.buttonColors(backgroundColor = sec),
                modifier = Modifier
                    .height(30.dp)
                    .width(100.dp)
                    .padding(end = 15.dp) ,
                contentPadding = PaddingValues(0.dp)
            ) {

                Image(painter = painterResource(id = R.drawable.add_icon_white), contentDescription = "")

                Text(text = "ADD", color = Color.White, fontFamily = quickSand)

                DropdownMenu(
                    expanded = isDropDownVisible,
                    onDismissRequest = { isDropDownVisible = false },
                    modifier = Modifier
                        .width(150.dp)
                        .height(85.dp)
                        .background(sec)
                ) {
//                    TextButton(onClick = { navHostController.navigate(Screen.NewCourse.route )}) {
                        Row(
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp)
                                .padding(start = 20.dp, top = 5.dp)
                                .clickable { navHostController.navigate(Screen.NewCourse.route) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.create_new_white),
                                contentDescription = "",
                                tint = Color.White
                            )
                            Text(text = "  Create", color = Color.White, fontFamily = quickSand)
                        }
//                    }
//                    TextButton(onClick = { navHostController.navigate(Screen.ImportCourse.route) }) {
                        Row(
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp)
                                .padding(start = 20.dp, bottom = 5.dp)
                                .clickable { navHostController.navigate(Screen.ImportCourse.route) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.import_icon),
                                contentDescription = "",
                                tint = Color.White
                            )
                            Text(text = "  Import", color = Color.White, fontFamily = quickSand)
                        }
//                    }
                }
            }

        }
        if (!listOfClasses.isEmpty()) {
            LazyColumn() {
               items(items = listOfClasses) { name ->
                   ClassItemModel(name.first, onClick = { navHostController.navigate(Screen.ViewCourse.passCourseName(name.first, name.second))}, icon = painterResource(
                       id = R.drawable.info_white
                   ))
               }
            }
        }
    }
}

@Composable
fun ClassItemModel(name: String, icon: Painter , onClick: () -> Unit) {
    val quicksand = FontFamily(Font(R.font.quicksand_medium))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(10))
            .background(Color(0xFF2196F3))
            .clickable { onClick() }
    ) {
//        Image(painter = icon, contentDescription = "")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    fontFamily = quicksand,
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Text(
                    text = name,
                    fontFamily = quicksand,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_forwward_white),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.padding(end = 15.dp).size(25.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = name,
            fontFamily = quicksand,
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 10.dp, start = 20.dp)
        )
    }
}