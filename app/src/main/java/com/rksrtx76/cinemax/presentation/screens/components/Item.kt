package com.rksrtx76.cinemax.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.rksrtx76.CINEMAX.model.Media
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.presentation.viewmodel.DetailsViewModel
import com.rksrtx76.cinemax.presentation.viewmodel.HomeViewModel
import com.rksrtx76.cinemax.ui.theme.Radius
import com.rksrtx76.cinemax.util.Constants.POSTER_IMAGE_BASE_URL
import com.rksrtx76.cinemax.util.Screen
import timber.log.Timber

@Composable
fun Item(
    media : Media,
    homeViewModel: HomeViewModel,
    detailsViewModel : DetailsViewModel,
    navController: NavController,
    type : String,
    modifier: Modifier = Modifier
) {

    val imageUrl = "$POSTER_IMAGE_BASE_URL/${media.poster_path}"
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    val imageState = imagePainter.state

    val defaultDominator = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember{
        mutableStateOf(defaultDominator)
    }
    val selectedOption = homeViewModel.selectedOption.value

    Timber.d("Item- url, ${media.poster_path}")
    Timber.d("Item- title, ${media.title}")
    Timber.d("Item- overview, ${media.overview}")
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.dp))
            .clickable {
                navController.navigate(
                    "${Screen.DETAILS_SCREEN}?id=${media.id}&type=$type"
                )
            }
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ){
        if(imageState is AsyncImagePainter.State.Success){
            val imageBitmap = imageState.result.drawable.toBitmap()

            dominantColor = getAverageColor(imageBitmap = imageBitmap.asImageBitmap())

            Image(
                painter = imagePainter,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        if(imageState is AsyncImagePainter.State.Loading){
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center),
                strokeWidth = 2.dp
            )
        }
        if(imageState is AsyncImagePainter.State.Error){
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.cinemax),
                contentDescription = media.title
            )
        }
    }
}