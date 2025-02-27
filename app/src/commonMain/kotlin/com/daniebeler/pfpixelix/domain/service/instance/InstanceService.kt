package com.daniebeler.pfpixelix.domain.service.instance

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.fediverse_logo
import pixelix.app.generated.resources.lemmy_logo
import pixelix.app.generated.resources.mastodon_logo
import pixelix.app.generated.resources.misskey_logo
import pixelix.app.generated.resources.peertube_logo
import pixelix.app.generated.resources.pixelfed_logo

@Inject
class InstanceService(
    private val api: PixelfedApi
) {

    fun getInstance() = loadResource {
        api.getInstance()
    }

    fun getNodeInfo(domain: String) = loadResource {
        api.getNodeInfo(domain)
    }

    fun getSoftwareFromFediDB(slug: String) = loadResource {
        api.getSoftwareFromFediDB(slug)
    }.map { event ->
        if (event is Resource.Success) {
            event.data.icon = when (event.data.slug) {
                "pixelfed" -> Res.drawable.pixelfed_logo
                "mastodon" -> Res.drawable.mastodon_logo
                "peertube" -> Res.drawable.peertube_logo
                "lemmy" -> Res.drawable.lemmy_logo
                "misskey" -> Res.drawable.misskey_logo
                else -> Res.drawable.fediverse_logo
            }
        }
        event
    }

    fun getServerFromFediDB(slug: String) = loadResource {
        api.getServerFromFediDB(slug)
    }
}