package com.cdsap.monitor

import com.cdsap.monitor.data.MySqlApiImpl
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.html.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import com.cdsap.monitor.entities.request.PodRequest
import com.cdsap.monitor.repository.ExperimentRepositoryImpl
import com.cdsap.monitor.repository.PodRepositoryImpl
import kotlinx.html.*
import java.util.concurrent.TimeUnit


fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    connectionPool.connect().get()
    val mySqlApiImpl = MySqlApiImpl(connectionPool)
    val experimentRepository = ExperimentRepositoryImpl(mySqlApiImpl)
    val podRepository = PodRepositoryImpl(mySqlApiImpl)

    routing {
        get("/what") {
            call.respondHtml {
                head {
                    title {
                        +"Bagan: Experiments "
                    }
                }
                body {
                       p { +"Happen!"
                       }
                     }

                }
          }
        }
        get("/experiments") {
            call.respondHtml {
                val a = experimentRepository.getExperiments()
                println(a.size)
                head {
                    title {
                        +"Bagan: Experiments "
                    }
                }
                body {
                    a.forEach {
                        p {
                            a("/experiments/${it.name}") {
                                +it.name
                            }
                        }
                    }

                }
            }
        }
        get("/experiments/{experiment}") {
            call.respondHtml {

                val experiment = call.parameters["experiment"]
                val a = podRepository.getPodsByExperiment(experiment!!)
                println(a.size)
                head {
                    title { +"Bagan: Experiment $experiment" }
                }
                body {
                    a.forEach {
                        p {
                            +"${it.name} -- ${it.experiment_id}"
                        }
                    }
                }
            }
        }
        post("/experiments/{experiment}") {
            val experiment = call.parameters["experiment"]!!
            val result = experimentRepository.insertExperiment(experiment)
            call.respond(result)
        }
        post("/experiments/{id}/{pod}/") {
            val experiment = call.parameters["id"]!!
            val pod = call.parameters["pod"]!!
            val po = call.receive<PodRequest>()
            val result = podRepository.insertPod(experiment, pod, po)
            call.respond(result)
        }
        post("/experiments/{id}/{pod}/update") {
            val experiment = call.parameters["id"]!!
            val pod = call.parameters["pod"]!!
            val result = podRepository.updateCounterPod(experiment, pod)
            call.respond(result)
        }
    }
}

val connectionPool = MySQLConnectionBuilder.createConnectionPool {
    username = "root"
    host = "http://bagan-mysql"
    port = 3306
    password = "bagan"
    database = "bagan"
    maxActiveConnections = 100
    maxIdleTime = TimeUnit.MINUTES.toMillis(15)
    maxPendingQueries = 10_000
    connectionValidationInterval = TimeUnit.SECONDS.toMillis(30)
}
