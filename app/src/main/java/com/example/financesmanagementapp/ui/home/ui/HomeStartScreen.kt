package com.example.financesmanagementapp.ui.home.ui

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.utils.Constants
import kotlinx.coroutines.launch
import com.example.financesmanagementapp.domain.model.Record

/**
 * Main home screen of the application. Displays balance, crypto prices, and recent movements.
 *
 * @param navController Controller for navigation between screens.
 * @param viewModel ViewModel that manages the state and logic for the home screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStartScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val record =
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Record?>("record", null)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val csvSelectorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importCsv(it) }
    }

    val recordsList by viewModel.recordsList.collectAsState(initial = emptyList())

    record?.let{
        Log.d("franco","Valor actual del Record: ${record.value}")
    } // TODO borrar

    val context = LocalContext.current

    observeValuesUpdatedByWorker(context, viewModel)

    LaunchedEffect(Unit) {
        viewModel.setupWorkers(context)
        observeWorker(context)
        viewModel.exportedCsvEvent.collect { event ->
            when (event) {
                is HomeUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Mis Finanzas", modifier = Modifier.padding(18.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Importar registros") },
                    selected = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.import_icon),
                            contentDescription = "Importar registros",
                            Modifier
                                .width(36.dp)
                                .height(36.dp)
                        )
                    },
                    onClick = {
                        scope.launch {
                            csvSelectorLauncher.launch("text/csv")
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Exportar registros") },
                    selected = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.export_icon),
                            contentDescription = "Exportar registros",
                            Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    },
                    onClick = {
                        scope.launch {
                            Log.d("franco", "recordList: $recordsList")
                            viewModel.exportCsv()
                            scope.launch { drawerState.close() }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Mis Finanzas")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(route = AppScreens.AddRecordAmountScreen.route)
                        //recordsDetailList
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                BodyContent(
                    viewModel,
                    recordsList
                )
            }
            val a = paddingValues // To avoid error
        }
    }
}

/**
 * Main content body for the HomeStartScreen.
 */
@Composable
fun BodyContent(
    viewModel: HomeViewModel,
    recordsList: List<Record>,
) {
    val currentBalance by viewModel.currentBalance.collectAsState(initial = 0.0)
    val currentBtcValueDouble by viewModel.btcPrice.collectAsState(initial = 0.0)

    // Using remember with recordsList as key ensures that whenever recordsList updates
    // (from the StateFlow), the sorting is recalculated efficiently.
    val sortedRecords = remember(recordsList) {
        recordsList.sortedDescending()
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "ARS", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(22.dp))
            Text(text = String.format(java.util.Locale.US, "%.2f", currentBalance), style = MaterialTheme.typography.titleLarge)
        }
    }
    /*Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "BTC: $currentBtcValueDouble",
                style = MaterialTheme.typography.titleLarge
            ) // TODO esto va a estar en otra pantalla de Mercados o algo asi
            Button(onClick = { viewModel.getCryptoPrice(Constants.BTC_USDT_TICKER) }) {
                Icon(Icons.Default.Refresh, "Sync btc value")
            }
        }
    }*/
    /*Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {/*
                //TODO Habilitar funcionaidad de boton de graficos
                navControler.navigate(route = AppScreens.AddRecordsAmountScreen.route + "/Mi parametro") // Donde se displayara??
                Log.d("franco", "Boton que te lleva a los graficos")
            */
            }
        ) {
            Text("Graficos")
        }
    }*/
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ultimos movimientos", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = { viewModel.clearRecordsList() }
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = "Borrar todos los regs")
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       RecordsList(sortedRecords)
    }
}

/**
 * Observes values updated by the background worker for BTC price.
 */
fun observeValuesUpdatedByWorker(context: Context, viewModel: HomeViewModel) {
    WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData("btc_price_worker")
        .observeForever { workInfos ->
            val workInfo = workInfos?.firstOrNull()
            if (workInfo?.state != WorkInfo.State.RUNNING) {
                val prefs = context.getSharedPreferences("btc_price_pref", Context.MODE_PRIVATE)
                val price = prefs.getString("btc_price","0.0")
                Log.d("franco", "observWorkerResult, price: $price")
                viewModel.updateBtcPrice(price.toString())
            }
        }
}

/**
 * Debug observer for worker state and schedule.
 */
fun observeWorker(context: Context){
    WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData("btc_price_worker")
        .observeForever{ workInfos ->
            val workInfo = workInfos?.firstOrNull()
            Log.d("franco", "workinfo state: ${workInfo?.state}")
            val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            format.timeZone = java.util.TimeZone.getTimeZone("America/Argentina/Buenos_Aires")
            val formatted = format.format(workInfo?.nextScheduleTimeMillis)
            Log.d("franco", "workinfo time: ${formatted}")
        }
}

/**
 * Displays a list of financial records.
 */
@Composable
fun RecordsList(recordsList: List<Record>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        items(recordsList) { recordObject ->
            RecordItem(recordObject)
            HorizontalDivider()
        }
    }
}

/**
 * Individual record item component.
 */
@Composable
fun RecordItem(record: Record) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background) // Background of the Row
            .padding(horizontal = 16.dp), // Space around the Row
        verticalAlignment = Alignment.CenterVertically // Defines property for the children
    )
    {
        MyImage(record.category.iconRsc)
        Spacer(modifier = Modifier.width(12.dp))
        RecordContent(record)
    }
}

/**
 * Displays an icon for the corresponding category.
 */
@Composable
fun MyImage(rscId: Int) {
    Image(
        painterResource(rscId),
        "Category image",
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground))
}

/**
 * Content of a record item, handles expansion of the detail of the item.
 */
@Composable
fun RecordContent(record: Record) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Avoids the amount to be compressed
                .padding(start = 8.dp)
                .clickable { expanded = !expanded }
        ) {
            RecordTitle(record.category.categoryName, MaterialTheme.typography.labelLarge)
            RecordDescription(
                record.description,
                MaterialTheme.typography.labelMedium,
                if (expanded) Int.MAX_VALUE else 2
            )
            RecordDate(
                date = record.date,
                style = MaterialTheme.typography.labelSmall,
                color = Color()
            )
        }

        RecordAmount(record.currency, record.amount, record.isIncome)
    }
}

/**
 * Displays a title for a record, being it the name of the corresponding category.
 */
@Composable
fun RecordTitle(title: String, style: TextStyle) {
    Text(
        text = title,
        style = style
    )
}

/**
 * Displays a description for a record.
 */
@Composable
fun RecordDescription(desc: String, style: TextStyle, lines: Int = Int.MAX_VALUE) {
    Text(
        text = desc,
        style = style,
        maxLines = lines,
        overflow = TextOverflow.Ellipsis, // Hides text when record is not selected
    )
}

/**
 * Displays a date for a record.
 */
@Composable
fun RecordDate(date: String, style: TextStyle, color: Color) {
    Text(
        text = date,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * Displays a the amount and the currency of the record.
 */
@Composable
fun RecordAmount(currency: String, amount: Double, isIncome: Boolean) {
    Text(
        text = "$currency ${String.format(java.util.Locale.US, "%.2f", amount)}",
        style = MaterialTheme.typography.labelLarge,
        color = colorResource(if(isIncome) R.color.positive_green else R.color.negative_red)
    )
}
