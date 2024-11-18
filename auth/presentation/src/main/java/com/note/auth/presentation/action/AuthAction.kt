package com.note.auth.presentation.action

sealed interface AuthAction {
    data object LoginClick: AuthAction
    data class GetAccessToken(val stateKey: String?, val code: String?): AuthAction

    //TODO
    /**
     * enum class OauthType{
     * Github,
     * Google,
     * Kakao
     * }
     *
     * data class LoginClick(val oauthType: OauthType): AuthAction
     * data object: Register: AuthAction // 일반 회원가입 후 Github 연동
     * */
}