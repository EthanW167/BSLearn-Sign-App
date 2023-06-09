package com.example.composettest.LessonMaker

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.composettest.Domain.model.FLesson
import com.example.composettest.Domain.model.User
import com.example.composettest.Screen

@Composable
fun LessonShareScreen(
    navController: NavController,
    viewModel: LessonShareViewModel = hiltViewModel(),
    lessonId: String
){

    val lessonState = viewModel.lessonState.value
    viewModel.lessonIdGlobal = lessonId

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(72,69,221)
    ){}


    Column(Modifier.fillMaxSize()) {
        topBarShare(navController)
        LessonCard(lesson = lessonState.lesson)
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))) {

            searchUser(viewModel = viewModel)
            Divider(color = Color.Black, modifier = Modifier
                .padding(5.dp)
                .padding(horizontal = 10.dp)
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
            )

            val usersState = viewModel.usersState.value

            val userIdListState = viewModel.usersIdState.value

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(horizontal = 25.dp)){
                items(usersState.Users) { user ->

                    var index = usersState.Users.indexOf(user)
                    UserCard(user = user, userIdListState.userIdList[index])
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: FLesson){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)
        .padding(top = 10.dp)
        .padding(horizontal = 20.dp)
        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
        .height(80.dp)
        .background(Color(238, 238, 255), shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(Modifier.padding()) {
            Text(text = lesson.name, modifier = Modifier
                .padding(bottom = 10.dp), fontSize = 18.sp)
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Row() {
                    Icon(imageVector = Icons.Default.QuestionMark, contentDescription = "Question")
                    Text(text = "${lesson.questions} Questions", modifier = Modifier.padding(horizontal = 5.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row() {
                    Icon(imageVector = Icons.Default.WavingHand, contentDescription = "Signs")
                    Text(text = "+${lesson.signs} Signs to learn", modifier = Modifier.padding(horizontal = 5.dp))
                }

            }
        }
    }
}

@Composable
fun searchUser(viewModel: LessonShareViewModel){
    val context = LocalContext.current
    var searchText by remember {
        mutableStateOf("")
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(top = 15.dp)
        .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Center){
        OutlinedTextField(
            value = searchText,
            onValueChange = {newSearch -> searchText = newSearch},
            placeholder = { Text(
            text = "Add User"
        )})
        Box(modifier = Modifier
            .size(56.dp)
            .border(border = BorderStroke(1.dp, color = Color.LightGray))
            .clickable {
                var alreadyShared = viewModel.onEvent(ShareLessonEvent.AddUser(searchText))
                if (alreadyShared == "user added") {
                    Toast
                        .makeText(context, "Lesson Already Shared With User", Toast.LENGTH_SHORT)
                        .show()
                } else if (alreadyShared == "not found") {
                    Toast
                        .makeText(context, "User cannot be found", Toast.LENGTH_SHORT)
                        .show()
                } else if (alreadyShared == "empty search") {
                    Toast
                        .makeText(context, "Search Field Empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast
                        .makeText(context, "Lesson Shared", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            contentAlignment = Alignment.Center){
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Button")
        }
    }
    
}

@Composable
fun UserCard(user: User, userId: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .padding(bottom = 15.dp)
        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
        .background(Color(238, 238, 255), shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {


        Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {

            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Icon", Modifier.size(30.dp))
            Text(
                text = user.name,
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

            Text(
                text = "Tag: ${userId}",
                textAlign = TextAlign.End,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )


    }
}

@Composable
fun topBarShare(navController: NavController){
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(contentAlignment = Alignment.Center) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), horizontalArrangement = Arrangement.Start) {

                    Button(
                        onClick = { navController.navigateUp() },
                        shape = RoundedCornerShape(60),
                        modifier = Modifier.width(100.dp).shadow(elevation = 5.dp, shape = RoundedCornerShape(60)),
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(text = "Back" , color = Color.Black, fontSize = 16.sp)
                    }
                }
                Text(text = "Lesson Share", fontSize = 24.sp)
            }
        }
        Divider(color = Color.Black, modifier = Modifier
            .padding(5.dp)
            .padding(horizontal = 15.dp)
            .padding(bottom = 5.dp)
            .height(1.dp)
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
        )
    }
}