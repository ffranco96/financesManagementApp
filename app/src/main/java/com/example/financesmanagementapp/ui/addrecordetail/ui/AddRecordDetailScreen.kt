package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.Record


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDetailScreen(
    navController: NavController,
    viewModel: AddRecordDetailViewModel
){
    val detail by viewModel.detail.collectAsState()
    val recordStateFlow = navController.previousBackStackEntry?.savedStateHandle?.getStateFlow<Record?>(
        "record", null
    )
    recordStateFlow?.let{
        Log.d("franco","Valor actual del Record desde RecordDetailScreen: ${recordStateFlow.value}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        })
                },
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
                    val myRecord = recordStateFlow?.value?.copy(description = "blablaba")
                    Log.d("franco", "Ultimo estado del record: $myRecord")
                    navController.currentBackStackEntry?.savedStateHandle?.set("record", myRecord)
                    navController.navigate(AppScreens.HomeStartScreen.route)
                    // TODO ADD record to DB
                },
                modifier = Modifier.padding(16.dp),
                content = {Icon(Icons.Default.Check, contentDescription = "Aceptar")}
            )
        }
    ) {
        val a = it // To avoid error
        SecondBodyContent(
            valueDetail = detail,
            onDetailChange = { newValue ->
                viewModel.onDetailChange(newValue)
            }
        )
    }
}

@Composable
fun SecondBodyContent(
    valueDetail : String,
    onDetailChange: (String) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese detalle", style = MaterialTheme.typography.titleLarge)
        TextField(
            value = valueDetail,
            onValueChange = onDetailChange,
            placeholder = {Text("" )},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordDetailScreenWithPreview(){
    val navController = rememberNavController()
    AddRecordDetailScreen(
        navController,
        AddRecordDetailViewModel()
    )
}