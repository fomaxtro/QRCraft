package com.fomaxtro.core.presentation.screen.scan_history.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.presentation.components.QRContactIcon
import com.fomaxtro.core.presentation.components.QRGeolocationIcon
import com.fomaxtro.core.presentation.components.QRLinkIcon
import com.fomaxtro.core.presentation.components.QRPhoneNumberIcon
import com.fomaxtro.core.presentation.components.QRTextIcon
import com.fomaxtro.core.presentation.components.QRWifiIcon
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceAlt
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceDisabled
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toTitle
import com.fomaxtro.core.presentation.model.QRCodeUi
import com.fomaxtro.core.presentation.preview.PreviewModel
import com.fomaxtro.core.presentation.preview.PreviewQr
import java.time.format.DateTimeFormatter

@Composable
fun HistoryItem(
    item: QRCodeUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceHigher
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            when (item.qrCode) {
                is QRCode.Contact -> QRContactIcon()
                is QRCode.Geolocation -> QRGeolocationIcon()
                is QRCode.Link -> QRLinkIcon()
                is QRCode.PhoneNumber -> QRPhoneNumberIcon()
                is QRCode.Text -> QRTextIcon()
                is QRCode.Wifi -> QRWifiIcon()
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = item.qrCode.toTitle(),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.qrCode.toFormattedUiText().asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceAlt,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.createdAt
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceDisabled
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryItemPreview() {
    QRCraftTheme {
        HistoryItem(
            item = PreviewModel.createQRCodeUi(PreviewQr.contact),
            onLongClick = {},
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}