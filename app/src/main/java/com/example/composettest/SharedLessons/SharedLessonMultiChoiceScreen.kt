package com.example.composettest.SharedLessons

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.composettest.Lesson.LessonQuestionViewModel
import com.example.composettest.Lesson.SignChoiceViewModel
import com.example.composettest.Screen

@Composable
fun SharedLessonMultiChoiceScreen (
    navController: NavController,
    backStackEntry: NavBackStackEntry = navController.getBackStackEntry(Screen.SharedLessonPreviewScreen.route + "?lessonId={lessonId}"),
    signViewModel: SharedLessonPlayViewModel = hiltViewModel(remember { backStackEntry }),
    lessonId: String,
    questionIndex: Int,
    lessonTitle: String

){

    var selected by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }
    var selected3 by remember { mutableStateOf(false) }
    var selected4 by remember { mutableStateOf(false) }

    var answer = signViewModel.lessonEditState.value.lesson.questionsList[questionIndex].signData.sign
    var guess = remember { mutableStateOf("test") }
    var signData = signViewModel.signDataList.value
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(72,69,221)
    ){}
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

        topBarLesson(navController = navController, lessonTitle = lessonTitle)

        signViewModel.lessonEditState.value.lesson.questionsList[questionIndex].signData.filePath?.let { VideoDisplay(filepath = it) }

        Column(
            Modifier
                .padding(top = 10.dp)
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.White, shape = RoundedCornerShape(20.dp))) {

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color(238, 238, 255), shape = RoundedCornerShape(20.dp))){
                Text(modifier = Modifier.padding(20.dp), text = "What is the word being signed?", fontSize = 24.sp, textAlign = TextAlign.Center)
            }
            Column(modifier = Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                var signList = listOf(signData.signDataList.random().sign,signData.signDataList.random().sign,signData.signDataList.random().sign)

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SelectableButton(selected = selected, content = signList[0], onClick = {
                        selected = !selected
                        selected2 = false
                        selected3 = false
                        selected4 = false
                        guess.value = signList[0]})
                    SelectableButton(selected = selected2, content = signList[1], onClick = {
                        selected2 = !selected2
                        selected = false
                        selected3 = false
                        selected4 = false
                        guess.value = signList[1] })

                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SelectableButton(selected = selected3, content = signList[2], onClick = {
                        selected3 = !selected3
                        selected = false
                        selected2 = false
                        selected4 = false
                        guess.value = signList[2]})
                    if (answer != null) {
                        SelectableButton(selected = selected4, content = answer, onClick = {
                            selected4 = !selected4
                            selected = false
                            selected2 = false
                            selected3 = false
                            guess.value = answer
                        })
                    }

                }
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {

                        println(answer)
                        println(guess.value)
                        println(questionIndex)
                        if (answer == guess.value) {signViewModel.updateQuestion(questionIndex,1); signViewModel.nextScreen(questionIndex+1, signViewModel.lessonEditState.value.lesson.name, navController, lessonName = lessonId)}
                        else {signViewModel.updateQuestion(questionIndex,0); signViewModel.nextScreen(questionIndex+1, signViewModel.lessonEditState.value.lesson.name, navController, lessonName = lessonId)}

                    } ,modifier = Modifier
                        .width(200.dp)
                        .height(70.dp)
                        .padding(15.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(72,69,221)))  {
                        Text(text = "Next", color = Color.White)

                    }

                }

            }
        }
    }
}
@Composable
fun selectionButton(text: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) Color.Blue else Color.Green

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {},
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(backgroundColor = color),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text,
                color = Color.White
            )
        }
    }
}


@Composable
fun SelectableButton(
    selected: Boolean,
    content: String,
    Color: Color =
        if (selected) Color(199,199,255)
        else Color(238, 238, 255),
    onClick: () -> Unit

){

    Column(modifier = Modifier
        .padding(5.dp)
        .width(150.dp)
        .height(70.dp)
        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
        .clip(shape = RoundedCornerShape(20.dp))
        .clickable {
            onClick()
        }
        .background(Color, shape = RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = content, fontSize = 22.sp)
    }
}

@Composable
private fun VideoDisplay(filepath: Int){
    val context = LocalContext.current
    val exoPlayer = remember{ getSimpleExoPlayer(context, filepath) }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(top = 15.dp)
            .clip(shape = RoundedCornerShape(20.dp)),
        factory = { context1 ->
            PlayerView(context1).apply {
                player = exoPlayer
            }
        },
    )
}
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun getSimpleExoPlayer(context: Context, filepath: Int): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {

        val uri = "android.resource://" + context.packageName + "/" + filepath

        val defaultDataSourceFactory = DefaultDataSource.Factory(context)
        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
            context,
            defaultDataSourceFactory
        )
        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
        setMediaSource(source)
        prepare()
        play()
        repeatMode = Player.REPEAT_MODE_ALL
    }
}

@Composable
fun topBarLesson(navController: NavController, lessonTitle: String){
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
                        onClick = { navController.navigate(Screen.HomeScreen.route) },
                        shape = RoundedCornerShape(60),
                        modifier = Modifier
                            .width(100.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(60)),
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(text = "Back" , color = Color.Black, fontSize = 16.sp)
                    }
                }
                Text(text = lessonTitle, fontSize = 24.sp)
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
