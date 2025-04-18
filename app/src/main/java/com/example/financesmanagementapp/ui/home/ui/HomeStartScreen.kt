package com.example.financesmanagementapp.ui.home.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.home.data.model.RegisterEntity
import com.example.financesmanagementapp.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStartScreen(
    navController : NavController,
    registersDetailList: List<RegisterEntity>,
    currentBalance: Double,
    onBtcButtonClick: () -> Unit,
    currentBtcValueDouble : Double
){
    Scaffold(
        topBar = { TopAppBar(title = {Text("Inicio") })},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = AppScreens.AddRegisterAmountScreen.route + "/Mi parametro") // Donde se displayara??
                    //registersDetailList
                    Log.d("franco", "Clciked floating action button")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ){
            BodyContent(navController, registersDetailList, currentBalance, onBtcButtonClick, currentBtcValueDouble)
        }
        val a = paddingValues // To avoid error
    }
}

@Composable
fun BodyContent(
    navControler : NavController,
    registersDetailList: List<RegisterEntity>,
    currentBalance: Double,
    onBtcButtonClick: () -> Unit,
    currentBtcValueDouble: Double
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "ARS", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(22.dp))
            Text(text = currentBalance.toString(), style = MaterialTheme.typography.titleLarge)
        }
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "BTC: ${currentBtcValueDouble}", style = MaterialTheme.typography.titleLarge) // TODO esto va a estar en otra pantalla de Mercados o algo asi
            Button(onClick = onBtcButtonClick){
                Icon(Icons.Default.Refresh,"Sync btc value")
            }
        }
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Button(
            onClick = {/*
                navControler.navigate(route = AppScreens.AddRegisterAmountScreen.route + "/Mi parametro") // Donde se displayara??
                Log.d("franco", "Boton que te lleva a los graficos")
            */}
        ){
            Text("Graficos")
        }
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ultimos movimientos", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = {/*TODO borra todos los registros*/}
            ){
                Icon(Icons.Rounded.Delete, contentDescription = "Borrar todos los regs")
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       RegistersList(registersDetailList)
        /*Text("Monto 0 ARS", style = MaterialTheme.typography.labelLarge)
        Button(onClick =
        {
            navControler.navigate(route = AppScreens.AddRegisterAmountScreen.route + "/Mi parametro") // Donde se displayara??
            Log.d("franco", "Button")
        }
        ){
            Text("+")
        }*/
    }
}

@Composable
fun RegistersList(registersDetailList: List<RegisterEntity>) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        items(registersDetailList){ registerDetail ->
            Spacer(modifier = Modifier.height(8.dp))
            Register(registerDetail)
        }
    }
}

@Composable
fun Register(regEntityData: RegisterEntity) {
    val regTextData = listOf(regEntityData.title, regEntityData.description) // TODO Temporal, agregar mas caracteristicas
    Row(modifier = Modifier.
    background(MaterialTheme.colorScheme.background)
        .padding(8.dp))
    {
        MyImage()
        RegisterContent(regTextData)
    }
}

@Composable
fun RegisterContent(regTextData: List<String>) {
    var expanded by remember { mutableStateOf(false) } // Necesitamos que la variable mute en tiempo de ejecucion.
    // Ademas necesitamos que la variable mute a nivel de estado, y que esto haga que se repiten la interfaz
    Column(modifier = Modifier.padding(start = 8.dp).clickable {
        expanded = !expanded
    }) {
        RegisterTitle(regTextData[0], MaterialTheme.typography.labelLarge)
        RegisterDescription(
            regTextData[1],
            MaterialTheme.typography.labelMedium,
            if(expanded) Int.MAX_VALUE else 1
        )
    }
}

@Composable
fun RegisterTitle(title: String, style: TextStyle) {
    Text(
        text = title,
        style = style
    )
}

@Composable
fun RegisterDescription(desc: String, style: TextStyle, lines: Int = Int.MAX_VALUE) {
    Text(
        text = desc,
        style = style,
        maxLines = lines
    )
}

@Composable
fun MyImage(){
    Image(
        painterResource(R.drawable.ic_launcher_foreground),
        "Mi imagen",
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground))
}