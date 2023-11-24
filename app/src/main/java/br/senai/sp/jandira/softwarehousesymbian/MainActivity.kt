package br.senai.sp.jandira.softwarehousesymbian

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.softwarehousesymbian.ui.theme.SoftwareHouseSymbianTheme
import br.senai.sp.jandira.softwarehousesymbian.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody


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

    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }



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
                        .offset(x = (0).dp, y = 0.dp),
                    shape = CircleShape,
//                    backgroundColor = Color(232, 232, 232, 255)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person, contentDescription = "Pessoa",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(95.dp)
                            .height(95.dp),
                        tint = Color.Gray
                    )
                }
                Image(painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                    contentDescription = "Camera", modifier = Modifier.align(Alignment.BottomEnd)
                        .width(25.dp).height(25.dp).clickable { })


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
                        value = emailState,
                        onValueChange = { emailState = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = {
                            Text(text = "E-mail")
                        },
//                    leadingIcon = {
////                    Icon(painter = painterResource(id = ), contentDescription = )
////                        Icon(
////                            painter = painterResource(
////                                id = R.drawable.email_24
////                            ),
////                            contentDescription = stringResource(
////                                id = R.string.email_description
////
////                            ),
////                            tint = Color(207, 1, 240)
//                        //)
//                    }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordState,
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
                            var user = UserResponse(
                                email = emailState,
                                password = passwordState,
                            )
                           val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)

//                           val call = RetrofitHelper().Register().createUser(user)


                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response = apiService.createUser(user)
                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            val responseBody = response.body()
                                            if (responseBody != null) {
                                                // Sucesso, faça algo aqui se necessário
                                            } else {
                                                Log.i("DS3M", "BODY VAZIO")
                                            }
                                        } else {
                                            Log.i("DS3M", "NOP: ${response.code()}")
                                            // Lide com a resposta de erro aqui, se necessário
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Erro de rede ou exceção, trate de acordo
                                    Log.e("DS3M", "PROBLEMAS: ${e.message}", e)
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