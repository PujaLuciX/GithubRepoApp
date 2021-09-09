package com.assignment.githubrepoapp.data.model

data class RepoListModel(
    var name: String,
    var author: String,
    var avatar: String,
    var description: String,
    var language: String,
    var languageSymbolColor: String,
    var stars: Int,
    var forks: Int
)
