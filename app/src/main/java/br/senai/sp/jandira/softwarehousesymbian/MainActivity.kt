package br.senai.sp.jandira.softwarehousesymbian

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.softwarehousesymbian.ui.theme.SoftwareHouseSymbianTheme
import br.senai.sp.jandira.softwarehousesymbian.model.ApiResponse
import br.senai.sp.jandira.softwarehousesymbian.service.ApiService
import br.senai.sp.jandira.softwarehousesymbian.service.RetrofitFactory
import coil.compose.AsyncImage
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.chromium.base.Callback
import org.chromium.base.Log
import retrofit2.Call
import retrofit2.Response
import retrofit2.await


class MainActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoftwareHouseSymbianTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  RegisterUser()
                }
            }
        }
//        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUser() {

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    var url by remember {
        mutableStateOf<String>("")
    }

    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }

    var results by remember {
        mutableStateOf<ApiResponse?>(null)
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    //variavel que recebe o contexto
    val context = LocalContext.current


    //inicio column principal
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Inicio da Column login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(206, 1, 240)
            )

            Text(
                text = "",
                fontSize = 14.sp,
                color = Color(160, 156, 156)
            )

            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier.size(115.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Card(
                    modifier = Modifier
                        .size(115.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (0).dp, y = 0.dp)
                        .clickable {
                            singlePhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    shape = CircleShape,

                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop)

                    DisposableEffect(uri) {
                        if (uri != null) {
                            // Call your UploadPick function here when uri is updated
                            fun UploadPick(uri: Uri, context: Context) {
                                uri?.let {
                                    StorageUtil.uploadToStorage(
                                        uri = it,
                                        context = context,
                                        type = "image",
                                        {
                                            url = it
                                        })
                                }
                            }

                            UploadPick(uri!!, context)

                        }

                        onDispose { }
                    }

                }
                Image(painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                    contentDescription = "Camera", modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(25.dp)
                        .height(25.dp)
                        .clickable { })


            } // Fim da column login

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {

                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    //------------------
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = emailState ?: "example@gmail.com",
                        onValueChange = { emailState = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = {
                            Text(text = "E-mail")
                        },

                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordState ?: "123dfgt",
                        onValueChange = { passwordState = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        label = {
                            Text(text = "Senha")
                        },

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val body = JsonObject().apply {
                                addProperty("login", emailState)
                                addProperty("senha", passwordState)
                                addProperty("imagem", url)
                            }

//                            val call = RetrofitFactory.cadastro().createUser(body)
                            // Use uma coroutine para chamar a função suspensa createUser
                            GlobalScope.launch(Dispatchers.Main) {
                                try {
                                    val response = RetrofitFactory.cadastro().createUser(body).execute()

                                    if (response.isSuccessful) {
                                        val apiResponse = response.body()

                                        // Manipule a resposta aqui
                                        Toast.makeText(
                                            context,
                                            "${apiResponse?.mensagemStatus}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Tratar erro na resposta
                                        Toast.makeText(
                                            context,
                                            "Erro na requisição: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    // Trate exceções aqui
                                    Toast.makeText(
                                        context,
                                        "Erro na requisição: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(Color(179, 125, 255)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row {

                            Text(
                                text = "CADASTRAR",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                    }
                }

            }
        }
    }


}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SoftwareHouseSymbianPreview() {
    SoftwareHouseSymbianTheme {
       RegisterUser()
    }
}