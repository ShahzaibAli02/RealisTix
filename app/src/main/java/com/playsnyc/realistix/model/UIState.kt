package com.playsnyc.realistix.model

import com.playsnyc.realistix.enums.UiScreens

data class UIState(
    var isLoading:Boolean=false,
      var error: Error?= null,
      var uiScreen:UiScreens=UiScreens.NONE
        )