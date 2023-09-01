package br.com.fiap.rickmorty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.rickmorty.model.ListaDePersonagens
import br.com.fiap.rickmorty.model.Personagem
import br.com.fiap.rickmorty.service.RetrofitFactory
import br.com.fiap.rickmorty.ui.theme.RickMortyTheme
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      RickMortyTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting()
        }
      }
    }
  }
}

@Composable
fun Greeting() {

  var resultsState = remember {
    mutableStateOf(listOf<Personagem>())
  }

  // Chamada para a API
  val call = RetrofitFactory().getListaDePersonagensService().getListaDePersonagens()

  call.enqueue(object : Callback<ListaDePersonagens>{
    override fun onResponse(
      call: Call<ListaDePersonagens>,
      response: Response<ListaDePersonagens>
    ) {
      resultsState.value = response.body()!!.results
      Log.i("FIAP", "onResponse: ${response.code()}")
    }

    override fun onFailure(call: Call<ListaDePersonagens>, t: Throwable) {
      Log.i("FIAP", "onFailure: ${t.message}")
    }

  })



  Column {
    LazyColumn(){
      items(resultsState.value){personagem ->
        Card(modifier = Modifier
          .padding(start = 16.dp, end = 16.dp , top = 16.dp)
        ) {
          Row() {
            //Image(painter = painterResource(id = R.drawable.rick), contentDescription = "Imagem do personagem")
            AsyncImage(model = personagem.image, contentDescription = "Imagem do personagem")
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
              Text(
                text = personagem.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
              )
              Text(text = personagem.species)
            }
          }
        }
      }
    }
    Text(text = "FIM DA LISTA")
  }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  RickMortyTheme {
    Greeting()
  }
}