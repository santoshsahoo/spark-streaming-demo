package com.foo.datainsights

import java.util.Date

//class ReportHeader(var entity: Int, var reportId: Int, var title: String,
//                   var createdDate: Date, var userId: String, var total: Long) extends Serializable

class ReportEntry(entity: Int, reportId: Int,
                  category: Int, description: String, amount: Int, currency: Char) extends Serializable
