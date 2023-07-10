package com.example.mydiary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mydiary.presentation.navigation.MyAppNavHost
import com.example.mydiary.presentation.screen.task_list.ToDoListScreen
import com.example.mydiary.presentation.ui.theme.MyDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDiaryTheme {
                MyAppNavHost()
            }
        }
    }
}
