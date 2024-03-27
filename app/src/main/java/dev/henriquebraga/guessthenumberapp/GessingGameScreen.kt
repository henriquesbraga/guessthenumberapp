package dev.henriquebraga.guessthenumberapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.henriquebraga.guessthenumberapp.ui.theme.BlueDark
import dev.henriquebraga.guessthenumberapp.ui.theme.YellowDark
import kotlinx.coroutines.delay


@Composable
fun GuessingGameScreen(
    viewModel: MainViewModel
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()


    when (state.gameStage) {
        GameStage.PLAYING -> {
            ScreenContent(
                state = state,
                onValueChange = {
                    viewModel.updateTextField(userNo = it)
                },
                onEnterButtonClicked = {
                    viewModel.onUserInput(state.userNumber, context)
                }
            )
        }
        GameStage.WON -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueDark)
            ) {
                WinOrLoseDialog(
                    text = "Congratulations\nYou Won",
                    buttonText = "Play Again",
                    misteryNumber = state.mysteryNumber,
                    image = painterResource(R.drawable.ic_trophy),
                    resetGame = { viewModel.resetGame() }
                )
            }
        }
        GameStage.LOSE -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueDark)
            ) {
                WinOrLoseDialog(
                    text = "Better Luck next time",
                    buttonText = "Try Again",
                    misteryNumber = state.mysteryNumber,
                    image = painterResource(R.drawable.ic_rowing),
                    resetGame = { viewModel.resetGame() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(
    state: GuessingGameState,
    onValueChange: (String) -> Unit,
    onEnterButtonClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequester.requestFocus()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(BlueDark)
            .padding(20.dp)
    ) {
        Text (
            text = buildAnnotatedString {
                append("No. of guess left: ")
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("${state.noOfGuessLeft}")
                }
            },
            color = YellowDark,
            fontSize = 18.sp
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.guessedNumbersList.forEach { number ->
                Text(
                    text = "$number",
                    color = YellowDark,
                    fontSize = 42.sp,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
        Text(
            text = state.hintDescription,
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .focusRequester(focusRequester = focusRequester),
            value = state.userNumber,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 48.sp
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                containerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onEnterButtonClicked() }
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onEnterButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowDark,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 40.dp)
        ) {
            Text(text = "Enter", fontSize = 18.sp)
        }

    }
}

@Preview
@Composable
fun Prev() {
    val viewModel = viewModel<MainViewModel>()
    GuessingGameScreen(viewModel)
}