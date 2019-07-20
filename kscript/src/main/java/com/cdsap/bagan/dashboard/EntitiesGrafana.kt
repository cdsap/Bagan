package com.cdsap.bagan.dashboard

data class Target2(
    val measurement: String = "tracking",
    val orderByTime: String = "ASC",
    val policy: String = "default",
    val refId: String = "A",
    val resultFormat: String = "table",
    val query : String? = null,
    val rawQuery : Boolean = false,
    val groupBy: Array<Query> = arrayOf(Query(arrayOf("experiment"), type = "tag")),
    val select: Array<Array<Query>> = arrayOf(arrayOf(Query(arrayOf("value"), type = "field"))),
    val tags: Array<Tags>
)


data class Tags(
    val key: String,
    val operator: String,
    val value: String
)


data class Gridpos(
    val h: Int,
    val w: Int,
    val x: Int,
    val y: Int
)


data class Legend(
    val avg: Boolean = false,
    val current: Boolean = false,
    val max: Boolean = false,
    val min: Boolean = false,
    val show: Boolean = true,
    val total: Boolean = false,
    val values: Boolean = false
)

data class Query(val params: Array<String>, val type: String)



data class Target(
    val name: String,
    val orderByTime: String = "ASC",
    val policy: String = "default",
    val refId: String = "A",
    val resultFormat: String = "time_series",
    val tags: Array<Any> = emptyArray()
)

data class SparkLine(
    val fillColor: String = "rgba(31,118,189,0.18)",
    val full: Boolean = false,
    val lineColor: String = "rgba(31,120,193)",
    val show: Boolean = false
)

data class RangeMap(
    val from: String = "null",
    val text: String = "N/A",
    val to: String = "null"
)

data class MappingType(
    val name: String,
    val value: Int
)

data class Gauge(
    val maxValue: Int,
    val minValue: Int,
    val show: Boolean = true,
    val thresholdLabels: Boolean = false,
    val thresholdMarkers: Boolean = true
)


data class Xaxis(
    val buckets: Any? = null,
    val mode: String = "time",
    val name: Any? = null,
    val show: Boolean = true,
    val values: Array<String> = emptyArray()
)

data class Yaxes(
    val format: String = "short",
    val label: Any? = null,
    val logBase: Int = 1,
    val max: Any? = null,
    val min: Any? = null,
    val show: Boolean = true
)

data class Yaxis(
    val align: Boolean = false,
    val alignLevel: Any? = null
)

data class Tooltip(
    val shared: Boolean = true,
    val sort: Int = 0,
    val value_type: String = "individual"
)

class Options

data class Dashboard(
    val editable: Boolean = true,
    val gnetld: Any? = null,
    val graphTooltip: Int = 0,
    val schemaVersion: Int = 18,
    val style: String = "dark",
    val timezone: String = "",
    val title: String = "Bagan",
    val uid: String = "IS3q0sSWz",
    val version: Int = 5,
    val links: Array<String> = emptyArray(),
    val tags: Array<String> = emptyArray(),
    val annotations: Array<ListAnnotations> = arrayOf(ListAnnotations()),
    val templating: Array<ListAnnotations> = emptyArray(),
    val time: Time = Time(),
    val timePicker: TimePicker = TimePicker(),
    val panels: Array<Panel?>
)




data class Sort(val col: Int, val desc: Boolean)





data class TimePicker(
    val refresh_intervals: Array<String> =
        arrayOf("5s", "10s", "30s", "1m", "5m", "15m", "30m", "1h", "2h", "1d"),
    val time_options: Array<String> =
        arrayOf("5m", "15m", "1h", "6h", "12h", "24h")
)

data class Time(
    val from: String = "now-1h",
    val to: String = "now"
)

data class ListAnnotations(val list: Array<Annotation> = arrayOf(Annotation()))

data class Annotation(
    val buildln: Int = 1,
    val datasource: String = "-- Grafana --",
    val enable: Boolean = true,
    val hide: Boolean = true,
    val iconColor: String = "rgba(0, 211,255,1)",
    val name: String = "Annotations & Alert",
    val type: String = "dashboard"
)



