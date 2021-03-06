package com.github.premnirmal.ticker.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.premnirmal.ticker.components.Injector
import com.github.premnirmal.ticker.model.IStocksProvider
import com.github.premnirmal.ticker.network.data.Properties
import com.github.premnirmal.ticker.network.data.Quote
import com.github.premnirmal.ticker.repo.StocksStorage
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlertsViewModel : ViewModel() {

  @Inject internal lateinit var stocksProvider: IStocksProvider
  @Inject internal lateinit var stocksStorage: StocksStorage

  lateinit var symbol: String
  val quote: Quote?
    get() = stocksProvider.getStock(symbol)

  init {
    Injector.appComponent.inject(this)
  }

  fun setAlerts(
    alertAbove: Float,
    alertBelow: Float
  ) {
    viewModelScope.launch {
      quote?.let {
        val properties = it.properties ?: Properties(
            symbol
        )
        it.properties = properties.apply {
          this.alertAbove = alertAbove
          this.alertBelow = alertBelow
        }
        stocksStorage.saveQuoteProperties(properties)
      }
    }
  }
}