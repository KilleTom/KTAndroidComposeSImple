package com.ypz.ktandroidcomposesimple.view.module.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.UserInfo
import com.ypz.ktandroidcomposesimple.tools.WanAndroidUserTools
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModule@Inject constructor() : ViewModel() {

    var viewStates by mutableStateOf(AccountViewState())
        private set

    fun dispatch(action: AccountViewAction) {
        when (action) {
            is AccountViewAction.OnStart -> onStart()
            is AccountViewAction.ShowLogoutDialog -> showLogout()
            is AccountViewAction.DismissLogoutDialog -> dismissLogout()
            is AccountViewAction.Logout -> logout()
        }
    }

    private fun logout() {
        viewStates = viewStates.copy(showLogout = false, showLoggingOut = true)
        viewModelScope.launch {
            WanAndroidUserTools.onLogOut()
            delay(1000)
            viewStates = viewStates.copy(isLogged = false, showLogout = false, userInfo = null,  showLoggingOut = false)
        }
//        WanAndroidUserTools.onLogOut()
//        viewStates = viewStates.copy(isLogged = false, showLogout = false, userInfo = null)
    }

    private fun dismissLogout() {
        viewStates = viewStates.copy(showLogout = false)
    }

    private fun showLogout() {
        viewStates = viewStates.copy(showLogout = true)
    }

    private fun onStart() {
        viewStates =
            viewStates.copy(isLogged = WanAndroidUserTools.isLogged, userInfo = WanAndroidUserTools.userInfo)
    }
}

data class AccountViewState(
    val isLogged: Boolean = WanAndroidUserTools.isLogged,
    val userInfo: UserInfo? = WanAndroidUserTools.userInfo,
    val showLogout: Boolean = false,
    val showLoggingOut:Boolean = false
)

sealed class AccountViewAction {
    object OnStart : AccountViewAction()
    object ShowLogoutDialog : AccountViewAction()
    object DismissLogoutDialog : AccountViewAction()
    object Logout : AccountViewAction()
}