package com.example.financesmanagementapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.model.database.RegisterEntity
import com.example.financesmanagementapp.navigation.AppNavigation
import com.example.financesmanagementapp.navigation.AppScreens

private val registersListInstance = listOf(
    RegisterEntity(-1000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-2000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-3000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-4000.00,"Club de la milanesa", "", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-5000.00,"Club de la milanesa", "Lorem ipsum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-6000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-7000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-8000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-9000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-10000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-11000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-12000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-13000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStartScreen(navControler : NavController){
    Scaffold(
        topBar = { TopAppBar(title = {Text("Inicio") })},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navControler.navigate(route = AppScreens.AddRegisterAmountScreen.route + "/Mi parametro") // Donde se displayara??
                    Log.d("franco", "Clciked floating action button")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            BodyContent(navControler, registersListInstance)
        }
        val a = paddingValues // To avoid error
    }
}

@Composable
fun BodyContent(navControler : NavController, registersDetailList: List<RegisterEntity>){
    Column(
        modifier = Modifier.fillMaxSize(),
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
        RegisterTitle(regTextData[0], MaterialTheme.typography.labelLarge) /*TODO pasar title por parametro*/
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