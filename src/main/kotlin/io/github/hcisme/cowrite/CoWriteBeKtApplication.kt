package io.github.hcisme.cowrite

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@MapperScan("io.github.hcisme.cowrite.mappers")
@SpringBootApplication
class CoWriteBeKtApplication

fun main(args: Array<String>) {
    runApplication<CoWriteBeKtApplication>(*args)
}
