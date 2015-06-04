import java.util.Date

case class ReportHeader(entity: Int, reportId:Int, title: String, createdDate: Date, userId: String)

case class ReportEntry(entity:Int, reportId:Int, category:Int, description:String, amout:Int, currency:Char)