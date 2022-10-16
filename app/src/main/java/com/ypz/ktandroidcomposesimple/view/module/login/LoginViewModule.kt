package com.ypz.ktandroidcomposesimple.view.module.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypz.ktandroidcomposesimple.common.net.HttpResult
import com.ypz.ktandroidcomposesimple.net.wanAndroid.api.WanAndroidNetService
import com.ypz.ktandroidcomposesimple.tools.WanAndroidUserTools
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModule @Inject constructor(private var service: WanAndroidNetService) : ViewModel() {

    var viewStates by mutableStateOf(LoginViewState())
        private set
    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> login()
            is LoginViewAction.ClearAccount -> clearAccount()
            is LoginViewAction.ShowPassWord -> showPassword()
            is LoginViewAction.HidePassWord -> hidePassword()
            is LoginViewAction.UpdateAccount -> updateAccount(action.account)
            is LoginViewAction.UpdatePassword -> updatePassword(action.password)
            is LoginViewAction.PopBack -> viewModelScope.launch {
//                if (viewEvents.last() != LoginViewEvent.Logging) {
                _viewEvents.send(LoginViewEvent.PopBack)
//                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(LoginViewEvent.Logging)
            flow {
                val befroeRequestTime = System.currentTimeMillis()
                val request = service.login(viewStates.account.trim(), viewStates.password.trim())
                //避免网络响应过快导致 loading页面显示一闪而过
                val invalidTime = System.currentTimeMillis() - befroeRequestTime
                val miniInvalid = 3000L
                if (invalidTime < miniInvalid) {
                    kotlinx.coroutines.delay(miniInvalid)
                }
                emit(request)
            }.map {
                if (it.errorCode == 0) {
                    if (it.data != null) {
                        HttpResult.Success(it.data!!)
                    } else {
                        throw Exception("the result of remote's request is null")
                    }
                } else {
                    throw Exception(it.errorMsg)
                }
            }
                .flowOn(Dispatchers.IO).onEach {
                WanAndroidUserTools.onLogin(it.result)
                _viewEvents.send(LoginViewEvent.PopBack)
            }.catch {
                _viewEvents.send(LoginViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }

    private fun clearAccount() {
        viewStates = viewStates.copy(account = "")
    }

    private fun showPassword() {
        viewStates = viewStates.copy(password = "")
    }

    private fun hidePassword() {
        viewStates = viewStates.copy(password = "")
    }

    private fun updateAccount(account: String) {
        viewStates = viewStates.copy(account = account)
    }

    private fun updatePassword(password: String) {
        viewStates = viewStates.copy(password = password)
    }

}

data class LoginViewState(
    val account: String = "",
    val password: String = "",
    val isLogged: Boolean = false
)

/**
 * 一次性事件
 */
sealed class LoginViewEvent {

    object PopBack : LoginViewEvent()

    object Logging : LoginViewEvent()

    data class ErrorMessage(val message: String) : LoginViewEvent()
}

sealed class LoginViewAction {
    object PopBack : LoginViewAction()
    object Login : LoginViewAction()
    object ClearAccount : LoginViewAction()
    object ShowPassWord : LoginViewAction()
    object HidePassWord : LoginViewAction()
    data class UpdateAccount(val account: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
}