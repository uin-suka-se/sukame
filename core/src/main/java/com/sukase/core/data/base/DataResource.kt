package com.sukase.core.data.base

import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.base.DomainThrowable

sealed class DataResource<out R> private constructor() {
    data class Success<out T>(val data: T) : DataResource<T>() {
        fun mapToDomainResource(): DomainResource<T> =
            if (data != null) DomainResource.Success(data) else DomainResource.Empty
    }

    data class Error(val domainThrowable: DomainThrowable) : DataResource<Nothing>() {
        fun mapToDomainResource(): DomainResource.Error =
            DomainResource.Error(domainThrowable.mapToUiText())
    }

    data object Loading : DataResource<Nothing>() {
        fun mapToDomainResource(): DomainResource.Loading = DomainResource.Loading
    }
}



