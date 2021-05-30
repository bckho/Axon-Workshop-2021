package nl.rso.axon.happergame.queryside.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME
import org.springframework.boot.task.TaskExecutorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@ComponentScan
open class SpringConfig {

    @Lazy
    @Bean(name = [APPLICATION_TASK_EXECUTOR_BEAN_NAME])
    @ConditionalOnMissingBean(Executor::class)
    open fun applicationTaskExecutor(builder: TaskExecutorBuilder): ThreadPoolTaskExecutor {
        return builder.build()
    }



}