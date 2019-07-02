package com.cdsap.monitor.data

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.mysql.MySQLConnection
import com.github.jasync.sql.db.mysql.exceptions.MySQLException
import com.github.jasync.sql.db.pool.ConnectionPool
import com.cdsap.monitor.data.Queries.GET_EXPERIMENTS
import com.cdsap.monitor.data.Queries.GET_PODS_BY_EXPERIMENT
import com.cdsap.monitor.data.Queries.GET_POD_BY_EXPERIMENT_AND_NAME
import com.cdsap.monitor.data.Queries.INSERT_EXPERIMENT
import com.cdsap.monitor.data.Queries.INSERT_POD
import com.cdsap.monitor.data.Queries.UPDATE_POD_COUNTER
import com.cdsap.monitor.entities.domain.Pod
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking

class MySqlApiImpl(
        private val connectionPool: ConnectionPool<MySQLConnection>)
    : MySqlApi {

    override fun insertPod(pod: Pod) = queryResult(INSERT_POD,
            listOf(pod.name, pod.iterations, pod.experiment_id, pod.values, pod.configMap))


    override fun insertExperiment(name: String) = queryResult(INSERT_EXPERIMENT, listOf(name))

    override fun updatePodCounter(pod: Pod) = queryResult(UPDATE_POD_COUNTER,
            listOf(pod.iterations, pod.name, pod.experiment_id))

    override fun getExperiments() = queryResult(GET_EXPERIMENTS, emptyList())

    override fun getPodsByExperiment(name: String) = queryResult(GET_PODS_BY_EXPERIMENT, listOf(name))

    override fun getPodByExperimentAndName(experiment: String, name: String) =
            queryResult(GET_POD_BY_EXPERIMENT_AND_NAME, listOf(experiment, name))


    private fun queryResult(query: String, list: List<Any?>): QueryResult {
        return try {
            println("executing $query with $list")
            runBlocking {
                connectionPool.sendPreparedStatement(query, values = list).await()
            }
        } catch (mysqlException: MySQLException) {
            QueryResult(rowsAffected = 0L, statusMessage = mysqlException.message)
        }
    }
}

object Queries {
    const val INSERT_POD = "INSERT INTO `POD`(NAME,DATEINIT,COUNTER,EXPERIMENT_ID,EXPERIMENTS,CONFIGMAP)" +
            " VALUES (?,now(),?,?,?,?);"

    const val GET_EXPERIMENTS = "SELECT * FROM EXPERIMENT ORDER BY DATEINIT"

    const val GET_PODS_BY_EXPERIMENT = "SELECT * FROM POD WHERE EXPERIMENT_ID = ? ORDER BY NAME"

    const val GET_POD_BY_EXPERIMENT_AND_NAME = "SELECT * FROM POD WHERE EXPERIMENT_ID = ? AND NAME = ?"

    const val INSERT_EXPERIMENT = "INSERT INTO `EXPERIMENT`(NAME,DATEINIT) VALUES (?,now());"

    const val UPDATE_POD_COUNTER = "UPDATE POD SET COUNTER=? WHERE NAME=? AND EXPERIMENT_ID=?"

}