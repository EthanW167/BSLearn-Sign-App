package com.example.composettest.Lesson

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composettest.Domain.model.Lesson
import com.example.composettest.Domain.model.Question
import com.example.composettest.Domain.model.signData
import com.example.composettest.Domain.use_case.LessonUseCases
import com.example.composettest.Domain.util.LessonOrder
import com.example.composettest.Domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class LessonSummaryViewModel @Inject constructor(
    private val lessonUseCases: LessonUseCases,
    savedStateHandle: SavedStateHandle

): ViewModel() {


    private val _qState = mutableStateOf(QuestionState())
    val qState: State<QuestionState> = _qState

    private var getQuestionsJob: Job? = null

    var signData: signData? = null

    var lesson: Lesson? = null

    init {
        savedStateHandle.get<Int>("lessonId")?.let { lessonId ->
            getQuestions(lessonId)
            getLesson(lessonId)
        }
    }

    private fun getQuestions(lessonId: Int){
        getQuestionsJob?.cancel()
        lessonUseCases.getQuestionsByIdUseCase(lessonId)
            ?.onEach { questions ->
                _qState.value = qState.value.copy(questions = questions)
            }
            ?.launchIn(viewModelScope)
    }


    private fun getLesson(lessonId: Int){
        viewModelScope.launch {
            lesson = lessonUseCases.getLessonUseCase(lessonId)
                }
        }



    fun getSignData(question: Question){
        viewModelScope.launch {
            signData = lessonUseCases.getSignDataByIdUseCase(question.signId)
        }
    }

    fun insertLesson(lesson: Lesson){
        viewModelScope.launch {
            lessonUseCases.addLessonUseCase(
                Lesson(
                    name = lesson.name,
                    lessonNum = lesson.lessonNum,
                    questions = lesson.questions,
                    id = lesson.id,
                    signs = lesson.signs,
                    isCompleted = 1,
                    previewFilePath = lesson.previewFilePath,
                    description = lesson.description
                )
            )
        }
    }

    fun lessonQuestionDecider(question: Question): Int{

        if (question.signId == 0){
            return 2131165288
        } else if (question.signId == 1){
            return 2131165310
        } else if (question.signId == 2) {
            return 2131165185
        } else if (question.signId == 3) {
            return 2131165316
        } else if (question.signId == 4) {
            return 2131165187
        } else if (question.signId == 5) {
            return 2131165311
        } else if (question.signId == 6) {
            return 2131165312
        } else if (question.signId == 7) {
            return 2131165313
        } else if (question.signId == 8) {
            return 2131165314
        } else if (question.signId == 9) {
            return 2131165315
        }
        return 0
    }
}






