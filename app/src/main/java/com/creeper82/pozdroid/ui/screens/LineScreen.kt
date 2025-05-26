package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.types.Bollard
import com.creeper82.pozdroid.types.DirectionWithStops

@Composable
fun PozDroidLineScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LineHeader(
            lineName = "2",
            directions = arrayOf(
                DirectionWithStops(
                    direction = "Dębiec PKM",
                    bollards = arrayOf(
                        Bollard("Ogrody", "OGRD01"),
                        Bollard("Żeromskiego", "ZER01")
                    )
                ),
                DirectionWithStops(
                    direction = "Ogrody",
                    bollards = arrayOf(
                        Bollard("Dębiec PKM", "DEBI01"),
                        Bollard("Wspólna", "WSPO02")
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LineHeader(
    lineName: String,
    directions: Array<DirectionWithStops>,
    modifier: Modifier = Modifier,
    onDirectionSelected: (direction: DirectionWithStops) -> Unit = {}
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectedDirection by remember { mutableStateOf(directions[0]) }

    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { dropDownExpanded = true })
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                lineName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.width(16.dp))

            Text(
                selectedDirection.direction,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.weight(1f)
            )

            Icon(Icons.Filled.ArrowDropDown, "Expand directions arrow")

            DropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false })
            {
                directions.forEach { dir ->
                    DropdownMenuItem(
                        text = { Text(dir.direction) },
                        onClick = {
                            selectedDirection = dir
                            onDirectionSelected(dir)
                            dropDownExpanded = false
                        }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PozDroidLineScreenPreview() {
    PozDroidLineScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}