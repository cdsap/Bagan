package com.cdsap.bagan.experiments

class DashboardProvider(
    val experiments: List<Experiment>,
    val commands: List<String>
) {

    //   val indicatorBuilder = IndicatorBuilder()
    //   val dashboardBuilder = DashboardBuilder()
    //   val legendBuilder = LegendBuilder()


    fun generate() {
        // 1- Build Legend

        buildLegend()
        // Experiment 0  --> Gradle property1=A
        //                          property2=B


        // 2- Build Main DashBoard by command


        // For all the commands build the dashboard

        // 3- Build Main/Max indicators
        // for every command shw the best time pannel

    }

    fun buildLegend() {
        experiments.forEach {
            it.name + it.values
        }
    }

    fun graphs() {
        commands.forEach {

        }
    }

    fun minIndicators() {
        // winner

    }

}