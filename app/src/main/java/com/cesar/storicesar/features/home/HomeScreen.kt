package com.cesar.storicesar.features.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.storicesar.R
import com.cesar.storicesar.entities.BankAccount
import com.cesar.storicesar.entities.BankMovement

@Composable
fun HomeScreen(
    showEmptyMovements: Boolean,
    bankAccount: BankAccount,
    movements: MutableList<BankMovement>,
    onMovementClick: (movement: BankMovement) -> Unit,
    onBackPress:() -> Unit
) {
    BackHandler {
        onBackPress()
    }
    Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {

        Text(text = stringResource(id = R.string.bank_account), fontSize = 32.sp)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = stringResource(id = R.string.account_number),
                fontWeight = FontWeight.Bold
            )
            Text(text = bankAccount.accountNumber)
        }
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = stringResource(id = R.string.balance),
                fontWeight = FontWeight.Bold
            )
            Text(text = bankAccount.balance)
        }
        Text(
            text = stringResource(id = R.string.transactions),
            fontSize = 32.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        if(showEmptyMovements){
            Text(
                text = stringResource(id = R.string.no_movements),
                fontSize = 32.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        LazyColumn {
            items(movements) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            onMovementClick(item)
                        }
                ) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.amount,
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }

    }
}