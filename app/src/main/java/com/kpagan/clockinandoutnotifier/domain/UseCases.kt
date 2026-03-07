package com.kpagan.clockinandoutnotifier.domain

class SaveClockInAndOutConfigUseCase(private val repository: ClockInAndOutRepository) {
    suspend operator fun invoke(config: ClockInAndOutConfig) {
        repository.saveConfig(config)
    }
}

class GetClockInAndOutConfigUseCase(private val repository: ClockInAndOutRepository) {
    suspend operator fun invoke(): ClockInAndOutConfig {
        return repository.getConfig()
    }
}

class ObserveClockInAndOutConfigUseCase(private val repository: ClockInAndOutRepository) {
    operator fun invoke() = repository.observeConfig()
}