package com.dvt.weather.interview.presentation.model

import com.dvt.weather.interview.data.model.Model

interface DataModelMapper<M : Model, VD : ViewData> {
    fun mapToModel(viewData: VD): M

    fun mapToViewData(model: M): VD
}
