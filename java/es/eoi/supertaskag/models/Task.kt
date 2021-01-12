package es.eoi.supertaskag.models

import com.google.firebase.Timestamp
import java.util.*

class Task {
    var id: String? = null
    var title: String? = null
    var description: String? = null
    var idCategory: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var priority: Int = 0
    var complete: Int = 0

    constructor(){
    }

    constructor(
        id: String,
        title: String,
        description: String,
        category: String,
        startDate: Date,
        endDate: Date,
        priority: Int,
        complete: Int
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.idCategory = category
        this.startDate = startDate
        this.endDate = endDate
        this.priority = priority
        this.complete = complete
    }
}