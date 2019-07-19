package com.cdsap.bagan.dashboard

import com.squareup.moshi.Json

enum class PanelType {
    graph,
    text,
    singlestat,
    table
}

sealed class Panel(
    @Json(name = "type") val panelType: PanelType,
    var timeFrom: Any? = null,
    var timeShift: Any? = null,
    val options: Options = Options(),
    val links: Array<String>? = emptyArray()
)



data class Table(
    val pageSize: Any? = null,
    val scroll: Boolean = true,
    val showHeader: Boolean = true,
    val id: Int,
    val gridPos: Gridpos,
    val sort: Sort = Sort(2, false),
    val title: String,
    val transform: String = "table",
    val targets: Array<Target2>
) : Panel(
    panelType = PanelType.table
)


data class Winner(
    val cacheTimeout: Boolean = false,
    val colorBackground: Boolean = false,
    val colorValue: Boolean = false,
    val format: String = "none",
    val id: Int,
    val interval: String? = null,
    val mappingType: Int = 1,
    val maxDataPoints: Int = 100,
    val nullPointMode: String = "connected",
    val nullText: String? = null,
    val postfix: String = "",
    val postfixFontSize: String = "50%",
    val prefix: String = "",
    val prefixFontSize: String = "50%",
    val tableColumn: String = "",
    val thresholds: String = "",
    val title: String,
    val type: String = "singlestat",
    val valueFontSize: String = "80%",
    val valueName: String,
    val gridPos: Gridpos,
    val colors: Array<String> = arrayOf(
        "#299c46", "rgba(237, 129, 40, 0.89)", "#d44a3a"
    ),
    val gauge: Gauge,
    val mappingTypes: Array<MappingType> = arrayOf(
        MappingType(name = "value to text", value = 1),
        MappingType(name = "range to text", value = 2)
    ),
    val rangeMaps: Array<RangeMap> = arrayOf(RangeMap()),
    val sparkLine: SparkLine = SparkLine()
) : Panel(PanelType.singlestat)


data class Graph(
    val aliasColors: Array<Any> = emptyArray(),
    var bars: Boolean = false,
    val dashLength: Int = 10,
    val dashes: Boolean = false,
    val dataSource: String = "InfluxDb",
    val fill: Int = 1,
    val id: Int,
    val lines: Boolean = true,
    val lineWidth: Int = 1,
    val nullPointMode: String = "null",
    val percentage: Boolean = false,
    val pointradius: Int = 2,
    val points: Boolean = false,
    val renderer: String = "flot",
    val spaceLength: Int = 10,
    val steppedLine: Boolean = false,
    val stack: Boolean = false,
    val title: String,
    val type: String = "graph",
    val gridPos: Gridpos,
    val legend: Legend = Legend(),
    val seriesOverrides: Array<String> = emptyArray(),
    val thresholds: Array<String> = emptyArray(),
    val timeRegions: Array<String> = emptyArray(),
    val tooltip: Tooltip = Tooltip(),
    val xaxis: Xaxis = Xaxis(),
    val yaxes: Array<Yaxes> = arrayOf(Yaxes()),
    val yaxis: Yaxis = Yaxis()
) : Panel(panelType = PanelType.graph)


data class Markdown(
    val content: String,
    val id: Int,
    val mode: String = "markdown",
    val title: String,
    val type: String = "text",
    val gridPos: Gridpos
) : Panel(PanelType.text)
