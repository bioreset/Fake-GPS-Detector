package com.dariusz.fakegpsdetector.utils

import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager.*
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.CellTowerType

object CellTowersUtils {

    fun detectCellTowerType(cellType: Int?): CellTowerType {
        return when (cellType) {
            NETWORK_TYPE_LTE -> CellTowerType("lte")
            NETWORK_TYPE_GSM -> CellTowerType("gsm")
            NETWORK_TYPE_CDMA -> CellTowerType("wcdma")
            else -> CellTowerType("not detected")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun newCellTowersList(
        cellTowerType: CellTowerType?,
        cellTowerList: List<CellInfo>?
    ): List<CellTowerModel>? {
        val listLte = cellTowerList as List<CellInfoLte>?
        val listGsm = cellTowerList as List<CellInfoGsm>?
        val listWcdma = cellTowerList as List<CellInfoWcdma>?
        return when (cellTowerType?.type) {
            "lte" -> newCellTowersListLte(listLte)
            "gsm" -> newCellTowersListGsm(listGsm)
            "wcdma" -> newCellTowersListWcdma(listWcdma)
            else -> null
        }
    }

    private fun newCellTowersListLte(cltwm: List<CellInfoLte>?): List<CellTowerModel>? {
        return cltwm?.map {
            CellTowerModel(
                cellId = it.cellIdentity.ci,
                locationAreaCode = it.cellIdentity.tac,
                mobileCountryCode = it.cellIdentity.mccString,
                mobileNetworkCode = it.cellIdentity.mncString,
                signalStrength = it.cellSignalStrength.dbm
            )
        }
    }

    private fun newCellTowersListGsm(cltwm: List<CellInfoGsm>?): List<CellTowerModel>? {
        return cltwm?.map {
            CellTowerModel(
                cellId = it.cellIdentity.cid,
                locationAreaCode = it.cellIdentity.lac,
                mobileCountryCode = it.cellIdentity.mccString,
                mobileNetworkCode = it.cellIdentity.mncString,
                signalStrength = it.cellSignalStrength.dbm
            )
        }
    }

    private fun newCellTowersListWcdma(cltwm: List<CellInfoWcdma>?): List<CellTowerModel>? {
        return cltwm?.map {
            CellTowerModel(
                cellId = it.cellIdentity.cid,
                locationAreaCode = it.cellIdentity.lac,
                mobileCountryCode = it.cellIdentity.mccString,
                mobileNetworkCode = it.cellIdentity.mncString,
                signalStrength = it.cellSignalStrength.dbm
            )
        }
    }
}
