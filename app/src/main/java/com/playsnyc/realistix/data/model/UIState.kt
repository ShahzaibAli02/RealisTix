package com.playsnyc.realistix.data.model

import com.playsnyc.realistix.enums.UiScreens

data class UIState<T>(
    var uiScreen: UiScreens = UiScreens.NONE,
    var data:T ? = null,
    var state: ScreenState = ScreenState.None()
)

val <T> UIState<T>.isLoading: Boolean
    get() = this.state is ScreenState.Loading

val <T> UIState<T>.isSuccess: Boolean
    get() = this.state is ScreenState.Success

val <T> UIState<T>.isError: Boolean
    get() = this.state is ScreenState.Error


val <T> UIState<T>.errorMessage: String
    get() = if( this.state is ScreenState.Error) (this.state as ScreenState.Error).message else "Error"


val <T> UIState<T>.successMessage: String
    get() = if( this.state is ScreenState.Success) (this.state as ScreenState.Success).message else ""


sealed class ScreenState(val message:String=""){
    class None: ScreenState()
    class Loading(val loadingMessage:String="",val progress:Int=0): ScreenState(loadingMessage)

    class Error(val errorMessage:String=""): ScreenState(errorMessage)
    class Success(val successMessage:String=""): ScreenState(successMessage)
}