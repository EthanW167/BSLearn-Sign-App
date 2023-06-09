package com.example.composettest.UserInterface

import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.composettest.Domain.model.Lesson
import com.example.composettest.Lesson.LessonState
import com.example.composettest.Lesson.LessonViewModel
import com.example.composettest.R
import com.example.composettest.Screen
import com.example.composettest.SharedLessons.SharedLessonsViewModel
import com.example.composettest.ui.theme.ComposetTestTheme

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: LessonViewModel = hiltViewModel(), // View model to manage databases
    shareViewModel: SharedLessonsViewModel = hiltViewModel())
{


    //navController.popBackStack(route = Screen.LessonPreviewScreen.route + "?id={id}", inclusive = true)

    // state values are assigned to notify Compose recomposition is needed on value change

    val state = viewModel.state.value

    val userState = viewModel.user.value

    var localContext = LocalContext.current

    val userId = Settings.Secure.getString(localContext.contentResolver, Settings.Secure.ANDROID_ID).toString() // Android device Id

    viewModel.getUserData(userId)

    shareViewModel.getUser(userId)

    ComposetTestTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(72,69,221)
        ) {}
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally) {

        TopBar()

        MainBody(createTempDataList(), navController = navController, state, userState)
    }
}

@Composable // Composable to handle the information about streaks and signs learned
fun InfoBox(userState: UserState) {
    Row(
        Modifier
            .padding(top = 100.dp)
            .width(350.dp)
            .height(70.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color(72, 69, 211), shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        Icon(
            Icons.Default.Fireplace, contentDescription = "Streak Icon",
            Modifier
                .padding(top = 5.dp)
                .size(32.dp))
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Streak", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "5", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Divider(color = Color.Black, modifier = Modifier
            .height(50.dp)
            .width(1.dp)
            .align(Alignment.CenterVertically)
        )

        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Signs", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "${userState.user.signsLearned.size}", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Icon(
            Icons.Default.WavingHand, contentDescription = "Streak Icon",
            Modifier
                .padding(top = 5.dp)
                .size(32.dp))
    }
}

@Preview
@Composable
fun TopBar() {
    val contextForToast = LocalContext.current.applicationContext

    TopAppBar(
        title = {
            Text(text = "Sign App")
        },
        actions = {
            // search icon
            TopBarIconButton(
                imageVector = Icons.Outlined.Notifications,
                description = "Notifications",

                ) {
                Toast.makeText(contextForToast, "Notifications", Toast.LENGTH_SHORT)
                    .show()
            }

            // lock icon
            TopBarIconButton(
                imageVector = Icons.Outlined.Person,
                description = "Profile"
            ) {
                Toast.makeText(contextForToast, "Profile", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
fun TopBarIconButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}

@Composable
fun MainBody(list: List<TestData>, navController: NavController, state: LessonState, userState: UserState){
    Box() {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.White, shape = RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoBox(userState)
            Text(
                text = "Lessons",
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp), textAlign = TextAlign.Center, fontSize = 24.sp
            )

            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .padding()
                    .fillMaxWidth()
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                    .height(120.dp)
                    .background(Color(238, 238, 255), shape = RoundedCornerShape(20.dp))

                /*.border(
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    RoundedCornerShape(20.dp)
                )*/,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                LazyRow(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp)
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    items(state.lessons) { lesson ->

                        LessonCard(lessonData = lesson, navController = navController)

                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Arrow", modifier = Modifier.size(40.dp))
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxSize(), horizontalArrangement = Arrangement.Center
            ) {
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {

                    MainBodyIconButton(
                        imageVector = Icons.Default.WavingHand,
                        description = "Practice",
                        name = "Practice",
                        navController = navController,
                        size = 90
                    )
                    MainBodyIconButton(
                        imageVector = Icons.Default.EditNote,
                        description = "Lesson Maker",
                        name = "Lesson Maker",
                        navController = navController,
                        size = 120
                    )
                }
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {

                    MainBodyIconButton(
                        imageVector = Icons.Rounded.List,
                        description = "Dictionary",
                        name = "Dictionary",
                        navController = navController,
                        size = 120
                    )
                    MainBodyIconButton(
                        imageVector = Icons.Default.MenuBook,
                        description = "Community Lessons",
                        name = "Community Lessons",
                        navController = navController,
                        size = 100
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(
            Alignment.TopCenter)) {
            ProfileCard()
            Text(text = "*Insert Name*", fontSize = 24.sp)
        }
    }
}


@Composable
fun MainBodyIconButton(
    imageVector: ImageVector,
    description: String,
    name: String,
    navController: NavController,
    size: Int
) {

    var deviceId = Settings.Secure.getString(LocalContext.current.contentResolver, Settings.Secure.ANDROID_ID).toString()

    IconButton(onClick = {
        if (name == "Lesson Maker") {
            navController.navigate(Screen.LessonMakerOverview.route + "?userId=${deviceId}")
        } else if (name == "Dictionary") {
            navController.navigate(Screen.Dictionary.route)
        } else if (name == "Practice") {
            navController.navigate(Screen.PracticeSelectionScreen.route)
        } else if (name == "Community Lessons") {
            navController.navigate(Screen.SharedLessonsScreen.route + "?userId=${deviceId}")
        }
    },
        Modifier
            .padding(15.dp)
            .size(150.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color(238, 238, 255), shape = RoundedCornerShape(20.dp))) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Icon(
                imageVector = imageVector,
                contentDescription = description,
                Modifier.size(size.dp)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "$name", textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 5.dp))
            }
        }
    }
}


@Composable
fun LessonCard(lessonData: Lesson, navController: NavController){

    var color =
        if (lessonData.isCompleted == 1){
            Color(72, 69, 211)
        } else {
            Color.White
        }
    Card(
        Modifier
            .size(90.dp)
            .padding(5.dp)
            .clickable {
                navController.navigate(
                    Screen.LessonPreviewScreen.route + "?id=${lessonData.id}"
                )
            },
        backgroundColor = Color(238, 238, 255)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .padding(5.dp)
                    .size(45.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(
                        border = BorderStroke(width = 1.dp, Color.Black),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color, shape = RoundedCornerShape(10.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Text(text = "${lessonData.lessonNum}", textAlign = TextAlign.Center, color = Color.Black, fontSize = 30.sp)
            }
            Text(text = lessonData.name, textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun ProfileCard(){

    Box(){
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(238, 238, 255)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Placeholder", Modifier.size(60.dp))
        }
        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon", modifier = Modifier
            .size(35.dp)
            .align(Alignment.BottomEnd))
    }
}

@Preview
@Composable
fun OverlapTest(){
    Box {

        Column(
            Modifier
                .fillMaxSize()
                .padding(5.dp, top = 60.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ){}
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(
            Alignment.TopCenter)) {
            ProfileCard()
            Text(text = "*Insert Name*")
        }

    }
}


fun createTempDataList(): List<TestData> {

    val list = mutableListOf<TestData>()

    list.add(TestData("0","Greetings"))
    list.add(TestData("1","Hobbies"))
    list.add(TestData("2","Alphabet 1"))
    list.add(TestData("3","Alphabet 2"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))
    list.add(TestData("4","Miscellaneous"))

    val tempRandomList = mutableListOf<TestData>()
    for (i in list.indices) {
        tempRandomList.add(list.random())
    }

    return tempRandomList
}

data class TestData(
    val lessonNum: String,
    val title: String
)