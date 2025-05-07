package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.Record

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDetailScreen(navController: NavController){
    val recordStateFlow = navController.previousBackStackEntry?.savedStateHandle?.getStateFlow<Record?>(
        "record", null
    )
    recordStateFlow?.let{
        Log.d("franco","Valor actual del Record desde RecordDetailScreen: ${recordStateFlow.value}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Detalle")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val record: Record? = recordStateFlow?.value
                    record?.let{
                        Log.d("franco","Valor actual del Record desde RecordDetailScreen: $record")
                    }
                    // TODO ADD record to DB
                    navController.navigate(AppScreens.HomeStartScreen.route)
                },
                modifier = Modifier.padding(16.dp),
                content = {Icon(Icons.Default.Check, contentDescription = "Aceptar")}
            )
        }
    ){ paddingValues ->
        val a  = paddingValues
        Text("hola")//TODO add todo lo que falta
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordDetailScreenWithPreview(){
    val navController = rememberNavController()
    AddRecordDetailScreen(navController)
}