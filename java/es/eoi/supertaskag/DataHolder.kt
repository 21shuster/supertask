package es.eoi.supertaskag

import es.eoi.supertaskag.models.Category
import es.eoi.supertaskag.models.Task

object DataHolder {

    val dbUsers = "users"
    val dbCategories = "categories"
    val dbTasks = "tasks"
    val idCategory = "category"

    var currentCategory: Category? = null
    var currentTask: Task? = null
}