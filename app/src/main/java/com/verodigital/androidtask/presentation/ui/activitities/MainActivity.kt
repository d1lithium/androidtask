package com.verodigital.androidtask.presentation.ui.activitities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.verodigital.androidtask.R
import com.verodigital.androidtask.domain.TaskListViewModel
import com.verodigital.androidtask.presentation.ui.theme.AndroidtaskTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val b = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_host_fragment_container.findNavController().navigate(R.id.mainFragment,b)

    }

}
