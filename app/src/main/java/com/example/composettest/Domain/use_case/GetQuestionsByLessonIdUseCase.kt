package com.example.composettest.Domain.use_case

import com.example.composettest.Domain.model.Question
import com.example.composettest.Domain.model.signData
import com.example.composettest.Domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow

class GetQuestionsByLessonIdUseCase(
    private val repository: LessonRepository
    ) {

        operator fun invoke(lessonId: Int): Flow<List<Question>> {
            return repository.getQuestionsByLessonId(lessonId)
        }
    }
